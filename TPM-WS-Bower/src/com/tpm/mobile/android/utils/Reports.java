package com.tpm.mobile.android.utils;

import java.io.IOException;
import org.apache.log4j.Logger;

public class Reports{
	private static final Logger log = Logger.getLogger(Reports.class);
	private String vFileName;
	private String deviceId;
	private Process vedioRecordProcess;
	public Reports(String deviceId, String vFileName){
		
		this.vFileName= vFileName;
		this.deviceId=deviceId;
		
	}
	public void startRecoding(){
		
		try {
			String filelocation="/sdcard/"+vFileName+deviceId+".mp4";
			String vedioRecord = "adb -s "+deviceId+" shell screenrecord "+filelocation;
			log.info("Test Video Recoring Starting.. :: "+ vedioRecord);
			vedioRecordProcess = Runtime.getRuntime().exec(vedioRecord);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error(e);
		}catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e);
		}
	
	}
	
	
	
	public void stopRecording() {
		vedioRecordProcess.destroy();
		
		log.info("Video Recoring Stoped"); 
		
	 }
	

}
