package com.tpm.handler.websocket;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.commons.io.FileDeleteStrategy;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.tpm.android.app.service.VMLiteVNCServerService;
import com.tpm.mobile.android.app.service.AppManageDao;
import com.tpm.mobile.android.app.service.AppManageService;
import com.tpm.mobile.android.dao.DeviceDAO;
import com.tpm.mobile.android.dao.IDevice;
import com.tpm.mobile.android.exe.api.appium.dao.AppiumResultUtility;
import com.tpm.mobile.android.exe.api.appium.utils.AppiumProxy4;
import com.tpm.mobile.android.exe.api.appium.utils.AppuimScreenshot;
import com.tpm.mobile.android.exe.api.robotium.dao.RoboTestDao;
import com.tpm.mobile.android.exe.api.robotium.utils.InstrumentProxy;
import com.tpm.mobile.android.pojo.AppPojo;
import com.tpm.mobile.android.utils.Reports;
@ServerEndpoint(value="/deviceendpoint",  encoders = {FigureEncoder2.class}, decoders = {FigureDecoder2.class})
public class DeviceEndpoint {
    private static final Logger LOG = Logger.getLogger(DeviceEndpoint.class.getName());
    private static Set<Session> peers = Collections.synchronizedSet(new HashSet<Session>());
    
	private  boolean isAlive = true;
	private  boolean isStarted = false;
	private  boolean isError = false;
	private  String session;
	private  AppiumResultUtility aru=null;
	private  String DEVICE_VIDEO_PATH;
	private  String command;
	private  String line;
	
	private  Reports reports;
	private  Integer imageNo = 1;
	private  String  nodeConfig = null;
	private  String  appiumConfig = null;	
	private  Map<String, ArrayList<String>>  appiumLogMap = new HashMap<String, ArrayList<String>>();
	private  ArrayList<Map>  sessionList = new ArrayList<Map>();
	private  Map<String, String>  sessionMap = null;	

