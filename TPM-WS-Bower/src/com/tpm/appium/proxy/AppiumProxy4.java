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

@WebServlet("/ws/hub/*")
public class AppiumProxy4 extends HttpServlet{
	/**
	 * 
	 */
	private static Integer imageNo = 0;
	private static final long serialVersionUID = 1L;
	

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
		JSONObject jObj = null;
		String deviceId = null;
		PrintWriter out= response.getWriter();
		JSONParser parser = new JSONParser();
		
		HttpSession session=request.getSession(true);
		
		StringBuffer reqUrl= request.getRequestURL();
		try {
			
			//System.out.println("Post Request :: "+reqUrl.toString() );
			//System.out.println("Post Request :: "+ session.getId());
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		try {
			jObj = (JSONObject) parser.parse(request.getReader());
			//System.out.println(jObj.toJSONString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(jObj.containsKey("desiredCapabilities")){
			JSONObject desCaps=(JSONObject) jObj.get("desiredCapabilities");
			/*instanceMapper = new HashMap<String, Object>();*/
			deviceId=(String) desCaps.get("udid");
			appiumPort=(String) desCaps.get("appiumPort");
			if(appiumPort!=null && deviceId!=null ){
				//http://localhost:8080/TestPaceMobile/ws/hub/token/2511
				reqUrl=reqUrl.replace(0, 54, "http://appium.testpace.com:"+appiumPort+"/wd/hub");
				res =AppiumRequestProcessor.requstProcess(reqUrl, jObj);
				imageNo=imageNo+1;
				
				session.setAttribute("imageNo",imageNo.toString());
				session.setAttribute("deviceId", deviceId);
				session.setAttribute("appiumPort", appiumPort);
				
				
				
			} else {
				
			}
			
			
			
			
		} else {
			if (session.getAttribute("imageNo") != null && session.getAttribute("deviceId") != null && session.getAttribute("appiumPort") != null ) {
				   appiumPort =(String) session.getAttribute("appiumPort");
				   deviceId =(String) session.getAttribute("deviceId");
				   imageNo = Integer.parseInt( session.getAttribute("imageNo").toString());
				   imageNo = imageNo+1;
				   reqUrl=reqUrl.replace(0, 54, "http://appium.testpace.com:"+appiumPort+"/wd/hub");
				   res =AppiumRequestProcessor.requstProcess(reqUrl, jObj);
				   session.setAttribute("imageNo",imageNo.toString());
				
			    
			   
			} else {
			
				
			}
			
		}
		
		
		try{
			
			if(res!=null) {
				try {
					if(res.contains("sessionId")){
						JSONObject json = (JSONObject) parser.parse(res.toString());
						sessionId= (String) json.get("sessionId");
						AppuimScreenshot screenshot = new AppuimScreenshot(deviceId, imageNo, sessionId);
	            		screenshot.getScreenshot();
	            		/*if(res.contains("value")) {
	            			if(json.get("value")!=null) {
	            				String value= json.get("value").toString();
		            			if(value.contains("Successfully closed")) {
		            				imageNo =0;
		            				session.invalidate();
		            				System.out.println("Appium session removed");
		            				out.write(res);
		            				System.out.println("Post Responce to closed::"+ sessionId+" :: "+ res);
		            				return;
		            			}
	            			} else {
	            			
	            				if(reqUrl.toString().contains("close") ) {
		            				//System.out.println("value :: "+ value);
		            				imageNo =0;
		            				session.setMaxInactiveInterval(60);
		            				out.write(res);
		            				System.out.println("Post Responce to close::"+ sessionId+" :: "+ res);
		            				return;
		            			}
							}
	            			
	            			
	            		}*/
	            		
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
			session.invalidate();
			System.out.println("Appium session removed");
			out.write(HttpServletResponse.SC_NOT_FOUND);
		}
		
		
	
		
	  
	}
	
    
}