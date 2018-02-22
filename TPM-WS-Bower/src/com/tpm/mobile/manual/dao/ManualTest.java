package com.tpm.mobile.manual.dao;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.tpm.mobile.log.LogServiceClient;

public class ManualTest implements IManualTest{
	private Mongo mongo =null;
	private static final Logger log = Logger.getLogger(ManualTest.class);

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
	@Override
	public boolean storeManualtestInMangoDb(JSONArray test, String testName,JSONObject app, JSONObject user) {
		// TODO Auto-generated method stub
		boolean isStored= false;
		try{
			mongo = new MongoClient(host, port);
			DB db = mongo.getDB(dbName);
			BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("userId", user.get("userId").toString());
			whereQuery.put("appId", app.get("id").toString());
			whereQuery.put("testName", testName);
			DBCollection collection = db.getCollection("manualtest.files");
			DBCursor cursor = collection.find(whereQuery);
			if(!cursor.hasNext()) {
				BasicDBObject document = new BasicDBObject();
				document.put("userId", user.get("userId").toString());
				document.put("appId", app.get("id").toString());
				document.put("test",test.toString());
				document.put("testName",testName);
				String appType = app.get("appType").toString();
				appType=appType.replaceAll("^\"|\"$", "");
				document.put("appType",appType);
				
				
				// save the data document into mongoDB
				collection.insert(document);
				isStored= true;
			} 
		}catch(Exception e){
			e.printStackTrace();
			log.error(e);
		} finally {
			if(mongo!=null){
				mongo.close();
			}
			
		}
		
		return isStored;
		
	}

	@Override
	public void checkManualtestNameInMangoDb(String testName, JSONObject appId, JSONObject user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean updateManualtestInMangoDbWithResult(JSONArray test, String testName, JSONObject app, String deviceId, JSONObject user) {
		// TODO Auto-generated method stub
			boolean isUpdate= false;
			try{
				mongo = new MongoClient(host, port);
				DB db = mongo.getDB(dbName);
				DBCollection collection = db.getCollection("manualtest.files");

				BasicDBObject newDocument = new BasicDBObject();
				BasicDBObject newitem = new BasicDBObject();
				
				newDocument.append("$set", newitem.append("test", test.toString()));
				newDocument.append("$set", newitem.append("deviceId", deviceId));
				newDocument.append("$set", newitem.append("testType", "manual"));
				newDocument.append("$set", newitem.append("testSenario", "manual"));
				DateFormat simpleDateFormat =
		                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String exeTime = simpleDateFormat.format(new Date());
				newDocument.append("$set", newitem.append("excTime", exeTime));
			
				
				BasicDBObject searchQuery = new BasicDBObject();
				searchQuery.append("userId", user.get("userId").toString());
				searchQuery.append("appId", app.get("id").toString());
				searchQuery.append("testName", testName);
				collection.update(searchQuery, newDocument);
				
				isUpdate= true;
				
			}catch(Exception e){
				e.printStackTrace();
				log.error(e);
			} finally {
				if(mongo!=null){
					mongo.close();
				}
				
			}
			
			return isUpdate;
	}

	@Override
	public JSONObject getManualtestResultFromMangoDb(String testName, JSONObject app, String deviceId, JSONObject user) {
		JSONObject testResult = null;
		 
		try{
		// TODO Auto-generated method stub
			mongo = new MongoClient(host, port);
			DB db = mongo.getDB(dbName);
			DBCollection collection = db.getCollection("results.files");
			BasicDBObject whereQuery = new BasicDBObject();
			log.info(testName);
			log.info(deviceId);
			log.info(app.get("id").toString());
			log.info(user.get("userId"));

		    whereQuery.put("testName", testName);
		    whereQuery.put("deviceId", deviceId);
		    whereQuery.put("appId", app.get("id").toString());
		    whereQuery.put("userId", user.get("userId"));
		    DBCursor cursor = collection.find(whereQuery);
		    if (cursor.hasNext()) {
		    	DBObject result= cursor.next();
		    	log.info(result);
		    	JSONParser jsonParser = new JSONParser();
		    	    testResult = (JSONObject)jsonParser.parse(result.toString().replace("\\\"", "'"));
		        log.info(testResult);
		    }
		}catch(Exception e){
			e.printStackTrace();
			log.error(e);
		} finally {
			if(mongo!=null){
				mongo.close();
			}
			
		}
		return testResult;
	}
	@Override
	public boolean storeManualTestResultsInMangoDb(JSONObject results, JSONObject user){
		// TODO Auto-generated method stub
				boolean isStored= false;
				JSONObject app=(JSONObject) results.get("app");
				try{
					mongo = new MongoClient(host, port);
					DB db = mongo.getDB(dbName);
					
					BasicDBObject whereQuery = new BasicDBObject();
					whereQuery.put("userId", user.get("userId").toString());
					whereQuery.put("appId",  app.get("id").toString());
					whereQuery.put("testName", results.get("testName"));
					whereQuery.put("testType", "execution");
					DBCollection collection = db.getCollection("results.files");
					DBCursor cursor = collection.find(whereQuery);
					if(!cursor.hasNext()) {
						
							BasicDBObject document = new BasicDBObject();
							document.put("userId", user.get("userId").toString());
							document.put("appId", app.get("id").toString());
							document.put("test",results.get("test"));
							document.put("testName", results.get("testName"));
							document.put("deviceId",  results.get("deviceId"));
							document.put("testType", "execution");
							document.put("testSenario",  "manual");
							document.put("testResult", results.get("testResult") );
							if(results.get("logs")!=null){
								document.put("logs", results.get("logs").toString());
							}
						
							DateFormat simpleDateFormat =
					                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							String uploaddate = simpleDateFormat.format(new Date());
							document.put("excTime", uploaddate);
							
							// save the data document into mongoDB
							collection.insert(document);
							isStored= true;
						
						
					} 
				}catch(Exception e){
					e.printStackTrace();
					log.error(e);
				} finally {
					if(mongo!=null){
						mongo.close();
					}
					
				}
				
				return isStored;
		
	}

}