	private  Map<String, ArrayList<String>>  deviceLogMap = new HashMap<String, ArrayList<String>>();	
	private static boolean isStop= false;
	private static boolean logStop= false;
	private String appiumHost;
	@OnMessage
    public void onMessage(Figure2 figure, final Session peer) throws IOException, EncodeException, ParseException {
    	//LOG.info(figure.toString());
    	 if (figure != null) {
        	if(figure.getJson().containsKey("VNCConnection")){
        		JSONObject jsonObject = (JSONObject) figure.getJson().get("VNCConnection");
        		String deviceId = null;
                String orientation = null;
                int status = 0;
                deviceId = (String) jsonObject.get("deviceId");
        	    orientation = (String) jsonObject.get("orientation");
        	    IDevice dService= new DeviceDAO();
        		status=dService.checkDeviceConnectionStatus(deviceId);
    			if(status==0){
    				VMLiteVNCServerService vncService= new VMLiteVNCServerService();
    				Object[] isStart = null;
    				try {
    					isStart = vncService.startVNCServer(deviceId, orientation);
    				} catch (InterruptedException e1) {
    					// TODO Auto-generated catch block
    					e1.printStackTrace();
    				}
    				if((Boolean) isStart[0]){
    					dService.updateDeviceConnectionStatus(deviceId, 1);
    					peer.getAsyncRemote().sendText("{\"openStatus\": \"Success\",\"deviceId\": \""+deviceId+"\"}");
    					System.out.println("{\"openStatus\": \"Success\",\"deviceId\": \""+deviceId+"\"}");
    					//System.out.println("Success");
    					//log.info("Success");
    				}else {
    					peer.getAsyncRemote().sendText("{\"openStatus\": \""+isStart[1]+"\",\"deviceId\": \""+deviceId+"\"}");
    					System.out.println("{\"openStatus\": \""+isStart[1]+"\",\"deviceId\": \""+deviceId+"\"}");
    					//System.out.println(isStart[1]);
    					//log.error(isStart[1]);
    				}
    			}else {
    				peer.getAsyncRemote().sendText("{\"openStatus\": \"Device is busy or not avialble at this time, Choose another device to continue\",\"deviceId\": \""+deviceId+"\"}");
    				System.out.println(deviceId+" Device is busy or not avialble at this time, Choose another device to continue");
    				
    			}
        	  
        		
        	} else if(figure.getJson().containsKey("closeVNCConnection")){
        		JSONObject jsonObject = (JSONObject) figure.getJson().get("closeVNCConnection");
        		String deviceId =(String) jsonObject.get("deviceId");
        		IDevice dService= new DeviceDAO();
        		
        		
        		int status = dService.checkDeviceConnectionStatus(deviceId);
        		if(status==1){
        			VMLiteVNCServerService vncService= new VMLiteVNCServerService();
        			
        			Object[] isStop = null;
        			try {
        				isStop = vncService.stopVNCServer(deviceId, "portrait");
        			} catch (InterruptedException e) {
        				// TODO Auto-generated catch block
        				e.printStackTrace();
        			}
        			
        			if((Boolean) isStop[0]){
        				dService.updateDeviceConnectionStatus(deviceId, 0);
        				peer.getAsyncRemote().sendText("{\"closeStatus\": \"Success\",\"deviceId\": \""+deviceId+"\"}");
             		   	System.out.println("{\"closeStatus\": \"Success\",\"deviceId\": \""+deviceId+"\"}");
        				
        			}else {
        				peer.getAsyncRemote().sendText("{\"closeStatus\": \""+isStop[1]+"\",\"deviceId\": \""+deviceId+"\"}");
             		   System.out.println("{\"closeStatus\": \"Success\",\""+isStop[1]+"\": \""+deviceId+"\"}");
        				
        			}
        		}else {
        			peer.getAsyncRemote().sendText("{\"closeStatus\": \"Device cleanup activity failure, Plaese contact support-apps\",\"deviceId\": \""+deviceId+"\"}");
         		   System.out.println("{\"closeStatus\": \"Device cleanup activity failure, Plaese contact support-apps\",\"deviceId\": \""+deviceId+"\"}");
        			
        		}
        	} else if(figure.getJson().containsKey("installApp")){
        		JSONObject jsonObject = (JSONObject) figure.getJson().get("installApp");
        		String deviceId =(String) jsonObject.get("deviceId");
        		String appId =  (String) jsonObject.get("appId");
        		String testType = (String) jsonObject.get("testType");
        		String userId = (String) jsonObject.get("user");
        		AppManageService appSer = new AppManageService();
        		AppManageDao appDao = new AppManageDao();
        		AppPojo app= new AppPojo();
        		app.setId(Integer.parseInt(appId));

        		try {
        			app=appDao.getAppInfoFromMangoDB(app, userId);
        			
        		} catch (java.text.ParseException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
        		appSer = new AppManageService();
        		if(testType == "robotium" ){
        			 boolean isUnsigned = appSer.appUnsignService(app);
        				if(isUnsigned){
        					boolean isResigned = appSer.appResignService(app);
        					if(isResigned){
        						//isInstall = appSer.appInstallService(deviceId, app);
        					}
        				}
        		}
               
        		boolean isInstall = appSer.appInstallService(deviceId, app);
        		
        		File temp= new File(app.getDownloadedAppPath());
        		temp.delete();
        		if(isInstall){
        			peer.getAsyncRemote().sendText("{\"installStatus\": \"Success\",\"deviceId\": \""+deviceId+"\"}");
        		   System.out.println("{\"installStatus\": \"Success\",\"deviceId\": \""+deviceId+"\"}");
        		 
        		} else {
        			peer.getAsyncRemote().sendText("{\"installStatus\": \"Failure\",\"deviceId\": \""+deviceId+"\"}");
         		   System.out.println("{\"installStatus\": \"Failure\",\"deviceId\": \""+deviceId+"\"}");
        		}
        	} else if(figure.getJson().containsKey("appCleanupActivity")){
        		JSONObject jsonObject = (JSONObject) figure.getJson().get("appCleanupActivity");
        		String packageName = (String) jsonObject.get("packageName");
        		String deviceId = (String) jsonObject.get("deviceId");
        		AppPojo app = new AppPojo();
        		app.setAppPackageName(packageName);
        		AppManageService appSer= new AppManageService();
        		boolean status= appSer.appUnInstallService(deviceId, app);
        		if(status){
        			peer.getAsyncRemote().sendText("{\"cleanupStatus\": \"Success\",\"deviceId\": \""+deviceId+"\"}");
         		   	System.out.println("{\"cleanupStatus\": \"Success\",\"deviceId\": \""+deviceId+"\"}");
        		}else {
        			peer.getAsyncRemote().sendText("{\"cleanupStatus\": \"Device cleanup activity failure, Plaese contact support-apps\",\"deviceId\": \""+deviceId+"\"}");
         		   	System.out.println("{\"cleanupStatus\": \"Device cleanup activity failure, Plaese contact support-apps\",\"deviceId\": \""+deviceId+"\"}");
        		}
        		
        	} else if(figure.getJson().containsKey("installAUTApp")){
        		JSONObject jsonObject = (JSONObject) figure.getJson().get("installAUTApp");
        		String deviceId = (String) jsonObject.get("deviceId");
        		String appId = (String) jsonObject.get("appId");
        		String autId =  (String) jsonObject.get("autId");
        		String userId = (String) jsonObject.get("user");
        		AppManageDao appDao = new AppManageDao();
    			AppPojo app= new AppPojo();
        		app.setId(Integer.parseInt(appId));
        		app.setAutId(Integer.parseInt(autId));
        			
        		try {
        			app=appDao.getAppAUTInfoFromMangoDB(app, userId);
        		} catch (java.text.ParseException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
        		
        		AppManageService appSer = new AppManageService();
        		boolean isUnsigned = appSer.appUnsignService(app);
        		boolean isInstall = false ;
        		if(isUnsigned){
        			boolean isResigned = appSer.appResignService(app);
        			if(isResigned){
        				isInstall = appSer.appAUTInstallService(deviceId, app);
        			}
        		}
        		boolean result = Files.deleteIfExists(Paths.get(app.getDownloadedAppPath()));
        		if(isInstall && result){
        			peer.getAsyncRemote().sendText("{\"installAUTStatus\": \"Success\",\"deviceId\": \""+deviceId+"\"}");
         		   	System.out.println("{\"installAUTStatus\": \"Success\",\"deviceId\": \""+deviceId+"\"}");
         		 
         		} else {
         			peer.getAsyncRemote().sendText("{\"installAUTStatus\": \"Failure\",\"deviceId\": \""+deviceId+"\"}");
          		   	System.out.println("{\"installAUTStatus\": \"Failure\",\"deviceId\": \""+deviceId+"\"}");
         		}
        		
        	}else if(figure.getJson().containsKey("InstrumentExecuter")){
        		JSONObject jsonObject = (JSONObject) figure.getJson().get("InstrumentExecuter");
        		//System.out.println(jsonObject.toJSONString());
        		InstrumentProxy proxy= new InstrumentProxy();
        		JSONObject resultJson =proxy.start(jsonObject,peer);
        		String deviceId = (String) jsonObject.get("deviceId");
        		String userId = (String) jsonObject.get("user");
        		
        		RoboTestDao results= new RoboTestDao();
        		boolean isSuccess = results.storeRoboTestResultsInMangoDb( resultJson, userId);
        	    if(isSuccess){
        	    	peer.getAsyncRemote().sendText("{\"InstrumentExecuterStatus\": \"Success\",\"deviceId\": \""+deviceId+"\"}");
          		   System.out.println("{\"InstrumentExecuterStatus\": \"Success\",\"deviceId\": \""+deviceId+"\"}");
          		
          		} else {
          			peer.getAsyncRemote().sendText("{\"InstrumentExecuterStatus\": \"Failure\",\"deviceId\": \""+deviceId+"\"}");
           		   System.out.println("{\"InstrumentExecuterStatus\": \"Failure\",\"deviceId\": \""+deviceId+"\"}");
          		}
        		
        	}else if(figure.getJson().containsKey("appiumTestExecuter")){
        		Process appiumPro = null;
            	BufferedReader appiumBuf = null;
            	System.out.println(figure.getJson().toJSONString());
            	JSONObject jsonObject = (JSONObject) figure.getJson().get("appiumTestExecuter");
        		final String deviceId = jsonObject.get("deviceId").toString();
        		final String appiumPort = jsonObject.get("tpAppiumPort").toString();
        		initialize() ;
        		InetAddress inet=Inet4Address.getLocalHost();
        		inet.getHostAddress();
        		//--platform-name Android --platform-version 5.1.1 --automation-name Appium
        		//--app D:\\apk\\instagram-9-5-0.apk --webhook localhost:9876 
        		String appiumCommand= nodeConfig+" "+appiumConfig+" --address "+appiumHost+" --port "+appiumPort+"  --webhook localhost:9876 --session-override  --log-no-color";
        		try {
        			appiumPro = new ProcessBuilder("cmd.exe", "/C", appiumCommand).start();
        			//log.info(command);
        			appiumBuf = new BufferedReader(new InputStreamReader(appiumPro.getInputStream()));
        			line = appiumBuf.readLine();
        			
        			ArrayList<String> appiumLogs = new ArrayList<String>();
        			LOG.info("Appium logging Started");
        		    while (isAlive) {
        		    	if(line != null){
        		    		if(!line.isEmpty()){
        		    			//System.out.println(deviceId+" :: "+line);
        		    			//LOG.info(deviceId+" :: "+line);
        		    			appiumLogs.add(line);
        		    			/*
        		    			JsonObject event = Json.createObjectBuilder().
					                        add("appiumLog",line).
					                        build();
        		    			synchronized(peer) {
        		    				//peer.getAsyncRemote().sendText("{\"appiumLog\": \""+line+"\"}");
        		    				peer.getAsyncRemote().sendText(event.toString());
        		    				
        		    			}*/
        		    			if(line.contains("Appium REST http interface listener started on")){
        							LOG.info(line);
        							isStarted = true;
        							aru= new AppiumResultUtility();
        							
    								peer.getAsyncRemote().sendText("{\"appiumServerStartStatus\": \""+line+"\",\"deviceId\": \""+deviceId+"\"}");
    								LOG.info("{\"appiumServerStartStatus\": \""+line+"\",\"deviceId\": \""+deviceId+"\"}");
        		    			
        							
        						}else if(session == null && !line.contains("desired") && line.contains("Session created with session id")){
        							LOG.info(line);
    			            		String[] sessionId=line.split(":");
    		            			session=sessionId[1];
    		            			session = session.replaceAll("\\s+","");
    		            			
    		            			/*peer.getAsyncRemote().sendText("{\"appiumSession\": \""+session+"\",\"deviceId\": \""+deviceId+"\"}");
    								LOG.info("{\"appiumSession\": \""+session+"\",\"deviceId\": \""+deviceId+"\"}");*/
    		            			
    			            	} else if( isStarted && deviceId != null && session != null){
    			            			LOG.info(line);
    			  						peer.getAsyncRemote().sendText("{\"appiumExeStatus\": \"Appium execition started on device\",\"deviceId\": \""+deviceId+"\"}");
    			  						LOG.info("{\"appiumExeStatus\": \"Appium execition started on device\",\"deviceId\": \""+deviceId+"\"}");
            		    			
        			  					try {
        			  						reports = new Reports(deviceId, session);
        				  					reports.startRecoding();
        			  					}catch(Exception e){
        			  						e.printStackTrace();
        			  					}
        			  					
        			  					
        			  					new Thread(new Runnable() {
        			  						public void run() {
        			  							synchronized(this){
        			  								InputStream is = null;
        			  						        InputStreamReader isr = null;
        			  						        BufferedReader deviceLogBuf =null;
        			  						        Process p = null;
        				  							 try {
        				  								ArrayList<String> deviceLogs = new ArrayList<String>();
        				  								LOG.info("Device logging Started");
        				  							        p = Runtime.getRuntime().exec("adb -s "+deviceId+" shell logcat");
        				  							        is = p.getInputStream();
        				  							        isr = new InputStreamReader(is);
        				  							        deviceLogBuf = new BufferedReader(isr);
        				  							        String deviceLogLine = null;
        				  							        while ((deviceLogLine = deviceLogBuf.readLine()) != null) {
        				  							        	if(!deviceLogLine.isEmpty()){
        				  							        	    deviceLogs.add(deviceLogLine);
        				  							        	    /*try{
        				  							        	    	
    	     				  							        		JsonObject deviceLogJson = Json.createObjectBuilder().
    	     				  							                        add("deviceLog",deviceLogLine).
    	     				  							                        build();
    	     				  							        		synchronized(peer){
    	     				  							        			peer.getAsyncRemote().sendText(deviceLogJson.toString());
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
        				  							        deviceLogMap.put(deviceId, deviceLogs);
        				  							    } catch (IOException  e) {
        				  							    	LOG.info(e.toString());
        				  							    } finally {
        				  							    	try {
        				  							    		if (deviceLogBuf!=null) {
        				  							    			deviceLogBuf.close();
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
    			            	}else if(!line.contains("desired") && !line.contains("null") && !line.contains("<-- DELETE")  ){
    			            		
	    			            	if(line.contains("Responding to client with success") || (line.contains("<-- POST") && line.contains("element") && line.contains(" 200 ") )){
	    			            		AppuimScreenshot screenshot = new AppuimScreenshot(deviceId, imageNo, session);
	    			            		screenshot.start();
	    			            		LOG.info(line);
	    			   					imageNo =1+imageNo;
	    			        
	    			            	} else if (line.contains("Responding to client with error") || (line.contains("<-- POST") && line.contains("element") && line.contains(" 500 ") ) || line.contains("Closing session, cause was 'New Command Timeout of")) {
	    			      		 		AppuimScreenshot screenshot = new AppuimScreenshot(deviceId, imageNo, session);
	    			            		screenshot.start();
	    			            		LOG.info(line);
	    			   				    isError = true;
	    			      		 	 	imageNo =1+imageNo;
	    							 }else if(line.contains("Creating new appium session")){
	    								 LOG.info(line);
	    							 }
    			            	
        			  	  		}else  if((line.contains("<-- DELETE") && line.contains(" 200 ")) || (line.contains("<-- POST") &&  line.contains(" 500 ")) || (line.contains("<-- POST") && line.contains("close") &&  line.contains(" 200 ")) ||  isError){
        			  	  			LOG.info(line);
        			            	String lines;
        							DEVICE_VIDEO_PATH = "/sdcard/"+session+deviceId+".mp4";//
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
        									//LOG.info(command);
        									r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        									
        									while((lines=r.readLine()) != null ){
        										if(!lines.isEmpty()){
        											if(lines.contains("100%")){
        												//log.info(lines);
        												String dalateCommand= "adb -s "+deviceId+" shell rm -f /sdcard/"+session+deviceId+".mp4";
        												Process deleteProcess = null;
        												BufferedReader deleteBufferedReader=null;
        												String deleteResults=null;    
        												try {
        													deleteProcess = new ProcessBuilder("cmd.exe", "/C", dalateCommand).start();
        													//LOG.info(dalateCommand);
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
        									//LOG.info(e.toString());
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
        									String tcp= getTCPProcessId(appiumPort);
        									try{
        										String cmd="Taskkill /PID "+tcp+" /F";
        										//String nodekill="taskkill /F /IM node.exe";
        										killProcess=new ProcessBuilder("cmd.exe", "/C", cmd).start();
        										killBuffer = new BufferedReader(new InputStreamReader(killProcess.getInputStream()));
        										while((killResults=killBuffer.readLine()) != null ){
        											if(!killResults.isEmpty()){
        												if(killResults.contains("SUCCESS")){
        													//LOG.info("Appium Server Closed Normally");
    													
    														 peer.getAsyncRemote().sendText("{\"appiumServerCloseStatus\": \"Appium REST http interface listener closed normally\",\"deviceId\": \""+deviceId+"\"}");
    														 LOG.info("{\"appiumServerCloseStatus\": \"Appium REST http interface listener closed normally\",\"deviceId\": \""+deviceId+"\"}");
			  	                		    			
        													
        													break;
        												}
        											}
        										}
        									}catch(Exception e){
        										//LOG.info(e.toString());
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
        								//LOG.info("Appium logging completed");
        							}
        						
        			            	
        			            }
        						
        						
        						
        					}
        		    	}
        		     	
        		    	
        		     	 line = appiumBuf.readLine();	
        				
        			}
        		    appiumLogMap.put(deviceId, appiumLogs);
        			if(!appiumLogMap.isEmpty()){
        				JSONObject logs= new JSONObject();
        				while(!logStop){
        					try {
        						Thread.sleep(1000);
        					} catch (InterruptedException e) {
        						// TODO Auto-generated catch block
        						e.printStackTrace();
        					}
        				}
        				logs.put("devicelog", deviceLogMap.get(deviceId));
        				logs.put("appiumLog", appiumLogMap.get(deviceId));
        				JSONObject app = (JSONObject) jsonObject.get("app");
        				String testName =jsonObject.get("testName").toString();
        				app.put("logs", logs);
        				if(reports!=null){
        					aru.saveVideo(FileUtils.getUserDirectory()+"\\AppData\\Local\\Temp\\"+session+deviceId+".mp4",deviceId ,app, session, isError, testName);
        					//report.resume();
        				}
        				
        		
        				
        			}
        		} catch ( IOException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		} finally {
        			try {
        				if(appiumPro!=null){
        					appiumPro.destroy();
        				}
        				if(appiumBuf!=null){
        					appiumBuf.close();
        				}
        				File fin = new File(FileUtils.getUserDirectory()+"\\AppData\\Local\\Temp\\"+session+deviceId+".mp4");
        				fin.delete();
        				/*File fin = new File(FileUtils.getUserDirectory()+"\\AppData\\Local\\Temp");

        				for (File file : fin.listFiles()) {
        					try{
        						FileDeleteStrategy.FORCE.delete(file);
        					} catch(Exception e){
        						
        					}
        				    
        				}*/
        				
    					peer.getAsyncRemote().sendText("{\"appiumExeComStatus\": \"Appium Execution Completed\",\"deviceId\": \""+deviceId+"\"}");
    					LOG.info("{\"appiumExeComStatus\": \"Appium Execution Completed\",\"deviceId\": \""+deviceId+"\"}");
    					deviceLogMap.remove(deviceId);
    					appiumLogMap.remove(deviceId);
        				
							
           			
        				//
        			} catch (IOException e) {
        				// TODO Auto-generated catch block
        				e.printStackTrace();
        			}
        		}
            } else if (figure.getJson().containsKey("connection")) {
            	JSONObject con = (JSONObject)figure.getJson().get("connection");
            	String status=	con.get("status").toString();
            	String deviceId=	con.get("deviceId").toString();
            	if(status == "false" || status.contains("false")){
            		LOG.info(status);
            		if(peers.contains(peer)){
            			synchronized(peer){
            				 peer.getAsyncRemote().sendText("{\"connect\": \"true\",\"deviceId\": \""+deviceId+"\"}");
 		    			
 		    			}
						//LOG.info("send to client {'connect': 'true'}");
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
			//appiumPort = prop.getProperty("APPIUM_PORT") ;
			
		} catch (IOException e) {
			

		}
	}
    private String getTCPProcessId(String port) throws IOException 
	{
		String command= "netstat -a -n -o | find \""+port+"\"";
		System.out.println(command);
		Process p = new ProcessBuilder("cmd.exe", "/C", command).start();
		BufferedReader tcpbuf = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String tcpProccessId= null;
		String line = tcpbuf.readLine();
		if(line != null){
    		if(!line.isEmpty()){
    			System.out.println(line);
    			if(line.contains(port)) {
    				String[] rs=line.split(" ");
    				int index=rs.length-1;
    				tcpProccessId = rs[index];
    				
    			}
    		}
		}
		
		return tcpProccessId;	
		
	}
}