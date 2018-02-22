package com.tpm.handler.websocket;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.commons.io.FileDeleteStrategy;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.tpm.mobile.android.exe.api.appium.dao.AppiumResultUtility;
import com.tpm.mobile.android.exe.api.appium.utils.AppiumProxy4;
import com.tpm.mobile.android.exe.api.appium.utils.AppuimScreenshot;
import com.tpm.mobile.android.utils.Reports;
@ServerEndpoint(value="/tpwsendpoint2",  encoders = {FigureEncoder.class}, decoders = {FigureDecoder.class})
public class TPWSEndpoint2 {
    private static final Logger LOG = Logger.getLogger(TPWSEndpoint2.class.getName());
    private static Set<Session> peers = Collections.synchronizedSet(new HashSet<Session>());
    
	private  boolean isAlive = true;
	private  boolean isStarted = false;
	private  boolean isError = false;
	private  String platformName;
	private  String platformVersion;
	private  String deviceName;
	private  String key;
	private  String deviceId;
	private  String session;
	private  AppiumResultUtility aru=null;
	private  String DEVICE_VIDEO_PATH;
	private  String command;
	private  String line;
	
	private  Reports reports;

	private  Integer imageNo = 1;
	private  Process pro = null;
	private  BufferedReader br = null;
	
	private  String  nodeConfig = null;
	private  String  appiumConfig = null;	
	private  ArrayList<String>  appiumLogs= null;	

	private ArrayList<String> deviceLogs=null;
	private static boolean isStop= false;
	private static boolean logStop= false;
	private String appiumHost;
	private String appiumPort;
    
