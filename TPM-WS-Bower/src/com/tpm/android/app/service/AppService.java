package com.tpm.android.app.service;


import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import com.tpm.mobile.android.pojo.AppPojo;
import com.tpm.mobile.android.utils.CommandPrompt;


public class AppService implements IAppService {
	private static final Logger log = Logger.getLogger(AppService.class);
	private CommandPrompt cmd ;
	private String appPackage;
	private String buildToolPath;
	private String host;
	private String port;
	private String mongodbName;
	private DB db;
	private String bucketName;
	private String packageName = null;
	private String activityName = null;
	private String versionName = null;
	private String iconPath = null;
	private Mongo mongo = null;
	public AppService() throws IOException{
		Properties prop = new Properties();
		prop.load(AppService.class.getClassLoader().getResourceAsStream("config.properties"));

		buildToolPath= prop.getProperty("buildToolPath");
		host= prop.getProperty("host");
		port= prop.getProperty("port");
		mongodbName= prop.getProperty("mongodbName");
		bucketName= prop.getProperty("bucketName");
		
	}
		
	@Override
	public boolean appInstallService(String deviceId, AppPojo app) {
		
		boolean status=false;
		try {
			cmd = new CommandPrompt();
			
			String apkFile= app.getDownloadedAppPath();
			String apkName = app.getFileName();
			
			log.info("App Name >> " + apkName);
			//log.info("app Package >> " + appPackage);
			String installCommand = "adb -s "+deviceId+" install "+apkFile; // 0123456789ABCD // // adb -s 0123456789ABCD install D:/RedBus.apk
			
			
			log.info("Command >> " + installCommand);
			
			cmd.runCommand(installCommand);
			
			status = true;
		} catch (InterruptedException e) {
			
			
			log.warn(e);
		} catch (IOException e) {
			
			
			log.warn(e);
		}
		catch (Exception e) {
			
			
			log.warn(e);
		}
		return status;
	}

	@Override
	public boolean appUnInstallService(String deviceId, AppPojo app) {
		
		boolean status=false;
		String appPackage = app.getAppPackageName();
		cmd = new CommandPrompt();
		try {
			String clearCommand = "adb shell pm clear "+appPackage; // 0123456789ABCD // // adb shell pm clear in.redbus.android
		
			cmd.runCommand(clearCommand);
		
			String uninstallCommand = "adb -s "+deviceId+" uninstall "+appPackage; // 0123456789ABCD // // adb -s TA93300GXV uninstall in.redbus.android
		
			cmd.runCommand(uninstallCommand);
			status = true;
		} catch (InterruptedException e) {
			
		
			log.warn(e);
		} catch (IOException e) {
			
			
			log.warn(e);
		}catch (Exception e) {
			
			
			log.warn(e);
		}
		return status;
	}

	@Override
	public AppPojo getAppInfoService( AppPojo app) throws IOException {
		
		CommandPrompt cmd = new CommandPrompt();
		
		List<String> resultList = null;
		try {
			resultList = cmd.runCommand(buildToolPath+"/aapt dump badging "+app.getDownloadedAppPath());
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		
		for (String result : resultList) {
			//log.info(result);
			int ind = result.indexOf("package: name=");
            if (ind >= 0) {
            	log.debug(result);
            	String[] temp=result.split("=");
            	String[] temp2=temp[1].split(" ");
            	packageName = temp2[0];
            	packageName=packageName.replace("'","");
            	
               
            }
            int ind1 = result.indexOf("versionName=");
            if (ind1 >= 0) {
            	log.debug(result);
            	String[] temp=result.split("=");
            	String[] temp2=temp[3].split(" ");
            	versionName = temp2[0];
            	versionName=versionName.replace("'","");
            	
               
            }
           
            int ind2 = result.indexOf("launchable-activity: name=");
            if (ind2 >= 0) {
            	log.debug(result);
            	String[] temp=result.split("=");
            	String[] temp2=temp[1].split(" ");
            	activityName = temp2[0];
            	activityName=activityName.replace("'","");
            	
               
            }
            int ind3 = result.indexOf("icon=");
            if (ind3 >= 0) {
            	log.debug(result);
            	String[] temp=result.split(" ");
            	try{
            		String[] temp2=temp[2].split("=");
	            	iconPath = temp2[1];
	            	iconPath=iconPath.replace("'","");
	            	
            	}catch(ArrayIndexOutOfBoundsException e ){
            		log.warn(e);
            	}
            	
               
            }
	            
		}
		app.setVersion(versionName);
		app.setAppPackageName(packageName);
		app.setActivityName(activityName);
		app.setIconPath(iconPath);
		log.info("Package Name	>>>>>>>>"+packageName);
		log.info("Activity Name	>>>>>>>>"+activityName);
		log.info("Icon Path	>>>>>>>>"+iconPath);
		log.info("App version	>>>>>>>>"+versionName);
		return app;
	}

	
	@Override
	public void deleteAppFromTemp(AppPojo app) {
		
		File file = new File(app.getDownloadedAppPath());

		if(file.delete()){
			log.info(file.getName() + " is deleted!");
		}else{
			log.warn("Delete operation is failed.");
		}
	}

	@Override
	public boolean appLaunchService(String deviceId,  AppPojo app)  {
		boolean status=false;
		try {
			appPackage = app.getAppPackageName();
			String launchCommand = "adb -s "+deviceId+" shell monkey -p "+appPackage+" -c android.intent.category.LAUNCHER 1";
			// adb shell monkey -p com.instagram.android -c android.intent.category.LAUNCHER 1
			// for multiple devices
			// adb -s 0123456789ABCD shell monkey -p in.redbus.android -c android.intent.category.LAUNCHER 1
			log.info("Command >> " + launchCommand);
			cmd.runCommand(launchCommand);
			status= true;
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		return status;
	}
	
	
	
	@Override
	public boolean getAppFromMongoDB(AppPojo app) throws NumberFormatException, MongoException, IOException {
		
		boolean isApp = false;
		try{
			mongo = new Mongo(host,Integer.parseInt(port));
			db = mongo.getDB(mongodbName);
			log.info("Mongo DB Connection Established");
			//Create instance of GridFS implementation
			GridFS gridFs = new GridFS(db, bucketName);
			//Find the image with the name image1 using GridFS API
			GridFSDBFile outputApkFile = gridFs.findOne(app.getFileName()+".apk");
			
			if(outputApkFile!=null){
				log.info("Total Chunks: " + outputApkFile);
				//Get the number of chunks
			    String apkLocation = System.getProperty("java.io.tmpdir")+File.separator+app.getFileName()+".apk";
			    app.setDownloadedAppPath(apkLocation);
			    File apkFile= new File(apkLocation);
			    outputApkFile.writeTo(apkFile);
			    isApp = true;
			} else {
				isApp = false;
			}
		}catch(Exception e){
			log.error(e);
		}finally {
			mongo.close();
		}
		
		return isApp;
	}

	@Override
	public void saveAppInMongoDB(String filename)throws NumberFormatException, MongoException, IOException  {
		try{
			mongo= new Mongo(host,Integer.parseInt(port));
			db = mongo.getDB(mongodbName);
			log.info("Mongo DB Connection Established");
			
			String newFileName = "AZ Screen Recorder";
			File imageFile = new File("D:\\apk\\AZ Screen Recorder.apk");
			GridFS gfs = new GridFS(db, bucketName);
			GridFSInputFile gfsFile = gfs.createFile(imageFile);
			gfsFile.setFilename(newFileName);
			gfsFile.save();
		}catch(Exception e){
			log.error(e);
		}finally {
			mongo.close();
		}
		
		
	}

}
