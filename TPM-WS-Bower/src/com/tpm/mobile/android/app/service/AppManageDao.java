package com.tpm.mobile.android.app.service;


import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import com.tpm.mobile.android.pojo.AppPojo;
import com.tpm.mobile.log.LogServiceClient;

@Repository
public class AppManageDao implements IAppManageDao {
	private static final Logger log = Logger.getLogger(AppManageDao.class);
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
	public boolean storeAppInMangoDB(AppPojo app, String userId) throws ParseException {
		// TODO Auto-generated method stub
		boolean isStored= false;
		MongoClient mongo=null;
		try {
			
			mongo = new MongoClient(host, port);
			DB db = mongo.getDB(dbName);
		
			String newFileName = app.getFileName();

			File appFile = new File(app.getDownloadedAppPath());

			// create a "photo" namespace
			GridFS gfsPhoto = new GridFS(db, "appfile");

			// get image file from local drive
			GridFSInputFile gfsFile = gfsPhoto.createFile(appFile);
			
			BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("userId", userId);
			DBCollection collection = db.getCollection("appfile.files");
			
			DBCursor cursor = collection.find(whereQuery);
			
			DBObject appinfo;
			int appid = 0;
			int counter = 0;
			while(cursor.hasNext()) {
				 appinfo=cursor.next();
				 String id=appinfo.get("appId").toString();
				 if(counter < Integer.parseInt(id))
		            {
					 counter = Integer.parseInt(id);
		            }
				 appid=counter;
			}
			// set a new filename for identify purpose
			gfsFile.put("fileName",newFileName);
			gfsFile.setContentType("multipart");
			gfsFile.put("userId", userId);
			gfsFile.put("appId", appid+1);
			gfsFile.put("appName", app.getAppName());
			gfsFile.put("appType", app.getAppType());
			gfsFile.put("appPackageName", app.getAppPackageName());
			gfsFile.put("activity", app.getActivityName());
			gfsFile.put("icon", app.getIcon());
			gfsFile.put("defaultIcon", app.getDefaultIcon());
			gfsFile.put("version", app.getVersion());
			gfsFile.put("buildId", app.getBuildId());
			gfsFile.put("status", app.getStatus());
			DateFormat simpleDateFormat =
	                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String uploaddate = simpleDateFormat.format(new Date());
			
		
			gfsFile.put("uploadTime", uploaddate);
			
			// save the image file into mongoDB
			gfsFile.save();

			// print the result
			
			while (cursor.hasNext()) {
				log.info(cursor.next());
			}
			
			isStored= true;
		} catch (UnknownHostException e) {
			log.error(e);
		} catch (MongoException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		} finally {
			if(mongo!=null){
				mongo.close();
			}
		}
		return isStored;
	}
	@Override
	public AppPojo getAppInfoFromMangoDB(AppPojo app, String userId) throws ParseException, IOException {
		MongoClient mongo=null;
		try {
			
			mongo = new MongoClient(host, port);
			DB db = mongo.getDB(dbName);
		
			DBCollection collection = db.getCollection("appfile.files");
			BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("userId", userId);
			whereQuery.put("appId", app.getId());
			DBCursor cursor = collection.find(whereQuery);
			DBObject appinfo;
			while(cursor.hasNext()) {
				 appinfo=cursor.next();
				 String icon;
				 String weburl;
				 String filename = null;
				 String appType=appinfo.get("appType").toString();
				 app = new AppPojo();
				if( appType.equalsIgnoreCase("webApp")){
					 icon=appinfo.get("icon").toString();
					 log.info(icon);
					 app.setIcon(icon);
					 weburl=appinfo.get("webUrl").toString();
					 app.setWebUrl(weburl);
					 
				} else {
					 icon=appinfo.get("icon").toString();
					 
					 app.setIcon(icon);
					 filename=appinfo.get("fileName").toString();
					 app.setFileName(filename);
					 //Create instance of GridFS implementation
					 GridFS gridFs = new GridFS(db,"appfile");
					 //Find the image with the name image1 using GridFS API
					 BasicDBObject query = new BasicDBObject();
					   query.put("fileName", filename);

					 GridFSDBFile appFile = gridFs.findOne(query);
					 if (appFile != null ){
						//Get the number of chunks
						 //log.info("appFile: " + appFile);
					    // log.info("Total Chunks: " + appFile.numChunks());
	 				     //Location of the image read from MongoDB to be written
						
						 String downloadPath = FileUtils.getUserDirectory()+"\\AppData\\Local\\Temp\\"+filename;
						 appFile.writeTo(downloadPath);
						 app.setDownloadedAppPath(downloadPath);
					 }
					 if(appinfo.get("activity")!=null){
						 String activity=appinfo.get("activity").toString();
						 app.setActivityName(activity);
					 }
					
					 String appPackageName=appinfo.get("appPackageName").toString();
					 String buildId=appinfo.get("buildId").toString();
					
					app.setAppPackageName(appPackageName);
					app.setBuildId(buildId);
 				     
				}
				 String userid=appinfo.get("userId").toString();
				 String defaluticon=appinfo.get("defaultIcon").toString();
				 String appName=appinfo.get("appName").toString();
				
				 String version=appinfo.get("version").toString();
				
				 String status=appinfo.get("status").toString();
				 String uploadTime=appinfo.get("uploadTime").toString();
				 int id=(int) appinfo.get("appId");
				 DateFormat simpleDateFormat =
			                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				 Date upTime=simpleDateFormat.parse(uploadTime);
				 
				 app.setUserId(userid);
				 app.setAppType(appType);
				 app.setAppName(appName);
				 app.setDefaultIcon(defaluticon);
				 app.setUploadedDate(upTime);
				 app.setVersion(version);
				 app.setStatus(status);
				 app.setId(id);
				
			}
			
		} catch (UnknownHostException e) {
			log.error(e);
			//log.error(e);
		} catch (MongoException e) {
			log.error(e);
			//log.error(e);
		}finally {
			if(mongo!=null){
				mongo.close();
			}
		}
		return app;
	}
	
