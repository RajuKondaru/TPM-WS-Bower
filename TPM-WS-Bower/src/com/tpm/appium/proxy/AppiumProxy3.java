package com.tpm.appium.proxy;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.tpm.mobile.android.exe.api.appium.utils.AppuimScreenshot;

@WebServlet("/ws1/hub/*")
public class AppiumProxy3 extends HttpServlet{
	/**
	 * 
	 */
	private static Integer imageNo = 0;
	private static final long serialVersionUID = 1L;
	Map<String, Map<String, Object>> sessionMapper = new HashMap<String, Map<String, Object>>();
	

	/** 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String appiumPort=null;
		String res= null;
		String sessionId = null;
		Map<String, Object> instanceMapper = null;
		HttpSession session=request.getSession(false);
		try {
			session.getAttribute("sessionMapper");
		}catch (Exception e) {
			session=request.getSession();
			//System.out.println("Post Request first:: "+ session.getId());
		}
		StringBuffer reqUrl= request.getRequestURL();
		try {
			
			System.out.println("Post Request :: "+reqUrl.toString() );
			//System.out.println("Post Request :: "+ session.getId());
		}catch (Exception e) {
			// TODO: handle exception
		}
		PrintWriter out= response.getWriter();
		JSONParser parser = new JSONParser();
		JSONObject jObj = null;
		
		String deviceId = null;
		
		try {
			jObj = (JSONObject) parser.parse(request.getReader());
			System.out.println(jObj.toJSONString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(jObj.containsKey("desiredCapabilities")){
			JSONObject desCaps=(JSONObject) jObj.get("desiredCapabilities");
			instanceMapper = new HashMap<String, Object>();
			deviceId=(String) desCaps.get("udid");
			appiumPort=(String) desCaps.get("appiumPort");
			if(appiumPort!=null && deviceId!=null ){
				//http://localhost:8080/TestPaceMobile/ws/hub/token/2511
				reqUrl=reqUrl.replace(0, 54, "http://appium.testpace.com:"+appiumPort+"/wd/hub");
				res =AppiumRequestProcessor.requstProcess(reqUrl, jObj);
				imageNo=imageNo+1;
				instanceMapper.put("imageNo",imageNo.toString());
				instanceMapper.put("deviceId", deviceId);
				instanceMapper.put("appiumPort", appiumPort);
				
			} else {
				System.out.println("");
			}
			
			
			
			
		} else {
			if (session.getAttribute("sessionMapper") != null) {
				sessionMapper=(Map<String, Map<String, Object>>) session.getAttribute("sessionMapper");
			   if(sessionMapper!=null && !sessionMapper.isEmpty()) {
				   for (Entry<String, Map<String, Object>> entry : sessionMapper.entrySet()) {
					   if(reqUrl.toString().contains(entry.getKey())) {
						   //System.out.println("Key = " + entry.getKey() +
		                    //       ", Value = " + entry.getValue());
						  
						   instanceMapper=entry.getValue();
						   appiumPort =(String) instanceMapper.get("appiumPort");
						   deviceId =(String) instanceMapper.get("deviceId");
						   imageNo = Integer.parseInt( instanceMapper.get("imageNo").toString());
						   imageNo = imageNo+1;
						   reqUrl=reqUrl.replace(0, 54, "http://appium.testpace.com:"+appiumPort+"/wd/hub");
						   res =AppiumRequestProcessor.requstProcess(reqUrl, jObj);
						   instanceMapper.put("imageNo",imageNo.toString());
							
					   }
		    
				   }
			          
			   }
			    
			    
			   
			} else {
			
				
			}
			
		}
		
		
			try{
				
				if(res!=null) {
					try {
						if(res.contains("sessionId")){
							JSONParser jsonparser = new JSONParser(); 
							JSONObject json = (JSONObject) jsonparser.parse(res.toString());
							sessionId= (String) json.get("sessionId");
							Thread.sleep(3000);
							AppuimScreenshot screenshot = new AppuimScreenshot(deviceId, imageNo, sessionId);
		            		screenshot.getScreenshot();
		            		if(res.contains("value")) {
		            			if(json.get("value")!=null) {
		            				String value= json.get("value").toString();
			            			if(value.contains("Successfully closed") || reqUrl.toString().contains("close") ) {
			            				//System.out.println("value :: "+ value);
			            				imageNo =0;
			            				sessionMapper.remove(sessionId, instanceMapper);
			            				System.out.println("Appium session removed");
			            				out.write(res);
			            				return;
			            			}
		            			} else {
		            				//System.out.println("value :: "+ res.toString());
		            				if(reqUrl.toString().contains("close") ) {
			            				//System.out.println("value :: "+ value);
			            				imageNo =0;
			            				sessionMapper.remove(sessionId, instanceMapper);
			            				System.out.println("Appium session removed");
			            				out.write(res);
			            				return;
			            			}
								}
		            			
		            			
		            		}
		            		
		            		if(jObj.containsKey("desiredCapabilities")){
		            			sessionMapper.put(sessionId, instanceMapper);
		            			session.setAttribute("sessionMapper", sessionMapper);
		            			System.out.println("Appium session added to application session");
		            		} else {
		            			sessionMapper.replace(sessionId, instanceMapper);
		            			session.setAttribute("sessionMapper", sessionMapper);
							}
		            		out.write(res);
		            		System.out.println("Post Responce ::"+ sessionId+" :: "+ res);
						} 
						
					} catch (Exception e) {
						e.printStackTrace();
						
						AppuimScreenshot screenshot = new AppuimScreenshot(deviceId, imageNo, sessionId);
	            		screenshot.getScreenshot();
						out.write("{\"status\":7,\"value\":{\"message\":\"An element could not be located on the page using the given search parameters.\",\"origValue\":\"No element found\"},\"sessionId\":\""+sessionId+"\"}");
						
					}
				}
			}catch(Exception e){
				
				e.printStackTrace();
				sessionMapper.remove(sessionId, instanceMapper);
				System.out.println("Appium session removed");
				out.write(HttpServletResponse.SC_NOT_FOUND);
			}
		
		
	
		
	  
	}
	
    
}