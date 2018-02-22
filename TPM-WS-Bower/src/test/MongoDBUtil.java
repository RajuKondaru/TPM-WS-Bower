package test;
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
	private static List<String> imgPathList = new ArrayList<String>();
	
	
	public static void saveImage(String imagePath) throws IOException {
		Mongo mongo = new Mongo("localhost", 27017);
		db = mongo.getDB("mobile");
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
		mongo.close();
		
	}
	public static void saveVideo(String videoPath) throws IOException {
		Mongo mongo = new Mongo("localhost", 27017);
		db = mongo.getDB("mobile");
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
		mongo.close();
	}
	
}