package com.tpm.mobile.android.utils;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
/**
* Connects to a device using ddmlib and dumps its event log as long as the device is connected.
*/
public class Screenshot extends Thread {
	  private  File tmpDir = null;
	  private  String lines = null;
	  private  Process p;
	  private BufferedReader r; 
	  private String session;
	  
	  
	  public String iDevice;
	  public Integer imgNo;
	  public Screenshot(String iDevice, int imgNo, String session){
		  this.iDevice=iDevice;
		  this.imgNo= imgNo;
		  this.session= session;
		
	  }
	
	
	/*
	* Grab an image from an ADB-connected device.
	*/
	public void getScreenshot()	throws IOException, InterruptedException {
		
		
			/*For Take screenshot of device*/
			String imgName="screen_"+imgNo+"_"+session+".png";
			String screenshotPath="/sdcard/"+imgName;
			String command= "adb -s "+iDevice+" shell screencap -p "+screenshotPath;
			try {
				
				p = new ProcessBuilder("cmd.exe", "/C", command).start();
				System.out.println(command);
				r = new BufferedReader(new InputStreamReader(p.getInputStream()));
				while((lines=r.readLine()) != null ){
					if(!lines.isEmpty()){
						if(lines.contains("100%")){
							//System.out.println(lines);
							break;
						}
						
					}
					
				}
				
				
				//iDevice.pullFile(DEVICE_VIDEO_PATH, local.getAbsolutePath() );
			} catch ( IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*For Taken screenshot of device send to server temp directory*/
			command= "adb -s "+iDevice+" pull "+screenshotPath+" "+System.getProperty("java.io.tmpdir");
			try {
				p = new ProcessBuilder("cmd.exe", "/C", command).start();
				//System.out.println(command);
				r = new BufferedReader(new InputStreamReader(p.getInputStream()));
				
				while((lines=r.readLine()) != null ){
					if(!lines.isEmpty()){
						if(lines.contains("100%")){
							//System.out.println(lines);
							
							break;
							
						}
						
					}
					
				}
				
			} catch ( IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*For Taken screenshot of device is delete from device*/
			command= "adb -s "+iDevice+" shell rm -f "+screenshotPath;
			try {
				p = new ProcessBuilder("cmd.exe", "/C", command).start();
				//System.out.println(command);
				r = new BufferedReader(new InputStreamReader(p.getInputStream()));
				
				while((lines=r.readLine()) != null ){
					if(!lines.isEmpty()){
						if(lines.contains("100%")){
							//System.out.println(lines);
							
							break;
							
						}
						
					}
					
				}
				
			} catch ( IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				
				MongoDBUtil.saveImage(System.getProperty("java.io.tmpdir")+imgName);
				stop();
				resume();
				
			}
			
	
	}
	public static File createTempFile(String prefix, String suffix){
	    String tempDir = System.getProperty("java.io.tmpdir");
	    String fileName = (prefix != null ? prefix : "" ) + System.nanoTime() + (suffix != null ? suffix : "" ) ;
	    return new File(tempDir, fileName);
	}
	
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