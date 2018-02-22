package test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;
import com.tpm.mobile.android.pojo.AppPojo;

public class AppiumResultUtility {
	
		private List<String> imgPathList = new ArrayList<String>();
		public  void saveImage(String imagePath) throws IOException {
			Mongo mongo = new Mongo("localhost", 27017);
			DB db = mongo.getDB("mobile");
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
		public void saveVideo(String videoPath, AppPojo app) throws IOException {
			Mongo mongo = new Mongo("localhost", 27017);
			DB db = mongo.getDB("mobile");
			
			videoPath=videoPath.replace("\\", "/");
			File videoFile = new File(videoPath);

			// create a "video" namespace
			GridFS gfsVideo = new GridFS(db, "executionVideo");

			// get video file from local drive
			GridFSInputFile gfsFile = gfsVideo.createFile(videoFile);

			// set a new filename for identify purpose
			gfsFile.setFilename(videoFile.getName());
			gfsFile.setContentType("video");
			gfsFile.setContentType("video");
			gfsFile.setContentType("video");
			// save the video file into mongoDB
			gfsFile.save();
			mongo.close();
		}
		
	

}
