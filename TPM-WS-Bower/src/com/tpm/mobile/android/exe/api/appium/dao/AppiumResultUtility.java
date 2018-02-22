package com.tpm.mobile.android.exe.api.appium.dao;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.json.JsonObject;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;
import com.tpm.mobile.log.LogServiceClient;

public class AppiumResultUtility {
	private static final Logger log = Logger.getLogger(AppiumResultUtility.class);
		private List<String> imgPathList = new ArrayList<String>();
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
		
		public  void saveImage(String imagePath, String session) throws IOException {
			MongoClient mongo=null;
			try {
				
				mongo = new MongoClient(host, port);
				DB db = mongo.getDB(dbName);
				imagePath=imagePath.replace("\\", "/");
				//System.out.println(imagePath);
				File  imageFile = new File(imagePath);
				// create a "photo" namespace
				GridFS gfsImage = new GridFS(db, "screenshots");

				// get image file from local drive
				GridFSInputFile gfsFile = gfsImage.createFile(imageFile);

				// set a new filename for identify purpose
				gfsFile.setFilename(imageFile.getName());
				gfsFile.setContentType("image");
				gfsFile.put("sessionId", session);
				// save the image file into mongoDB
				gfsFile.save();
				imgPathList.add(imagePath);
				//log.info(imagePath+" saved");
			} catch(Exception e){
				log.error(e);
			} finally {
				if(mongo!=null){
					mongo.close();
				}
			}
			
			
			
		}
		public void saveVideo(String videoPath,String deviceId, JSONObject app, String session,Boolean isError, String testName) throws IOException {
			MongoClient mongo=null;
			try {
				
				mongo = new MongoClient(host, port);
				DB db = mongo.getDB(dbName);
			
				videoPath=videoPath.replace("\\", "/");
				File videoFile = new File(videoPath);
	
				// create a "video" namespace
				GridFS gfsVideo = new GridFS(db, "results");
	
				// get video file from local drive
				GridFSInputFile gfsFile = gfsVideo.createFile(videoFile);
	
				// set a new filename for identify purpose
				gfsFile.setFilename(videoFile.getName());
				gfsFile.setContentType("video");
				gfsFile.put("userId", app.get("userId"));
				gfsFile.put("appId", app.get("id").toString());
				gfsFile.put("testName", testName);
				gfsFile.put("deviceId",  deviceId);
				gfsFile.put("testType", "Appium");
				gfsFile.put("sessionId", session);
				gfsFile.put("testSenario", "Automation");
				if(isError){
					gfsFile.put("testResult", "Failed");
				} else {
					gfsFile.put("testResult", "Passed");
				}
				gfsFile.put("logs", app.get("logs"));
				DateFormat simpleDateFormat =
		                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String uploaddate = simpleDateFormat.format(new Date());
				gfsFile.put("excTime", uploaddate);
				
				// save the video file into mongoDB
				gfsFile.save();
			} catch(Exception e){
				log.error(e);
			} finally {
				if(mongo!=null){
					mongo.close();
				}
			}
		} 
		
	

}