	@Override
	public AppPojo getAppAUTInfoFromMangoDB(AppPojo app, String userId) throws ParseException, IOException {
		MongoClient mongo=null;
		try {
			
			mongo = new MongoClient(host, port);
			DB db = mongo.getDB(dbName);
		
			DBCollection collection = db.getCollection("autfile.files");
			BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("userId", userId);
			whereQuery.put("appId", app.getId());
			whereQuery.put("autId", app.getAutId());
			DBCursor cursor = collection.find(whereQuery);
			DBObject appinfo;
			if(cursor.hasNext()) {
				 appinfo=cursor.next();
				 String filename = null;
				 String appType=appinfo.get("appType").toString();
				
				 app.setAppType(appType);
				
				 filename=appinfo.get("filename").toString();
				 app.setFileName(filename);
				 //Create instance of GridFS implementation
				 GridFS gridFs = new GridFS(db,"autfile");
				 BasicDBObject query = new BasicDBObject();
				 query.put("filename", filename);
   			     //Find the image with the name image1 using GridFS API
				 GridFSDBFile appFile = gridFs.findOne(query);
				 if (appFile != null ){
	  				 //Get the number of chunks
					 //log.info("appFile: " + appFile);
					 //Location of the image read from MongoDB to be written
				     String downloadPath = FileUtils.getUserDirectory()+"\\AppData\\Local\\Temp\\"+filename;
				     appFile.writeTo(downloadPath);
				     app.setDownloadedAppPath(downloadPath);
				 }
			}
			
		} catch (UnknownHostException e) {
			log.error(e);
			//log.error(e);
		} catch (MongoException e) {
			log.error(e);
			//log.error(e);
		} finally {
			if(mongo!=null){
				mongo.close();
			}
		}
		return app;
	}

