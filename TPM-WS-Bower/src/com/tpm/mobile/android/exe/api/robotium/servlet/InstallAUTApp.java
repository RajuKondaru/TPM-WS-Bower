package com.tpm.mobile.android.exe.api.robotium.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.tpm.mobile.android.app.service.AppManageDao;
import com.tpm.mobile.android.app.service.AppManageService;
import com.tpm.mobile.android.pojo.AppPojo;

/**
 * Servlet implementation class PrepareRoboExecution
 */
@WebServlet("/installAUTApp")
public class InstallAUTApp extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(InstallAUTApp.class);
	private AppManageDao appDao=null;
	private boolean isResigned = false;
	private boolean isUnsigned = false;
	private boolean isInstall= false;
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		JSONParser parser = new JSONParser();
		JSONObject jObj = null;
		String deviceId = null;
        String appId = null;
        String autId = null;
        try {
			jObj = (JSONObject) parser.parse(request.getReader());
			deviceId = (String) jObj.get("deviceId");
			appId = (String) jObj.get("appId");
			autId = (String) jObj.get("autId");
			
			
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
       	
		appDao = new AppManageDao();
			
		AppPojo app= new AppPojo();
		app.setId(Integer.parseInt(appId));
		app.setAutId(Integer.parseInt(autId));
		HttpSession session=request.getSession(false);
	    JSONObject user=(JSONObject) session.getAttribute("userInfo");
			
		try {
			app=appDao.getAppAUTInfoFromMangoDB(app, user.get("userId").toString());
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		AppManageService appSer = new AppManageService();
        isUnsigned = appSer.appUnsignService(app);
		if(isUnsigned){
			isResigned = appSer.appResignService(app);
			if(isResigned){
				isInstall = appSer.appAUTInstallService(deviceId, app);
			}
		}
		boolean result = Files.deleteIfExists(Paths.get(app.getDownloadedAppPath()));
		if(isInstall && result){
	 	    out.write("Success");
	 	    log.info("Success");
	    } else {
	    	out.write("failure");
	    	log.info("failure");
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
