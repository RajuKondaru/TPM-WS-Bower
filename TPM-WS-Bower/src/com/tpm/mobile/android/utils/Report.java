package com.tpm.mobile.android.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.CollectingOutputReceiver;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.TimeoutException;

public class Report extends Thread{
	private static final Logger log = Logger.getLogger(Report.class);
	private IDevice iDevice;
	private String vFileName;
	private String deviceId;
	private String cammand;
	private ADB adb;
	private IDevice[] idevices;
	public Report(String deviceId, String vFileName){
		
		this.vFileName= vFileName;
		this.deviceId=deviceId;
		
	}
	private void startRecoding(){
		adb = new ADB();
		if (!adb.initialize(null)) {
			log.info("Could not find adb, please install Android SDK and set path to adb.");
		}
		
		
		
		idevices = adb.getDevices();
		System.out.println(idevices.length);
		for (int k = 0; k < idevices.length; k++) {
				log.info("Device ID :: "+idevices[k].getSerialNumber());
				if( idevices[k].getSerialNumber().equalsIgnoreCase( deviceId)){
					iDevice= idevices[k];
				}
		}
		cammand="screenrecord --size 1280x720 /sdcard/"+vFileName+".mp4";
		
		try {
			if(iDevice!=null){
				CollectingOutputReceiver receiver = new CollectingOutputReceiver();
				iDevice.executeShellCommand(cammand, receiver, 3000*20*3);
				log.info("Video Recoring Started");
			}
			
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			log.error(e);
		} catch (AdbCommandRejectedException e) {
			// TODO Auto-generated catch block
			log.error(e);
		} catch (ShellCommandUnresponsiveException e) {
			// TODO Auto-generated catch block
			log.error(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error(e);
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e);
		}
		
		
			
					
			
		
	
	}
	
	@Override
	public void run() {
		
		startRecoding();
		
		
	}
	@SuppressWarnings("deprecation")
	public String stopRecording(HttpServletRequest request) throws IOException{
	
		String lines=null;
		String DEVICE_VIDEO_PATH = "/sdcard/"+vFileName+".mp4";//
		//E:\Robotium\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\RobotiumWebProxy/Robotium\videos

		//String contextPath =request.getServletContext().getRealPath("");
		
		String videopath = FileUtils.getUserDirectory()+"/AppData/Local/Temp/"+vFileName+".mp4";
		videopath=videopath.replace("/", "\\");
		log.info(videopath);
		String command= "adb -s "+deviceId+" pull "+DEVICE_VIDEO_PATH+" "+videopath;
		try {
			stop();
			Thread.sleep(5000);
			Process p = new ProcessBuilder("cmd.exe", "/C", command).start();
			BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while((lines=r.readLine()) != null ){
				if(!lines.isEmpty()){
					if(lines.contains("100%")){
						String deleteCommand= "adb -s "+deviceId+" shell rm -f /sdcard/"+vFileName+".mp4";
						Process deleteProcess = null;
						BufferedReader deleteBufferedReader=null;
						String deleteResults=null;    
						try {
							deleteProcess = new ProcessBuilder("cmd.exe", "/C", deleteCommand).start();
							deleteBufferedReader = new BufferedReader(new InputStreamReader(deleteProcess.getInputStream()));
							while((deleteResults=deleteBufferedReader.readLine()) != null ){
								if(!deleteResults.isEmpty()){
									if(deleteResults.contains("100%")){
										break;
									}
								}
							}
						} catch ( IOException e) {
							log.info(e);
							
						} finally {
							deleteProcess.destroy();
							deleteBufferedReader.close();
							
						}
						break;
					}
					
				}
				
			}
			p.destroy();
			r.close();
			
		} catch ( IOException | InterruptedException e) {
			
		}finally {
			
			resume();
			//adb.terminate();
		}
		log.info("Video Recoring Stoped"); 
		return videopath;
	 }
	

}
