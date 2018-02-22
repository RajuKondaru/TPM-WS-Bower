package com.tpm.mobile.main.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.tpm.mobile.android.app.service.AppManageService;
import com.tpm.mobile.android.pojo.AppPojo;

/**
 * Servlet implementation class StopExecuterServlet
 */
@SuppressWarnings("serial")
@WebServlet("/LaunchBrowser")
public class LaunchBrowser extends HttpServlet {
	private static final Logger log = Logger.getLogger(LaunchBrowser.class);
	protected String deviceID;
	protected JSONObject test = null;
	protected String deviceid = null ;
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter pw = response.getWriter();
		JSONParser parser = new JSONParser();
		JSONObject obj = null;
		try {
			obj = (JSONObject) parser.parse(request.getReader());
			test = (JSONObject) obj.get("test");
			deviceid = (String) obj.get("deviceId");
		} catch (ParseException e) {
			log.error(e);
		}
		JSONObject appobj = (JSONObject)test.get("app");
		String webUrl =(String) appobj.get("webUrl");
		log.info(webUrl);
		AppPojo app = new AppPojo();
		app.setWebUrl(webUrl);
		AppManageService appSer= new AppManageService();
		boolean status= appSer.browserLaunchService(deviceid, app);
		
		
		if(status){
			pw.print("Success");
			log.info("Browser Startup activity Success");
		}else {
			pw.print("Failure");
			log.info("Browser Startup activity failure, Plaese contact support-apps");
		}
		
		
			
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
