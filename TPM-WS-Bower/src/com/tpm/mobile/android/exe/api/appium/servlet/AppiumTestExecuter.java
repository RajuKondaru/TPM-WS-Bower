package com.tpm.mobile.android.exe.api.appium.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.tpm.mobile.android.exe.api.appium.utils.AppiumProxy;

/**
 * Servlet implementation class AppiumTestExecuter
 */
@WebServlet("/appiumTestExecuter")
public class AppiumTestExecuter extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private AppiumProxy proxy=null;
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		JSONParser parser = new JSONParser();
		 JSONObject jObj = null;
		try {
			jObj = (JSONObject) parser.parse(request.getReader());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
        String deviceid = null ;
        String testName = null ;
        
        JSONObject app = null ;
		deviceid = (String) jObj.get("deviceId");
		testName = (String) jObj.get("testName");
		app =(JSONObject) jObj.get("app");
		/*Thread proxy= new Thread(new AppiumProxy(deviceid, app));
		proxy.start();*/
		new AppiumProxy().startProxy(deviceid, app, testName);
		out.write("Success");
		
    	
			
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
