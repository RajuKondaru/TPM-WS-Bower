package com.tpm.mobile.results.dao;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
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
import com.mongodb.MongoException;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.tpm.mobile.log.LogServiceClient;

public class ExecResults implements IExecResults {
	private static final Logger log = Logger.getLogger(ExecResults.class);
	
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
	public List<JSONObject>  getAllResults(JSONObject user) {
		List<JSONObject> results = new ArrayList<JSONObject>();
		JSONObject testResult = null;
		MongoClient mongo=null;
		try {
			
			mongo = new MongoClient(host, port);
			DB db = mongo.getDB(dbName);
			DBCollection collection = db.getCollection("results.files");
			collection.setObjectClass(null);
			BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("userId",  user.get("userId"));
			BasicDBObject excludeQuery = new BasicDBObject();
			excludeQuery.append("logs", 0);
			DBCursor cursor = collection.find(whereQuery, excludeQuery);
			DBObject result;
			while(cursor.hasNext()) {
				result=cursor.next();
				String resultstr= result.toString().replace("\\\"", "'");
				//log.info(result);
				JSONParser jsonParser = new JSONParser();
	    	    testResult = (JSONObject)jsonParser.parse(resultstr);
	    	   // log.info(testResult);
				results.add(testResult);
			}
			//  log.info(results);
		} catch (MongoException e) {
			log.error(e);
			//log.error(e);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(mongo!=null){
				mongo.close();
			}
		}
		return results;
	}
	@Override
	public InputStream getVideoAsStream(String filename, JSONObject user) {
		// TODO Auto-generated method stub
		InputStream is= null;
		MongoClient mongo=null;
		try {
			
			mongo = new MongoClient(host, port);
			DB db = mongo.getDB(dbName);
			
			
			GridFS gridFs = new GridFS(db,"results");
			//Find the image with the name image1 using GridFS API
			BasicDBObject query = new BasicDBObject();
			query.put("filename", filename);
			query.put("userId",  user.get("userId"));
			GridFSDBFile appFile = gridFs.findOne(query);
			if (appFile != null ){
				//Get the number of chunks
				 is=appFile.getInputStream();
			 }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return is;
	}
	@Override
	public InputStream getScreenShotAsStream(String filename) {
		// TODO Auto-generated method stub
		InputStream is= null;
		MongoClient mongo=null;
		try {
			
			mongo = new MongoClient(host, port);
			DB db = mongo.getDB(dbName);
			
			GridFS gridFs = new GridFS(db,"screenshots");
			//Find the image with the name image1 using GridFS API
			BasicDBObject query = new BasicDBObject();
			query.put("filename", filename);
			GridFSDBFile appFile = gridFs.findOne(query);
			if (appFile != null ){
				//Get the number of chunks
				 is=appFile.getInputStream();
			 }
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return is;
	}
	
	@Override
	public JSONObject getResult(String testName, JSONObject app, String deviceId, JSONObject user) {
		
		JSONObject testResult = null;
		MongoClient mongo=null;
		try {
			
			mongo = new MongoClient(host, port);
			DB db = mongo.getDB(dbName);
			
			DBCollection collection = db.getCollection("results.files");
			collection.setObjectClass(null);
			BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("testName", testName);
			whereQuery.put("deviceId", deviceId);
			whereQuery.put("userId",  user.get("userId"));
			if(app.get("id")!=null){
				whereQuery.put("appId", app.get("id").toString());
			} else if (app.get("appId")!=null) {
				whereQuery.put("appId", app.get("appId").toString());
			}
			BasicDBObject excludeQuery = new BasicDBObject();
			excludeQuery.append("logs", 0);
			
			DBCursor cursor = collection.find(whereQuery, excludeQuery);
			DBObject resultInfo = null;
			if(cursor.hasNext()) {
				resultInfo=cursor.next();
				String resultstr= resultInfo.toString().replace("\\\"", "'");
			
				JSONParser jsonParser = new JSONParser();
	    	    testResult = (JSONObject)jsonParser.parse(resultstr);
			}
			//log.info(resultInfo.toString());
		} catch (MongoException e) {
			log.error(e);
			//log.error(e);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if(mongo!=null){
				mongo.close();
			}
		}
		return testResult;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getScreenShotInfo(String session) {
		
		JSONObject screenShotInfo = null;
		MongoClient mongo=null;
		try {
			
			mongo = new MongoClient(host, port);
			DB db = mongo.getDB(dbName);
			
			DBCollection collection = db.getCollection("screenshots.files");
			BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("sessionId", session);
			DBCursor cursor = collection.find(whereQuery);
			DBObject result;
			screenShotInfo = new JSONObject();
			Integer index=1;
			while(cursor.hasNext()) {
				result=cursor.next();
				String filename= (String) result.get("filename");
				screenShotInfo.put(index, filename);
				index++;
			}
			//  log.info(results);
		} catch (MongoException | UnknownHostException e) {
			
		} finally {
			if(mongo!=null){
				mongo.close();
			}
		}
		return screenShotInfo;
	}
	@Override
	public InputStream getLogsAsStream(String testName, String logType, String deviceId ) {
		// TODO Auto-generated method stub
		InputStream is= null;
		MongoClient mongo=null;
		try {
			
			mongo = new MongoClient(host, port);
			DB db = mongo.getDB(dbName);
			
			DBCollection collection = db.getCollection("results.files");
			BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("testName", testName);
			whereQuery.put("deviceId", deviceId);
			DBCursor cursor = collection.find(whereQuery);
			
		/*	
			GridFS gridFs = new GridFS(db,"results");
			//Find the image with the name image1 using GridFS API
			BasicDBObject query = new BasicDBObject();
			query.put("testName", testName);
			
			GridFSDBFile appFile = gridFs.findOne(query);*/
			if(cursor.hasNext()) {
			//if (appFile != null ){
				//Get the number of chunks
				DBObject result = cursor.next();
				//System.out.println(result.toString());
				DBObject logs=(DBObject) result.get("logs");
				String logtype=logs.get(logType).toString();
				
				is = new ByteArrayInputStream(logtype.getBytes(StandardCharsets.UTF_8));
				 
			 }
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(mongo!=null){
				mongo.close();
			}
		}
		
		return is;
	}

}
