package com.tpm.mobile.main.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

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

import com.mongodb.MongoClient;
import com.tpm.mobile.android.app.service.AppManageDao;
import com.tpm.mobile.android.pojo.AppPojo;

/**
 * Servlet implementation class SaveWebConfigurationsServlet
 */
@WebServlet("/SaveWebApp")
public class WebAppUploap extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(WebAppUploap.class);
	protected AppPojo webConfigs;
	protected AppManageDao appDao;
	protected List<AppPojo> webConfigsList;
	protected JSONObject jObj = null;
	protected String url = null;
	protected String name = null;
	protected String version = null;
	protected String apptype = null;
	protected String icon = null;
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONParser parser = new JSONParser();
		try {
			jObj = (JSONObject) parser.parse(request.getReader());
			url = jObj.get("url").toString();
			name = jObj.get("name").toString();
			version = jObj.get("version").toString();
			apptype = jObj.get("apptype").toString();
			icon = jObj.get("iconurl").toString();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        HttpSession session=request.getSession(false);
        JSONObject user=(JSONObject) session.getAttribute("userInfo");
        webConfigs= new AppPojo();
		webConfigs.setUserId(user.get("userId").toString());
		webConfigs.setWebName(name);
		webConfigs.setWebUrl(url);
		webConfigs.setWebVersion(version);
		webConfigs.setAppType(apptype);
		webConfigs.setIcon(icon);
		webConfigs.setDefaultIcon("./images/apps/web-red1.png");
		webConfigs.setStatus("Active");
		appDao = new AppManageDao();
		appDao.storeWebAppInMangoDB(webConfigs, user.get("userId").toString());
			
			
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