	@Override
	public List<AppPojo> getAppsInfoFromMangoDB(String userId) throws ParseException {
		List<AppPojo> applist = new ArrayList<AppPojo>();
		AppPojo app=null;
		MongoClient mongo=null;
		try {
			
			mongo = new MongoClient(host, port);
			DB db = mongo.getDB(dbName);
		
			DBCollection collection = db.getCollection("appfile.files");
			BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("userId", userId);
			DBCursor cursor = collection.find(whereQuery);
			DBObject appinfo;
			while(cursor.hasNext()) {
				 appinfo=cursor.next();
				 String icon;
				 String weburl;
				 String filename = null;
				 String appPackageName = null;
				 String activity = null;
				 String buildId=null;
				 String appType=appinfo.get("appType").toString();
				 app = new AppPojo();
				if( appType.equalsIgnoreCase("webapp")){
					 icon=appinfo.get("icon").toString();
					 log.info(icon);
					 app.setIcon(icon);
					 weburl=appinfo.get("webUrl").toString();
					 app.setWebUrl(weburl);
					 
				} else {
					 icon=appinfo.get("icon").toString();
					 
					 app.setIcon(icon);
					 filename=appinfo.get("fileName").toString();
					 app.setFileName(filename);
					 appPackageName=appinfo.get("appPackageName").toString();
					 app.setAppPackageName(appPackageName);
					 if(appinfo.get("activity")!=null){
						 activity=appinfo.get("activity").toString();
						 app.setActivityName(activity);
					 }
					 
					 buildId=appinfo.get("buildId").toString();
				}
				 String userid=appinfo.get("userId").toString();
				 String defaluticon=appinfo.get("defaultIcon").toString();
				 String appName=appinfo.get("appName").toString();
				 String version=appinfo.get("version").toString();
				
				
				 String status=appinfo.get("status").toString();
				 String uploadTime=appinfo.get("uploadTime").toString();
				 int id=(int) appinfo.get("appId");
				 DateFormat simpleDateFormat =
			                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				 Date upTime=simpleDateFormat.parse(uploadTime);
				 app.setBuildId(buildId);
				 app.setUserId(userid);
				 app.setAppType(appType);
				 app.setAppName(appName);
				 app.setDefaultIcon(defaluticon);
				 app.setUploadedDate(upTime);
				 app.setVersion(version);
				 app.setStatus(status);
				 app.setId(id);
				 applist.add(app); 
			}
			
		} catch (MongoException | UnknownHostException e) {
			log.error(e);
			//log.error(e);
		} finally {
			if(mongo!=null){
				mongo.close();
			}
		}
		return applist;
	}

	@Override
	public boolean updateAppInfoFromMangoDB(AppPojo app, String userId) throws ParseException {
		MongoClient mongo=null;
		try {
			
			mongo = new MongoClient(host, port);
			DB db = mongo.getDB(dbName);
		
			DBCollection collection = db.getCollection("appfile.files");
			BasicDBObject newDocument = new BasicDBObject();
			newDocument.append("$set", new BasicDBObject().append("status", app.getStatus()));

			BasicDBObject searchQuery = new BasicDBObject().append("appId", app.getId());

			collection.update(searchQuery, newDocument);
			
		} catch (MongoException | UnknownHostException e) {
			log.error(e);
		} finally {
			if(mongo!=null){
				mongo.close();
			}
		}
		return true;
	}

	

	@Override
	public void storeWebAppInMangoDB(AppPojo app, String userId) throws IOException {
		MongoClient mongo=null;
		try {
			
			mongo = new MongoClient(host, port);
			DB db = mongo.getDB(dbName);
		
			BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("userId", userId);
			DBCollection collection = db.getCollection("appfile.files");
			
			DBCursor cursor = collection.find(whereQuery);
			DBObject appinfo;
			int appid = 0;
			while(cursor.hasNext()) {
				 appinfo=cursor.next();
				 String id=appinfo.get("appId").toString();
				 appid=Integer.parseInt(id);
			}
			// set a new filename for identify purpose
			BasicDBObject document = new BasicDBObject();
			document.put("userId", userId);
			document.put("appId", appid+1);
			document.put("appName", app.getWebName());
			document.put("webUrl", app.getWebUrl());
			document.put("appType", app.getAppType());
			document.put("icon", app.getIcon());
			document.put("defaultIcon", app.getDefaultIcon());
			document.put("version", app.getWebVersion());
			document.put("status", app.getStatus());
			DateFormat simpleDateFormat =
	                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String uploaddate = simpleDateFormat.format(new Date());
			document.put("uploadTime", uploaddate);
			
			// save the data document into mongoDB
			collection.insert(document);
		} catch(Exception e){
			
		} finally {
			if(mongo!=null){
				mongo.close();
			}
		}
		
		
		
	}

