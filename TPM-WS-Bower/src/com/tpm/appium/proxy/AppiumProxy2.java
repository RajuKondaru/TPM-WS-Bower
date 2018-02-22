package com.tpm.appium.proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.tpm.mobile.android.exe.api.appium.utils.AppuimScreenshot;

@WebServlet("/wd/hub/*")
public class AppiumProxy2 extends HttpServlet{
	/**
	 * 
	 */
	private static Integer imageNo = 0;
	private static final long serialVersionUID = 1L;
	String appiumPort=null;
	StringBuffer reqUrl=null;

	/** 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out= response.getWriter();
		StringBuffer reqUrl= request.getRequestURL();
		
		HttpSession session=request.getSession(false);
		try {
			System.out.println("Get Request :: "+ session.getId());
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		if(session!=null && session.getAttribute("authentication").equals(true)){
			appiumPort = (String) session.getAttribute("appiumPort");
			reqUrl= request.getRequestURL();
			reqUrl=reqUrl.replace(0, 43, "http://appium.testpace.com:"+appiumPort+"/wd/hub");
			System.out.println("Get Request :: "+reqUrl.toString() );
			HttpUtility.sendGetRequest(reqUrl.toString());
			String res;
			try {
				res=HttpUtility.readSingleLineRespone();
				out.write(res);
				System.out.println("Get Response :: "+res);
			} catch (Exception e) {
				System.err.println("Get Error ::" +e.getMessage());
			}
		} else {
			out.write(HttpServletResponse.SC_UNAUTHORIZED);
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session=request.getSession(false);
		try {
			System.out.println("Post Request :: "+ session.getId());
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		PrintWriter out= response.getWriter();
		JSONParser parser = new JSONParser();
		JSONObject jObj = null;
		String api_key = null;
		String deviceId = null;
		
		try {
			jObj = (JSONObject) parser.parse(request.getReader());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(jObj.containsKey("desiredCapabilities")){
			JSONObject desCaps=(JSONObject) jObj.get("desiredCapabilities");
			api_key=(String) desCaps.get("api_key");
			deviceId=(String) desCaps.get("udid");
			appiumPort=(String) desCaps.get("tpAppiumPort");
			if(api_key!=null){
				session = request.getSession();
				session.setAttribute("authentication", true);
				session.setAttribute("deviceId", deviceId);
				session.setAttribute("appiumPort", appiumPort);
				//System.out.println(api_key);
			} else {
				session = request.getSession();
				session.setAttribute("authentication", false);
			}
			
		}
		
		if(session.getAttribute("authentication").equals(true)){
			try{
				appiumPort = (String) session.getAttribute("appiumPort");
				reqUrl= request.getRequestURL();
				reqUrl=reqUrl.replace(0, 43, "http://appium.testpace.com:"+appiumPort+"/wd/hub");
				
				System.out.println("Post Request :: "+reqUrl.toString() +"  "+jObj.toString());
				//HttpUtility.sendPostRequest(reqUrl.toString(), jObj.toJSONString());
				HttpClient httpClient = HttpClientBuilder.create().build();
				HttpResponse httpRes= null;
				try {

				    HttpPost httpReq = new HttpPost(reqUrl.toString());
				   
				    httpReq.addHeader("content-type", "application/json");
				    httpReq.setEntity(new StringEntity(jObj.toJSONString()));
				    httpRes = httpClient.execute(httpReq);

				    //handle response here...

				}catch (Exception ex) {

				    //handle exception here

				} finally {
				    //Deprecated
				    //httpClient.getConnectionManager().shutdown(); 
				}
				String res = null;
				try {
					HttpEntity entity = httpRes.getEntity();
					if (entity != null) {

			            // A Simple JSON Response Read
			            InputStream instream = entity.getContent();
			            res = convertStreamToString(instream);
			            // now you have the string representation of the HTML request
			            //System.out.println("RESPONSE: " + res);
			            instream.close();
			           

			        }
				}catch (Exception e) {
					// TODO: handle exception
				}
				
		        /*// Headers
		        org.apache.http.Header[] headers = httpRes.getAllHeaders();
		        for (int i = 0; i < headers.length; i++) {
		            System.out.println(headers[i]);
		        }*/
				
				
				String sessionId = null;
				session = request.getSession();
				
				if(session.getAttribute("imageNo") != null) {
					imageNo =Integer.parseInt( session.getAttribute("imageNo").toString());
					imageNo = imageNo+1;
					//System.out.println("From get "+imageNo);
				} else {
					session.setAttribute("imageNo",imageNo);
					imageNo=imageNo+1;
					//System.out.println("From set "+imageNo);
				}
				try {
				
						//res=HttpUtility.readSingleLineRespone();
					
				
					if(res.contains("sessionId")){
						JSONParser jsonparser = new JSONParser(); 
						JSONObject json = (JSONObject) jsonparser.parse(res.toString());
						sessionId= (String) json.get("sessionId");
						deviceId = (String) session.getAttribute("deviceId");
						Thread.sleep(3000);
						AppuimScreenshot screenshot = new AppuimScreenshot(deviceId, imageNo, sessionId);
	            		screenshot.getScreenshot();
	            		session.setAttribute("imageNo", imageNo);
	            		if(res.contains("value")) {
	            			if(json.get("value")!=null) {
	            				String value= json.get("value").toString();
		            			if(value.contains("Successfully closed")) {
		            				//System.out.println("value :: "+ value);
		            				imageNo =0;
		            				session.setAttribute("imageNo", imageNo);
		            			}
	            			} else {
	            				//System.out.println("value :: "+ res.toString());
							}
	            			
	            			
	            		}
	            		out.write(res);
						
						System.out.println("Post Response :: "+ res.toString());
						session = request.getSession();
						session.setAttribute("session", sessionId);
					} 
					
				} catch (Exception e) {
					e.printStackTrace();
					sessionId= (String) session.getAttribute("session");
					AppuimScreenshot screenshot = new AppuimScreenshot(deviceId, imageNo, sessionId);
            		screenshot.getScreenshot();
					out.write("{\"status\":7,\"value\":{\"message\":\"An element could not be located on the page using the given search parameters.\",\"origValue\":\"No element found\"},\"sessionId\":\""+sessionId+"\"}");
					//System.err.println("Post Error ::" +e.getMessage());
					System.err.println("{\"status\":7,\"value\":{\"message\":\"An element could not be located on the page using the given search parameters.\",\"origValue\":\"No element found\"},\"sessionId\":\""+sessionId+"\"}");
					//session.setAttribute("imageNo", 0);
				}
				
			}catch(Exception e){
				
				e.printStackTrace();
				imageNo =0;
				session.setAttribute("imageNo", imageNo);
				out.write(HttpServletResponse.SC_NOT_FOUND);
			}
		} else {
			imageNo =0;
			session.setAttribute("imageNo", imageNo);
			out.write(HttpServletResponse.SC_UNAUTHORIZED);
		}
		
	
		
	  
	}
	private static String convertStreamToString(InputStream is) {

	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    StringBuilder sb = new StringBuilder();

	    String line = null;
	    try {
	        while ((line = reader.readLine()) != null) {
	            sb.append(line + "\n");
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            is.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    return sb.toString();
	}
    
}