package com.tpm.mobile.securiyt.helper;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;
import com.tpm.mobile.log.LogServiceClient;

public class RegisterHelper {
	
	private static final Logger log = Logger.getLogger(RegisterHelper.class);
	
	private static String host;
	private static Integer port;
	private static String dbName;
	
	
	static{
		Properties prop = new Properties();
		try {
			prop.load(LogServiceClient.class.getClassLoader()
					.getResourceAsStream("config.properties"));
			host = prop.getProperty("MONGODB_HOST");
			port = Integer.parseInt(prop.getProperty("MONGODB_PORT")) ;
			dbName = prop.getProperty("MONGODB_NAME");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	
	public boolean storeUserInfoInMangoDb(JSONObject userInfo) {
		// TODO Auto-generated method stub
		boolean isStored= false;
		MongoClient mongo=null;
		try {
			
			mongo = new MongoClient(host, port);
			DB db = mongo.getDB(dbName);
			BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("email",  userInfo.get("email").toString());
			DBCollection collection = db.getCollection("user.files");
			DBCursor cursor = collection.find(whereQuery);
			
			Integer userCount=(int) collection.count();
			userCount=userCount+1;
			if(!cursor.hasNext()) {
				BasicDBObject document = new BasicDBObject();
				document.put("userId", userCount.toString());
				document.put("userType", "trail");
				document.put("firstName", userInfo.get("firstName").toString());
				document.put("lastName", userInfo.get("lastName").toString());
				document.put("userName",userInfo.get("userName").toString());
				document.put("email",userInfo.get("email").toString());
				document.put("password",userInfo.get("password").toString());
				collection.insert(document);
				isStored= true;
			} 
		} catch(Exception e){
			e.printStackTrace();
			log.error(e);
		} finally {
			if(mongo!=null){
				mongo.close();
			}
		}
		
		return isStored;
		
	}

	
	
  public JSONObject isAuthenticated(JSONObject user) {
	  JSONObject userInfo=null;
	  MongoClient mongo=null;
	  //log.info(user.toJSONString());
		try {
			
			mongo = new MongoClient(host, port);
			DB db = mongo.getDB(dbName);
				BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("email",  user.get("username").toString());
			whereQuery.put("password",  user.get("password").toString());
			DBCollection collection = db.getCollection("user.files");
			DBCursor cursor = collection.find(whereQuery);
			if (cursor.hasNext()) {
				
		    	DBObject result= cursor.next();
		    	JSONParser jsonParser = new JSONParser();
		    	userInfo = (JSONObject)jsonParser.parse(result.toString().replace("\\\"", "'"));
		        //log.info(userInfo);
		    	
		    }
		} catch(Exception e){
			e.printStackTrace();
			log.error(e);
		} finally {
			if(mongo!=null){
				mongo.close();
			}
		}
		
		return userInfo;
   
  }
  public JSONObject updateLoginStaus(JSONObject user) throws UnknownHostException, ParseException {
		MongoClient mongo=null;
		 JSONObject userInfo=null;
		mongo = new MongoClient(host, port);
		DB db = mongo.getDB(dbName);
		DBCollection collection = db.getCollection("user.files");
		DBObject query = new BasicDBObject("email", user.get("email").toString());
  		DBObject update = new BasicDBObject();
  		WriteResult upres = null;
		if((boolean)user.get("login")==true) {
	  		update.put("$set", new BasicDBObject("login",false));
	  		upres = collection.update(query, update);
	  	} else {
	  		update.put("$set", new BasicDBObject("login",true));
	  		upres = collection.update(query, update);
	  	}
		DBCursor cursor = collection.find(query);
		if (cursor.hasNext()) {
			
	    	DBObject result= cursor.next();
	    	JSONParser jsonParser = new JSONParser();
	    	userInfo = (JSONObject)jsonParser.parse(result.toString().replace("\\\"", "'"));
	        //log.info(userInfo);
	    	
	    }
		return userInfo;
	  
  }
}