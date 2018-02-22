package com.tpm.mobile.android.exe.api.robotium.dao;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;
import com.tpm.mobile.android.pojo.ResultPojo;
import com.tpm.mobile.log.LogServiceClient;

public class RoboTestDao implements IRoboTestDao{
	
	private static final Logger log = Logger.getLogger(RoboTestDao.class);
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
	public boolean storeRoboTestInMangoDb(JSONArray test, String testName,JSONObject app, String userId) {
		// TODO Auto-generated method stub
		boolean isStored= false;
		MongoClient mongo=null;
		try {
			
			mongo = new MongoClient(host, port);
			DB db = mongo.getDB(dbName);
			
			//log.info(app.toString());
			BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("userId", userId);
			whereQuery.put("appId", app.get("id").toString());
			whereQuery.put("testName", testName);
			whereQuery.put("testType", "robotium");
			DBCollection collection = db.getCollection("robotest.files");
			DBCursor cursor = collection.find(whereQuery);
			if(!cursor.hasNext()) {
				BasicDBObject document = new BasicDBObject();
				document.put("userId", userId);
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
	public void checkRoboTestNameInMangoDb(String testName, JSONObject appId, String userId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean updateRoboTestInMangoDbWithResult(JSONArray test, String testName, JSONObject app, String deviceId, String userId) {
		// TODO Auto-generated method stub
			boolean isUpdate= false;
			MongoClient mongo=null;
			try {
				
				mongo = new MongoClient(host, port);
				DB db = mongo.getDB(dbName);
				
				DBCollection collection = db.getCollection("robotest.files");

				BasicDBObject newDocument = new BasicDBObject();
				BasicDBObject newitem = new BasicDBObject();
				
				newDocument.append("$set", newitem.append("test", test.toString()));
				newDocument.append("$set", newitem.append("deviceId", deviceId));
				newDocument.append("$set", newitem.append("testType", "robotuim"));
				DateFormat simpleDateFormat =
		                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String exeTime = simpleDateFormat.format(new Date());
				newDocument.append("$set", newitem.append("excTime", exeTime));
			
				
				BasicDBObject searchQuery = new BasicDBObject();
				searchQuery.append("userId", userId);
				searchQuery.append("appId", app.get("id").toString());
				searchQuery.append("testName", testName);
				searchQuery.append("testType", "robotuim");
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
	public JSONObject getRoboTestInfoFromMangoDb(String testName, JSONObject app, String deviceId, String userId) {
		 JSONObject testInfo = null;
		 MongoClient mongo=null;
			try {
				
				mongo = new MongoClient(host, port);
				DB db = mongo.getDB(dbName);
				
		
				DBCollection collection = db.getCollection("robotest.files");
			
				//log.info(testName);
				//log.info(deviceId);
				//log.info(app.get("id").toString());
			
				BasicDBObject whereQuery = new BasicDBObject();
			    whereQuery.put("testName", testName);
			    whereQuery.put("deviceId", deviceId);
			    whereQuery.put("appId", app.get("id").toString());
			    whereQuery.put("userId", userId);
			    DBCursor cursor = collection.find(whereQuery);
			    if (cursor.hasNext()) {
			    	DBObject result= cursor.next();
			    	//log.info(result);
			    	JSONParser jsonParser = new JSONParser();
			    	 testInfo = (JSONObject)jsonParser.parse(result.toString().replace("\\\"", "'"));
			        //log.info(testInfo);
			    }
		}catch(Exception e){
			e.printStackTrace();
			log.error(e);
		} finally {
			if(mongo!=null){
				mongo.close();
			}
		}
		return testInfo;
	}

	@Override
	public boolean storeRoboTestResultsInMangoDb(JSONObject results, String userId) {
		// TODO Auto-generated method stub
		boolean isStored= false;
		JSONObject test=(JSONObject) results.get("test");
		JSONObject app=(JSONObject)test.get("app");
		MongoClient mongo=null;
		try {
			
			mongo = new MongoClient(host, port);
			DB db = mongo.getDB(dbName);
			// create a "results" namespace
			GridFS gfsVedio = new GridFS(db, "results");

			BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("userID", userId);
			whereQuery.put("appId",  app.get("id").toString());
			whereQuery.put("testName", test.get("name"));
			whereQuery.put("testType", test.get("type"));
			whereQuery.put("testSenario",  test.get("senario"));
			DBCollection collection = db.getCollection("results.files");
			DBCursor cursor = collection.find(whereQuery);
			if(!cursor.hasNext()) {
				File vedioFile = new File(results.get("vedioPath").toString());
				// get vedio file from local drive
				GridFSInputFile gfsFile = gfsVedio.createFile(vedioFile);
				gfsFile.setFilename(test.get("name")+results.get("deviceid").toString()+".mp4");
				gfsFile.setContentType("multipart");
				gfsFile.put("userId", userId);
				gfsFile.put("appId", app.get("id").toString());
				gfsFile.put("testName", test.get("name"));
				gfsFile.put("deviceId",  results.get("deviceid"));
				gfsFile.put("testType", test.get("type"));
				gfsFile.put("testName", test.get("name"));
				gfsFile.put("testResult", results.get("testResult"));
				gfsFile.put("testSenario",  test.get("senario"));
				gfsFile.put("logs", results.get("logs"));
				DateFormat simpleDateFormat =
		                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String uploaddate = simpleDateFormat.format(new Date());
				gfsFile.put("excTime", uploaddate);
				
				// save the image file into mongoDB
				gfsFile.save();
				Files.delete(Paths.get(results.get("vedioPath").toString()));
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
	public ResultPojo getRoboTestResultFromMangoDb(String testName, JSONObject app, String deviceId, String userId) {
		ResultPojo testResult = null;
		MongoClient mongo=null;
		try {
			
			mongo = new MongoClient(host, port);
			DB db = mongo.getDB(dbName);
			DBCollection collection = db.getCollection("results.files");
			BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("testName", testName);
			whereQuery.put("deviceId", deviceId);
			whereQuery.put("userId",  userId);
			whereQuery.put("appId", app.get("id").toString());
			DBCursor cursor = collection.find(whereQuery);
			DBObject resultInfo;
			if(cursor.hasNext()) {
				 resultInfo = cursor.next();
				 String testType = resultInfo.get("testType").toString();
				 String logs = resultInfo.get("logs").toString();
				 String uploadTime = resultInfo.get("excTime").toString();
				 DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				 Date upTime=simpleDateFormat.parse(uploadTime);
				 String filename = null;
				 filename=resultInfo.get("filename").toString();
				 //Create instance of GridFS implementation
				 JSONParser parser = new JSONParser();
				 testResult= new ResultPojo();
				 testResult.setTestName(testName);
				 testResult.setDeviceid(deviceId);
				 testResult.setUserID(userId);
				 testResult.setAppId(app.get("id").toString());
				 testResult.setFilename(filename);
				 testResult.setTestType(testType);
				 testResult.setUploadTime(upTime);
				 if(logs!=null && !logs.isEmpty() ){
					 JSONObject jsonLogs = (JSONObject) parser.parse(logs);
					 testResult.setLogs(jsonLogs);
				 }
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

	

}
