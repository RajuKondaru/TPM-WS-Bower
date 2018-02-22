package test;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
/**
* Connects to a device using ddmlib and dumps its event log as long as the device is connected.
*/
public class AppuimScreenshot extends Thread {
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
	public void getScreenshot()	throws IOException, InterruptedException {
		
		
			/*For Take screenshot of device*/
			String imgName="screen_"+imgNo+"_"+session+".png";
			String screenshotPath="/sdcard/"+imgName;
			String command= "adb -s "+iDevice+" shell screencap -p "+screenshotPath;
			Process screenshotProcess = null;
			BufferedReader screenBuffer = null; 
			try {
				
				screenshotProcess = new ProcessBuilder("cmd.exe", "/C", command).start();
				System.out.println(command);
				screenBuffer = new BufferedReader(new InputStreamReader(screenshotProcess.getInputStream()));
				Thread.sleep(2000);
				while((lines=screenBuffer.readLine()) != null ){
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
				if(screenshotProcess!=null){
					screenshotProcess.destroy();
				}
				if(screenBuffer!=null){
					screenBuffer.close();
				}
			}
			/*For Taken screenshot of device send to server temp directory*/
			command= "adb -s "+iDevice+" pull "+screenshotPath+" "+System.getProperty("java.io.tmpdir");
			Process screenshotMoveProcess = null;
			BufferedReader screenMoveBuffer = null; 
			
			try {
				screenshotMoveProcess = new ProcessBuilder("cmd.exe", "/C", command).start();
				//System.out.println(command);
				screenMoveBuffer = new BufferedReader(new InputStreamReader(screenshotMoveProcess.getInputStream()));
				
				while((lines=screenMoveBuffer.readLine()) != null ){
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
				if(screenshotMoveProcess!=null){
					screenshotMoveProcess.destroy();
				}
				if(screenMoveBuffer!=null){
					screenMoveBuffer.close();
				}
			}
			/*For Taken screenshot of device is delete from device*/
			command= "adb -s "+iDevice+" shell rm -f "+screenshotPath;
			Process screenshotDeleteProcess = null;
			BufferedReader screenDeleteBuffer = null; 
			
			try {
				screenshotDeleteProcess = new ProcessBuilder("cmd.exe", "/C", command).start();
				//System.out.println(command);
				screenDeleteBuffer = new BufferedReader(new InputStreamReader(screenshotDeleteProcess.getInputStream()));
				
				while((lines=screenDeleteBuffer.readLine()) != null ){
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
				if(screenshotDeleteProcess!=null){
					screenshotDeleteProcess.destroy();
				}
				if(screenDeleteBuffer!=null){
					screenDeleteBuffer.close();
				}
				
				aru.saveImage(System.getProperty("java.io.tmpdir")+imgName);
				stop();
				resume();
				
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