	@Override
	public AppPojo storeRobotiumAUTAppInMangoDB(AppPojo app, String userId) throws ParseException {
		// TODO Auto-generated method stub
		MongoClient mongo=null;
		try {
			
			mongo = new MongoClient(host, port);
			DB db = mongo.getDB(dbName);
		
			String newFileName = app.getAppName();
			
			File appFile = new File(app.getDownloadedAppPath());

			// create a "photo" namespace
			GridFS gfsPhoto = new GridFS(db, "autfile");

			// get image file from local drive
			GridFSInputFile gfsFile = gfsPhoto.createFile(appFile);
			
			BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("userId", userId);
			whereQuery.put("appId", app.getId());
			DBCollection collection = db.getCollection("autfile.files");
			
			DBCursor cursor = collection.find(whereQuery);
			DBObject appinfo;
			int autid = 0;
			while(cursor.hasNext()) {
				 appinfo=cursor.next();
				 String id=appinfo.get("autId").toString();
				 autid=Integer.parseInt(id);
				 app.setAutId(autid+1);
			}
			// set a new filename for identify purpose
			gfsFile.setFilename(newFileName);
			gfsFile.setContentType("multipart");
			gfsFile.put("userId", userId);
			gfsFile.put("appId", app.getId());
			gfsFile.put("autId", app.getAutId());
			gfsFile.put("appName", app.getAppName());
			
			gfsFile.put("appType", app.getAppType());
			DateFormat simpleDateFormat =
	                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String uploaddate = simpleDateFormat.format(new Date());
			
		
			gfsFile.put("uploadTime", uploaddate);
			
			// save the image file into mongoDB
			gfsFile.save();

			// print the result
			
			while (cursor.hasNext()) {
				log.info(cursor.next());
			}
			
		
		} catch (UnknownHostException e) {
			log.error(e);
		} catch (MongoException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		} finally {
			if(mongo!=null){
				mongo.close();
			}
		}
		return app;
	}

	@Override
	public JSONObject getAppInfoByNameFromMongoDB(String appName, String userId) throws IOException {
		JSONObject app=null;
		MongoClient mongo=null;
		try {
			
			mongo = new MongoClient(host, port);
			DB db = mongo.getDB(dbName);
		
			DBCollection collection = db.getCollection("appfile.files");
			BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("appName", appName);
			whereQuery.put("userId", userId);
			DBCursor cursor = collection.find(whereQuery);
			DBObject appinfo;
			if(cursor.hasNext()) {
				 appinfo=cursor.next();
				 String resultstr= appinfo.toString().replace("\\\"", "'");
					//log.info(result);
					JSONParser jsonParser = new JSONParser();
					try {
						app = (JSONObject)jsonParser.parse(resultstr);
					} catch (org.json.simple.parser.ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 
				 
				
			}
			
		} catch (MongoException e) {
			log.error(e);
			//log.error(e);
		} finally {
			if(mongo!=null){
				mongo.close();
			}
		}
		return app;
		
	}
	@Override
	public boolean deleteAppFromMangoDB(AppPojo app, String userId) throws ParseException {
		// TODO Auto-generated method stub
		MongoClient mongo=null;
		Boolean isDeleted=false;
		try {
			mongo = new MongoClient(host, port);
			DB db = mongo.getDB(dbName);
			DBCollection collection = db.getCollection("appfile.files");
			BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("appId", app.getId());
			whereQuery.put("userId", userId);
			DBCursor cursor =collection.find(whereQuery);
		
			if(cursor.hasNext()){
				DBObject appObj=cursor.next();
				collection.find(whereQuery);
				collection.remove(appObj);
				isDeleted=true;
			}
			
			
			
		} catch (MongoException e) {
			log.error(e);
			//log.error(e);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(mongo!=null){
				mongo.close();
			}
		}
		
		
		return isDeleted;
	}

	

	
	
	

}
