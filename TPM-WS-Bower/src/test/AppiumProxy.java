package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Properties;

import com.itextpdf.text.log.SysoLogger;
import com.tpm.mobile.android.pojo.AppPojo;
import com.tpm.mobile.android.utils.Report;

import org.apache.commons.io.FileDeleteStrategy;

public class AppiumProxy {
	//private  ADB adb;
	//private  IDevice[] idevices;
	private static boolean isAlive = true;
	private static String platformName;
	private static String platformVersion;
	private static String deviceName;
	private static String key;
	private static String deviceId;
	private static String session;
	private static AppiumResultUtility aru=null;
	public static String DEVICE_VIDEO_PATH;
	public static String command;
	public static String line;
	public static boolean complete= false;
	public static Report report;
	public static AppuimScreenshot screenshot;
	public static Integer imageNo = 1;
	private static AppPojo app=null;
	private static Process pro = null;
	private static BufferedReader br = null;
	
	private static String  nodeConfig = null;
	private static String  appiumConfig = null;	
	private static ArrayList<String>  appiumLogs= null;	
	 
	//public void startProxy() throws UnknownHostException {
	public static void main(String[] args) throws UnknownHostException {
		// TODO Auto-generated method stub
		initialize() ;
		InetAddress inet=Inet4Address.getLocalHost();
		String ip=inet.getHostAddress();
		//--app D:\\apk\\instagram-9-5-0.apk --webhook localhost:9876 
		String command= nodeConfig+" "+appiumConfig+" --address appium.testpace.com --port 2467  --webhook localhost:9876 --session-override --platform-name Android --platform-version 23 --automation-name Appium --log-no-color";
		try {
			pro = new ProcessBuilder("cmd.exe", "/C", command).start();
			//System.out.println(command);
			br = new BufferedReader(new InputStreamReader(pro.getInputStream()));
			line = br.readLine();
			
			appiumLogs = new ArrayList<String>();
		    while (isAlive) {
		    	if(line != null){
		    		if(!line.isEmpty()){
						//System.out.println(line);
						appiumLogs.add(line);
						if(line.contains("Appium REST http interface listener started on appium.testpace.com:2467")){
							System.out.println("Appium REST http interface listener started on appium.testpace.com:2467");
							aru= new AppiumResultUtility();
						}
						
			            if(!line.contains("desired") && !line.contains("null") ){
			            	//System.out.println(line);
			            	if(line.contains("Responding to client with success")  ){
			            		try {
									Thread.sleep(2000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
			            		AppuimScreenshot screenshot = new AppuimScreenshot(deviceId, imageNo, session);
			            		screenshot.start();
			            		System.out.println(line);
			   					imageNo =1+imageNo;
			            			
			            		
			   		
			      		 	} else if (line.contains("Responding to client with error")) {
			       			  //System.out.println(line);
							 }else if(line.contains("Creating new appium session")){
								// System.out.println(line);
							 }
			            	
			            }else  if(line.contains("desired")  && line.contains("<-- GET")){ //
			            	System.out.println(line);
			            	 String[] desiredResult = line.split(",");
			            	 for (int i = 0; i < desiredResult.length; i++) {
			            		 if(desiredResult[i].contains("platformVersion")){
			            			 String[] version=desiredResult[i].split(":");
			            			 if(platformVersion==null){
			            				 platformVersion = version[1].replace("\"", "");
				            			 //System.out.println("Platform Version :: "+platformVersion);
			            			 }
			            			 
			            		 } else if(desiredResult[i].contains("platformName")){
			            			 String[] platform=desiredResult[i].split(":");
			            			 if(platformName==null){
			            				 platformName = platform[1].replace("\"", "");
				            			 //System.out.println("Platform Name :: "+platformName);
			            			 }
			            			
			            		 } else if(desiredResult[i].contains("deviceName")){
			            			 String[] device=desiredResult[i].split(":");
			            			 if(deviceName==null){
			            				 deviceName =device[1].replace("\"", "");
				            			 //System.out.println("Device Name :: "+deviceName);
			            			 }
			            			
			            		 } else if(desiredResult[i].contains("api_key")){
			            			 String[] api_key=desiredResult[i].split(":");
			            			 if(key==null){
			            				 key= api_key[1].replace("\"", "");
				            			 System.out.println("API Key :: "+key);
			            			 }
			            			
			            		 } else if(desiredResult[i].contains("udid")){
			            			 String[] udid = desiredResult[i].split(":");
			            			 if(deviceId==null){
			            				 deviceId=udid[1].replace("\"", "");
				            			 System.out.println("UDID :: "+deviceId);
			            			 }
			            			
			            		 } else if(desiredResult[i].contains("sessionId")){
			            			 String[] sessionId=desiredResult[i].split(":");
			            			 if(session==null){
			            				 session=sessionId[1].replace("\"", "").replace("}", "");
				            			 System.out.println("Session ID :: "+session);
			            			 }
			            			
			            		 } 
								
							}
			            	
			  				if(deviceId !=null && session !=null){
			  					report = new Report(deviceId, session);
			  					report.setName(deviceId);
	  							report.start();
			  	  			}
			            	
			            	
			            }else  if(line.contains("<-- DELETE") ){
			            	
			            	String lines;
							DEVICE_VIDEO_PATH = "/sdcard/"+session+".mp4";//
							command= "adb -s "+deviceId+" pull "+DEVICE_VIDEO_PATH+" "+System.getProperty("java.io.tmpdir");
							try {
								if(report!=null){
									report.stop();
								}
								Thread.sleep(5000);
								Process p = new ProcessBuilder("cmd.exe", "/C", command).start();
								//System.out.println(command);
								BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
								
								while((lines=r.readLine()) != null ){
									if(!lines.isEmpty()){
										if(lines.contains("100%")){
											//System.out.println(lines);
											String dalateCommand= "adb -s "+deviceId+" shell rm -f /sdcard/"+session+".mp4";
											Process deleteProcess = null;
											BufferedReader deleteBufferedReader=null;
											String deleteResults=null;    
											try {
												deleteProcess = new ProcessBuilder("cmd.exe", "/C", dalateCommand).start();
												//System.out.println(command);
												deleteBufferedReader = new BufferedReader(new InputStreamReader(deleteProcess.getInputStream()));
												
												while((deleteResults=deleteBufferedReader.readLine()) != null ){
													if(!deleteResults.isEmpty()){
														if(deleteResults.contains("100%")){
															//System.out.println(lines);
															
															break;
															
														}
														
													}
													
												}
												
											} catch ( IOException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
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
								
								
								
								Process killProcess=new ProcessBuilder("cmd.exe", "/C", "taskkill /F /IM node.exe").start();
								BufferedReader killBuffer = new BufferedReader(new InputStreamReader(killProcess.getInputStream()));
								String killResults=null;    
								while((killResults=killBuffer.readLine()) != null ){
									if(!killResults.isEmpty()){
										if(killResults.contains("SUCCESS")){
											System.out.println("Appium Server Closed Normally");
											break;
										}
									}
								}
								killProcess.destroy();
								killBuffer.close();
							} catch ( IOException | InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}finally {
								isAlive = false;
								
								if(report!=null){
									aru.saveVideo(System.getProperty("java.io.tmpdir")+session+".mp4", app);
									report.resume();
								}
								
							}
						
			            	
			            } 
						
					}
		    	}
		     	
		    	
		     	 line = br.readLine();	
				
			}
			int step=1; 
			if(!appiumLogs.isEmpty()){
				
				for (int i=0; i<appiumLogs.size();i++) {
					if(appiumLogs.get(i).contains("Device launched! Ready for commands")){
						System.out.println("Step: "+step+":: App launched");
						++step;
					}else if(appiumLogs.get(i).contains("Pushing command to appium work queue") && !appiumLogs.get(i).contains("wake") && !appiumLogs.get(i).contains("getDataDir") && !appiumLogs.get(i).contains("compressedLayoutHierarchy")){
						int j=1;
						ArrayList<String> stepLogs= new ArrayList<String>();
						while(!appiumLogs.get(i+j).contains("Responding to client with success") || !appiumLogs.get(i+j).contains("Responding to client with success")){
							//System.out.println(appiumLogs.get(i+j));
							stepLogs.add(appiumLogs.get(i+j));
							++j;
						}
						System.out.println( );
						for (int a=0; a<stepLogs.size();a++) {
							
							String action=stepLogs.get(a).replace("info: [debug] [BOOTSTRAP] [debug] ", "");
						
								if(action!=null){
									System.out.println("Step: "+step+":: "  +action);
									
								}
						}
						++step;
					}
					
				}
				
			}
		} catch ( IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(pro!=null){
					pro.destroy();
				}
				if(br!=null){
					br.close();
				}
				File fin = new File(System.getProperty("java.io.tmpdir"));

				for (File file : fin.listFiles()) {
					try{
						FileDeleteStrategy.FORCE.delete(file);
					} catch(Exception e){
						
					}
				    
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	
	private static  void initialize() {
		Properties propbrowser = new Properties();
		try {
			propbrowser.load(AppiumProxy.class.getClassLoader().getResourceAsStream("config.properties"));
				
			nodeConfig = propbrowser.getProperty("nodeConfig");
			appiumConfig = propbrowser.getProperty("appiumConfig");
			
		} catch (IOException e) {
			

		}
	}
	
//
}
