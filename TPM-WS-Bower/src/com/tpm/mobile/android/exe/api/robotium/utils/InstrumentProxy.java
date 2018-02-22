package com.tpm.mobile.android.exe.api.robotium.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.websocket.Session;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.tpm.mobile.android.utils.CommandPrompt;
import com.tpm.mobile.android.utils.Reports;

public class InstrumentProxy {
	private static final Logger log = Logger.getLogger(InstrumentProxy.class);
	public static boolean isStop= false;
	public static boolean logStop= false;
	public ArrayList<String> deviceLogs=null;
	public static boolean isUnInstall= false;
   
	
	@SuppressWarnings("unchecked")
	public JSONObject start(JSONObject obj, Session peer) throws IOException{
		 String instruments = null;
	        String packageName = null;
	        String deviceid = null ;
	        String testResult = "Passed" ;
	        JSONObject test = null ;
			instruments = (String) obj.get("instruments");
			packageName = (String) obj.get("packageName");
			deviceid = (String) obj.get("deviceId");
			//testName = obj.getString("test");
			test = (JSONObject) obj.get("test");
			
			
			
			final String deviceId=deviceid;
			String[] instrument = null;
		
			if(instruments.contains(",")){
				instrument=instruments.split(",");
			}else {
				instrument = new String[1];
				instrument[0]=instruments;
			}
			JSONObject resultJson = new JSONObject();
			
			//String sessionid="sample";
			
			Reports vedio = null;
			
				vedio = new Reports(deviceid, test.get("name").toString());
				vedio.startRecoding();
			
			deviceLogs = new ArrayList<String>();
			new Thread(new Runnable() {
				
				public void run() {
					synchronized(this) {
						 InputStream is = null;
					        InputStreamReader isr = null;
					        BufferedReader br =null;
					        Process p = null;
						 try {
							 log.info("adb -s "+deviceId+" shell logcat");
						        p = Runtime.getRuntime().exec("adb -s "+deviceId+" shell logcat");
						        is = p.getInputStream();
						        isr = new InputStreamReader(is);
						        br = new BufferedReader(isr);
						        String line;
						        while ((line = br.readLine()) != null) {
						        	if(!line.isEmpty()){
						        		//System.out.println(line);
						                deviceLogs.add(line);
							            if(isStop){
							            	break;
							            }
						        	}
						        }
						        
						    } catch (IOException  e) {
						        log.error(e);
						    } finally {
						    	try {
						    		if (br!=null) {
										br.close();
									}
									if (isr!=null) {
										isr.close();
									}
									if (is!=null){
										is.close();
									}
									if (p!=null){
										p.destroy();
									}
									logStop = true;
						    	} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
					
				      }

				}
			}).start();
			JSONObject logs = new JSONObject();
			Map<String, String> testLogs = new HashMap<String, String>();
			for (int i = 0; i < instrument.length; i++) {
				int testcaseno= i+1;
				//log.info(instrument[i]);
				CommandPrompt cmd=new CommandPrompt();
				String command="adb -s "+deviceid+" shell am instrument -w -e class "+instrument[i]+" com.app.abhibus.test/android.test.InstrumentationTestRunner";
				log.info(command);
				try {
					List<String> result=cmd.runCommand(command);
					peer.getBasicRemote().sendText("{\"testCaseRunStatus\": \""+testResult+"\",\"testcaseno\": \""+testcaseno+"\",\"deviceId\": \""+deviceId+"\"}");
	          		System.out.println("{\"testCaseRunStatus\": \""+testResult+"\",\"testcaseno\": \""+testcaseno+"\",\"deviceId\": \""+deviceId+"\"}");
					for (String string : result) {
						if(string.contains("Failure")){
							testResult="Failed";
						} else if(string.contains("Exception")){
							testResult="Error";
							break;
						} 
						
					}
					
					peer.getBasicRemote().sendText("{\"testCaseStatus\": \""+testResult+"\",\"testcaseno\": \""+testcaseno+"\",\"deviceId\": \""+deviceId+"\"}");
	          		System.out.println("{\"testCaseStatus\": \""+testResult+"\",\"testcaseno\": \""+testcaseno+"\",\"deviceId\": \""+deviceId+"\"}");
				
					
					testLogs.put(instrument[i], result.toString());
					//cmd.runCommand("adb kill-server");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					log.error(e);
				}
			}
			
			
			String lines;
			String DEVICE_VIDEO_PATH = "/sdcard/"+test.get("name").toString()+deviceId+".mp4";//
			String command= "adb -s "+deviceId+" pull "+DEVICE_VIDEO_PATH+" "+FileUtils.getUserDirectory()+"/AppData/Local/Temp";
			try {
				if(vedio!=null){
					//report.stop();
					vedio.stopRecording();
					
				}
				Thread.sleep(5000);
				log.info(command);
				Process p = new ProcessBuilder("cmd.exe", "/C", command).start();
				//log.info(command);
				BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
				
				while((lines=r.readLine()) != null ){
					if(!lines.isEmpty()){
						if(lines.contains("100%")){
							//log.info(lines);
							String dalateCommand= "adb -s "+deviceId+" shell rm -f /sdcard/"+test.get("name").toString()+deviceId+".mp4";
							Process deleteProcess = null;
							BufferedReader deleteBufferedReader=null;
							String deleteResults=null;    
							try {
								log.info(dalateCommand);
								deleteProcess = new ProcessBuilder("cmd.exe", "/C", dalateCommand).start();
								//log.info(command);
								deleteBufferedReader = new BufferedReader(new InputStreamReader(deleteProcess.getInputStream()));
								
								while((deleteResults=deleteBufferedReader.readLine()) != null ){
									if(!deleteResults.isEmpty()){
										if(deleteResults.contains("100%")){
											//log.info(lines);
											
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
				
			isStop= true;
			try {
				log.info("adb -s "+deviceid+" uninstall "+packageName);
		        Process unip = Runtime.getRuntime().exec("adb -s "+deviceid+" uninstall "+packageName);
		        InputStream is = unip.getInputStream();
		        InputStreamReader isr = new InputStreamReader(is);
		        BufferedReader unbr = new BufferedReader(isr);

		        String line;
		        
		        while ((line = unbr.readLine()) != null) {
		           // log.info(line);
		           
		        	if(line.contains("Success")){
		        		isUnInstall= true;
			            	break;
		            }else if(line.contains("Failure")){
		            	isUnInstall= false;
		            	break;
					}
		        }
		        
		    } catch (IOException e) {
		        log.error(e);
		        
		    }
			} catch ( IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				
			}
			
			JSONArray testLog = new JSONArray();
			testLog.add( testLogs);
			
			
			while(!logStop){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			logs.put("devicelog", deviceLogs);
		
			logs.put("testLogs", testLog);
			resultJson.put("logs", logs);
			resultJson.put("deviceid", deviceid);
			resultJson.put("vedioPath", FileUtils.getUserDirectory()+"/AppData/Local/Temp/"+test.get("name").toString()+deviceid+".mp4");
			resultJson.put("test", test);
			resultJson.put("testResult", testResult);
			
			 return resultJson;
				
	}

}
