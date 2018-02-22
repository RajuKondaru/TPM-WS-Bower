package com.tpm.mobile.android.exe.api.appium.utils;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.tpm.mobile.android.exe.api.appium.dao.AppiumResultUtility;
/**
 * @author raju.kondaru
 * @category Thread
* Connects to a device using ddmlib and dumps its event log as long as the device is connected.
*/
public class AppuimScreenshot extends Thread {
	private static final Logger log = Logger.getLogger(AppuimScreenshot.class);
	private  String lines = null;
	private String session;
	private AppiumResultUtility aru=null;
	public String iDevice;
	public Integer imgNo;
	public AppuimScreenshot(String iDevice, int imgNo, String session){
		this.iDevice=iDevice;
		this.imgNo= imgNo;
		this.session= session;
		this.aru=new AppiumResultUtility();
	}
	
	
	/*
	* Grab an image from an ADB-connected device.
	*/
	@SuppressWarnings("deprecation")
	public void getScreenshot()	throws IOException, InterruptedException {
		
		
			/*For Take screenshot of device*/
			String imgName="screen_"+imgNo+"_"+session+".png";
			String screenshotPath="/sdcard/"+imgName;
			String screenshotCmd= "adb -s "+iDevice+" shell screencap -p "+screenshotPath;
			Process screenshotProcess = null;
			BufferedReader screenBuffer = null; 
			try {
				screenshotProcess = new ProcessBuilder("cmd.exe", "/C", screenshotCmd).start();
				//log.info(screenshotCmd);
				screenBuffer = new BufferedReader(new InputStreamReader(screenshotProcess.getInputStream()));
				while((lines=screenBuffer.readLine()) != null ){
					if(!lines.isEmpty()){
						if(lines.contains("100%")){
							//log.info("Screenshot Take Process ::"+lines);
							break;
						}
						
					}
					
				}
			} catch ( IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if(screenshotProcess!=null){
					screenshotProcess.destroy();
				}
				if(screenBuffer!=null){
					screenBuffer.close();
				}
			}
			/*For Taken screenshot of device send to server temp directory*/
			String pullScreenCmd= "adb -s "+iDevice+" pull "+screenshotPath+" "+FileUtils.getUserDirectory()+"\\AppData\\Local\\Temp";
			Process screenshotMoveProcess = null;
			BufferedReader screenMoveBuffer = null; 
			
			try {
				screenshotMoveProcess = new ProcessBuilder("cmd.exe", "/C", pullScreenCmd).start();
				//log.info(pullScreenCmd);
				screenMoveBuffer = new BufferedReader(new InputStreamReader(screenshotMoveProcess.getInputStream()));
				
				while((lines=screenMoveBuffer.readLine()) != null ){
					if(!lines.isEmpty()){
						if(lines.contains("100%")){
							//log.info("Screenshot Move Process :: "+lines);
							
							break;
							
						}
						
					}
					
				}
				
			} catch ( IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if(screenshotMoveProcess!=null){
					screenshotMoveProcess.destroy();
				}
				if(screenMoveBuffer!=null){
					screenMoveBuffer.close();
				}
			}
			/*For Taken screenshot of device is delete from device*/
			String removeScreenCmd= "adb -s "+iDevice+" shell rm -f "+screenshotPath;
			Process screenshotDeleteProcess = null;
			BufferedReader screenDeleteBuffer = null; 
			
			try {
				screenshotDeleteProcess = new ProcessBuilder("cmd.exe", "/C", removeScreenCmd).start();
				//log.info(removeScreenCmd);
				screenDeleteBuffer = new BufferedReader(new InputStreamReader(screenshotDeleteProcess.getInputStream()));
				
				while((lines=screenDeleteBuffer.readLine()) != null ){
					if(!lines.isEmpty()){
						if(lines.contains("100%")){
							//log.info("Screenshot Delete Process ::"+lines);
							
							break;
							
						}
						
					}
					
				}
				
			} catch ( IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if(screenshotDeleteProcess!=null){
					screenshotDeleteProcess.destroy();
				}
				if(screenDeleteBuffer!=null){
					screenDeleteBuffer.close();
				}
				
				aru.saveImage(FileUtils.getUserDirectory()+"\\AppData\\Local\\Temp\\"+imgName, session);
				//stop();
				//resume();
				File temp= new File(FileUtils.getUserDirectory()+"\\AppData\\Local\\Temp\\"+imgName);
				temp.delete();
			}
			
	
	}
	/*public static File createTempFile(String prefix, String suffix){
	    String tempDir = System.getProperty("java.io.tmpdir");
	    String fileName = (prefix != null ? prefix : "" ) + System.nanoTime() + (suffix != null ? suffix : "" ) ;
	    return new File(tempDir, fileName);
	}*/
	
	@Override
	public void run() {
		try {
			getScreenshot();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}