    @OnMessage
    public void onMessage(Figure figure, final Session peer) throws IOException, EncodeException, ParseException {
    	LOG.info(figure.toString());
        if (figure != null) {
        	
        	if(figure.getJson().containsKey("appiumTestExecuter")){
        		
        		JsonValue testConfig = figure.getJson().get("appiumTestExecuter");
        		String jsonstr= testConfig.toString();
        		JSONParser parser = new JSONParser();
        		JSONObject jsonObject = (JSONObject) parser.parse(jsonstr);
        		
        		initialize() ;
        		InetAddress inet=Inet4Address.getLocalHost();
        		inet.getHostAddress();
        		//--app D:\\apk\\instagram-9-5-0.apk --webhook localhost:9876 
        		String appiumCommand= nodeConfig+" "+appiumConfig+" --address "+appiumHost+" --port "+appiumPort+"  --webhook localhost:9876 --session-override --platform-name Android --platform-version 5.1.1 --automation-name Appium --log-no-color";
        		try {
        			pro = new ProcessBuilder("cmd.exe", "/C", appiumCommand).start();
        			//log.info(command);
        			br = new BufferedReader(new InputStreamReader(pro.getInputStream()));
        			line = br.readLine();
        			
        			appiumLogs = new ArrayList<String>();
        			LOG.info("Appium logging Started");
        		    while (isAlive) {
        		    	if(line != null){
        		    		if(!line.isEmpty()){
        		    			
        		    			appiumLogs.add(line);
        		    			
        		    			JsonObject event = Json.createObjectBuilder().
					                        add("appiumLog",line).
					                        build();
        		    			synchronized(peer) {
        		    				//peer.getBasicRemote().sendText("{\"appiumLog\": \""+line+"\"}");
        		    				peer.getBasicRemote().sendText(event.toString());
        		    				
        		    			}
        						if(line.contains("Appium REST http interface listener started on")){
        							LOG.info(line);
        							isStarted = true;
        							aru= new AppiumResultUtility();
        							synchronized(peer) {
        								peer.getBasicRemote().sendText("{\"appiumStatus\": \"ready\"}");
        								LOG.info("{'appiumStatus': 'ready'}");
            		    			}
        							
        						} else   if((line.contains("desiredCapabilities")  && (line.contains("<-- GET") || line.contains("--> POST"))) || session == null){ //
        			            	//log.info(line);
        			            	if(session == null && line.contains("desiredCapabilities")){
        			            		 String[] desiredResult = line.split(",");
        				            	 for (int i = 0; i < desiredResult.length; i++) {
        				            		 if(desiredResult[i].contains("platformVersion")){
        				            			 String[] version=desiredResult[i].split(":");
        				            			 if(platformVersion==null){
        				            				 platformVersion = version[1].replace("\"", "");
        				            				 LOG.info("Platform Version :: "+platformVersion);
        				            			 }
        				            			 
        				            		 } else if(desiredResult[i].contains("platformName")){
        				            			 String[] platform=desiredResult[i].split(":");
        				            			 if(platformName==null){
        				            				 platformName = platform[1].replace("\"", "");
        				            				 LOG.info("Platform Name :: "+platformName);
        				            			 }
        				            			
        				            		 } else if(desiredResult[i].contains("deviceName")){
        				            			 String[] device=desiredResult[i].split(":");
        				            			 if(deviceName==null){
        				            				 deviceName =device[1].replace("\"", "");
        				            				 LOG.info("Device Name :: "+deviceName);
        				            			 }
        				            			
        				            		 } else if(desiredResult[i].contains("api_key")){
        				            			 String[] api_key=desiredResult[i].split(":");
        				            			 if(key==null){
        				            				 key= api_key[1].replace("\"", "");
        				            				 LOG.info("API Key :: "+key);
        				            			 }
        				            			
        				            		 } else if(desiredResult[i].contains("udid")){
        				            			 String[] udid = desiredResult[i].split(":");
        				            			 if(deviceId==null){
        				            				 deviceId=udid[1].replace("\"", "");
        				            				 LOG.info("UDID :: "+deviceId);
        				            			 }
        				            			
        				            		 } else if(desiredResult[i].contains("sessionId")){
        				            			 String[] sessionId=desiredResult[i].split(":");
        				            			 if(session==null){
        				            				 session=sessionId[1].replace("\"", "").replace("}", "");
        				            				 LOG.info("Session ID :: "+session);
        				            			 }
        				            			
        				            		 } 
        									
        								}
        				            	
        			            	} else if(session == null && !line.contains("desired") && line.contains("Session created with session id")){
        			            		String[] sessionId=line.split(":");
        		            			session=sessionId[1].replace("\"", "").replace(" ", "");
        		            			LOG.info("Session ID :: "+session);
        		            			
        			            	}
        			            	
        			  				if( isStarted && deviceId != null && jsonObject.get("deviceId").toString().equalsIgnoreCase(deviceId) && session != null){
        			  					synchronized(peer) {
        			  						peer.getBasicRemote().sendText("{\"appiumStatus\": \"start\"}");
        			  						LOG.info("{'appiumStatus': 'start'}");
                		    			}
        			  					try {
        			  						reports = new Reports(deviceId, session);
        				  					reports.startRecoding();
        			  					}catch(Exception e){
        			  						e.printStackTrace();
        			  					}
        			  					
        			  					deviceLogs = new ArrayList<String>();
        			  					new Thread(new Runnable() {
        			  						public void run() {
        			  							synchronized(this){
        			  								InputStream is = null;
        			  						        InputStreamReader isr = null;
        			  						        BufferedReader br =null;
        			  						        Process p = null;
        				  							 try {
        				  								LOG.info("Device logging Started");
        				  							        p = Runtime.getRuntime().exec("adb -s "+deviceId+" shell logcat");
        				  							        is = p.getInputStream();
        				  							        isr = new InputStreamReader(is);
        				  							        br = new BufferedReader(isr);
        				  							        String deviceLogLine = null;
        				  							        while ((deviceLogLine = br.readLine()) != null) {
        				  							        	if(!deviceLogLine.isEmpty()){
        				  							        	    deviceLogs.add(deviceLogLine);
        				  							        	    /*try{
        				  							        	    	
    	     				  							        		JsonObject deviceLogJson = Json.createObjectBuilder().
    	     				  							                        add("deviceLog",deviceLogLine).
    	     				  							                        build();
    	     				  							        		synchronized(peer){
    	     				  							        			peer.getBasicRemote().sendText(deviceLogJson.toString());
    	     				  	                		    			}
    	     				  							        		
        				  							        	    }catch (Exception e) {
																		// TODO: handle exception
        				  							        	    	LOG.info(e.getMessage());
																	}*/
	        				  							        	
        				  								            if(isStop || isError){
        				  								            	LOG.info("Device logging completed");
        				  								            	break;
        				  								            }
        				  							        	}
        				  							        }
        				  							        
        				  							    } catch (IOException  e) {
        				  							    	LOG.info(e.toString());
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
        				  										
        				  							    	} catch (IOException e) {
        				  										// TODO Auto-generated catch block
        				  										e.printStackTrace();
        				  									}
        				  							    	logStop = true;
        				  								}
        			  							}
        			  						}
        			  					}).start();
        	  							isStarted = false;
        			  	  			}
        			            	
        			            	
        			            }else if(!line.contains("desired") && !line.contains("null")  ){
        			            	//log.info(line);
        			            	if(line.contains("Responding to client with success") || (line.contains("<-- POST") && line.contains("element") && line.contains(" 200 ") )){
        			            		AppuimScreenshot screenshot = new AppuimScreenshot(deviceId, imageNo, session);
        			            		screenshot.start();
        			            		LOG.info(line);
        			   					imageNo =1+imageNo;
        			        
        			      		 	} else if (line.contains("Responding to client with error") || (line.contains("<-- POST") && line.contains("element") && line.contains(" 500 ") )) {
        			      		 		AppuimScreenshot screenshot = new AppuimScreenshot(deviceId, imageNo, session);
        			            		screenshot.start();
        			            		LOG.info(line);
        			   				    isError = true;
        			      		 	 	imageNo =1+imageNo;
        							 }else if(line.contains("Creating new appium session")){
        								 LOG.info(line);
        							 }
        			            	
        			            }else if((line.contains("<-- DELETE") && line.contains(" 200 ")) || (line.contains("<-- POST") &&  line.contains(" 500 ")) ||  isError){
        			            	
        			            	String lines;
        							DEVICE_VIDEO_PATH = "/sdcard/"+session+".mp4";//
        							command = "adb -s "+deviceId+" pull "+DEVICE_VIDEO_PATH+" "+FileUtils.getUserDirectory()+"\\AppData\\Local\\Temp";
        							try {
        								try{
        									if(reports!=null){
        										//report.stop();
        										reports.stopRecording();
        										isStop= true;
        									}
        								}catch(Exception e){
        									
        								}
        								
        								Thread.sleep(5000);
        								Process p = null;
        								BufferedReader r= null;
        								try{
        									p = new ProcessBuilder("cmd.exe", "/C", command).start();
        									LOG.info(command);
        									r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        									
        									while((lines=r.readLine()) != null ){
        										if(!lines.isEmpty()){
        											if(lines.contains("100%")){
        												//log.info(lines);
        												String dalateCommand= "adb -s "+deviceId+" shell rm -f /sdcard/"+session+".mp4";
        												Process deleteProcess = null;
        												BufferedReader deleteBufferedReader=null;
        												String deleteResults=null;    
        												try {
        													deleteProcess = new ProcessBuilder("cmd.exe", "/C", dalateCommand).start();
        													LOG.info(dalateCommand);
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
        													
        													if(deleteBufferedReader != null){
        														deleteBufferedReader.close();
        													}
        													if(deleteProcess != null){
        														deleteProcess.destroy();
        													}
        													
        													
        												}
        												break;
        											}
        											
        										}
        										
        									}
        								}catch(Exception e){
        									LOG.info(e.toString());
        								}finally {
        									
        									if(r!=null){
        										r.close();
        									}
        									if(p!=null){
        										p.destroy();
        									}
        									Process killProcess=null;
        									BufferedReader killBuffer=null;
        									String killResults=null;  
        									try{
        										killProcess=new ProcessBuilder("cmd.exe", "/C", "taskkill /F /IM node.exe").start();
        										killBuffer = new BufferedReader(new InputStreamReader(killProcess.getInputStream()));
        										while((killResults=killBuffer.readLine()) != null ){
        											if(!killResults.isEmpty()){
        												if(killResults.contains("SUCCESS")){
        													LOG.info("Appium Server Closed Normally");
        													synchronized(peer){
        														 peer.getBasicRemote().sendText("{\"appiumStatus\": \"stop\"}");
        														 LOG.info("{'appiumStatus': 'stop'}");
				  	                		    			}
				  	            							
        													
        													break;
        												}
        											}
        										}
        									}catch(Exception e){
        										LOG.info(e.toString());
        									}finally {
        										
        										if(killBuffer != null){
        											killBuffer.close();	
        										}
        										if(killProcess != null){
        											killProcess.destroy();
        										}
        										
        									}
        									
        								}
        						
        							} catch ( IOException | InterruptedException e) {
        								// TODO Auto-generated catch block
        								e.printStackTrace();
        							}finally {
        								isAlive = false;
        								LOG.info("Appium logging completed");
        							}
        						
        			            	
        			            }
        						
        						
        						
        					}
        		    	}
        		     	
        		    	
        		     	 line = br.readLine();	
        				
        			}
        			if(!appiumLogs.isEmpty()){
        				JSONObject logs= new JSONObject();
        				while(!logStop){
        					try {
        						Thread.sleep(1000);
        					} catch (InterruptedException e) {
        						// TODO Auto-generated catch block
        						e.printStackTrace();
        					}
        				}
        				logs.put("devicelog", deviceLogs);
        				logs.put("appiumLog", appiumLogs);
        				JSONObject app = (JSONObject) jsonObject.get("app");
        				String testName =jsonObject.get("testName").toString();
        				app.put("logs", logs);
        				if(reports!=null){
        					aru.saveVideo(FileUtils.getUserDirectory()+"\\AppData\\Local\\Temp\\"+session+".mp4",deviceId ,app, session, isError, testName);
        					//report.resume();
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
        				File fin = new File(FileUtils.getUserDirectory()+"\\AppData\\Local\\Temp");

        				for (File file : fin.listFiles()) {
        					try{
        						FileDeleteStrategy.FORCE.delete(file);
        					} catch(Exception e){
        						
        					}
        				    
        				}
        				synchronized(peer){
        					peer.getAsyncRemote().sendText("{\"appiumStatus\": \"complete\"}");
        					 LOG.info("{'appiumStatus': 'complete'}");
  		    			}
        				
							
           			
        				//
        			} catch (IOException e) {
        				// TODO Auto-generated catch block
        				e.printStackTrace();
        			}
        		}
            } else if (figure.getJson().containsKey("status")) {
            	String status=	figure.getJson().get("status").toString();
            
            	
            	if(status == "false" || status.contains("false")){
            		 LOG.info(status);
            		if(peers.contains(peer)){
            			synchronized(peer){
            				 peer.getBasicRemote().sendText("{\"connect\": \"true\"}");
 		    			
 		    			}
						LOG.info("send to client {'connect': 'true'}");
            		} 
            		
            		
            	}
            	
			}
        }
      
    }
    @OnOpen
    public void onOpen(Session peer) throws IOException, EncodeException {
        LOG.info("Connection opened ...");
        peers.add(peer);
    }
    @OnClose
    public void onClose(Session peer) {
        LOG.info("Connection closed ...");
        
        peers.remove(peer);
    }
    private void initialize() {
		Properties prop = new Properties();
		try {
			prop.load(AppiumProxy4.class.getClassLoader().getResourceAsStream("config.properties"));
				
			nodeConfig = prop.getProperty("NODE_CONFIG");
			appiumConfig = prop.getProperty("APPIUM_CONFIG");
			appiumHost = prop.getProperty("APPIUM_HOST");
			appiumPort = prop.getProperty("APPIUM_PORT") ;
			
		} catch (IOException e) {
			

		}
	}
}