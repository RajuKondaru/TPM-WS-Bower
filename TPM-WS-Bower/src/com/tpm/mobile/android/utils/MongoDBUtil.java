package com.tpm.mobile.android.utils;
import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileDeleteStrategy;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;

/**
 * Java MongoDB : Save image example
 *
 */


public class MongoDBUtil {
	private static DB db;
	private static  Mongo mongo;
	private static List<String> imgPathList; 
	static{
		
		try {
			mongo = new Mongo("localhost", 27017);
			
			db = mongo.getDB("mobile");
			imgPathList = new ArrayList<String>();
			System.out.println("Connection Established..");
		} catch (UnknownHostException | MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	}
	public static void main(String[] args) {

		try {
			/*MongoClient mongo1 = new MongoClient(new MongoClientURI("mongodb://predev.clictest.com:27017"));
			//MongoClient mongo1 = new MongoClient("predev.clictest.com", Integer.parseInt("27017"));
			
			MongoDatabase database = mongo1.getDatabase("Test");
			  MongoCollection collections= database.getCollection("people");
			  //create index on name field 
			                //use 1 for ascending index , -1 for descending index 
			  BasicDBObject index = new BasicDBObject("name",1);
			  
			  collections.createIndex(index);
			  System.out.println(" index created successfully ");
			  ListIndexesIterable indexes = collections.listIndexes(BasicDBObject.class);
			  MongoCursor cursor = indexes.iterator();
			  while(cursor.hasNext())
			  {
			   BasicDBObject getCreatedIdx = cursor.next();
			   System.out.println("index is : "+ getCreatedIdx.toString());
			  }
			  
			  
			  
			  mongo1.close();*/
			
			
			DBCollection collection = db.getCollection("image");

			String newFileName = "mkyong-java-image";

			File imageFile = new File("D:\\screen1002.png");

			// create a "photo" namespace
			GridFS gfsPhoto = new GridFS(db, "screenshots");

			// get image file from local drive
			GridFSInputFile gfsFile = gfsPhoto.createFile(imageFile);

			// set a new filename for identify purpose
			gfsFile.setFilename(newFileName);
			gfsFile.setContentType("image");
			// save the image file into mongoDB
			gfsFile.save();

			/*// print the result
			DBCursor cursor = gfsPhoto.getFileList();
			while (cursor.hasNext()) {
				System.out.println(cursor.next());
			}

			// get image file by it's filename
			GridFSDBFile imageForOutput = gfsPhoto.findOne(newFileName);

			// save it into a new image file
			imageForOutput.writeTo("D:\\JavaWebHostingNew.png");

			// remove the image file from mongoDB
			//gfsPhoto.remove(gfsPhoto.findOne(newFileName));
*/
			System.out.println("Done");

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public static void saveImage(String imagePath) throws IOException {
		DBCollection collection = db.getCollection("image");
		
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
		// save the image file into mongoDB
		gfsFile.save();
		imgPathList.add(imagePath);
		
	}
	public static void saveVideo(String videoPath) throws IOException {
		DBCollection collection = db.getCollection("video");
		videoPath=videoPath.replace("\\", "/");
		File videoFile = new File(videoPath);

		// create a "video" namespace
		GridFS gfsVideo = new GridFS(db, "executionVideo");

		// get video file from local drive
		GridFSInputFile gfsFile = gfsVideo.createFile(videoFile);

		// set a new filename for identify purpose
		gfsFile.setFilename(videoFile.getName());
		gfsFile.setContentType("video");
		// save the video file into mongoDB
		gfsFile.save();
	}
	public static void close() throws IOException{
		mongo.close();
		
		System.out.println("Connection Closed");
	}
}