package com.tpm.mobile.main.servlets;

import java.io.IOException;
import java.text.ParseException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.mongodb.MongoClient;
import com.tpm.mobile.android.app.service.AppManageDao;
import com.tpm.mobile.android.pojo.AppPojo;

/**
 * Servlet implementation class MoveToArchiveappDaovlet
 */
@WebServlet("/MoveToActiveApp")
public class MoveToActiveApp extends HttpServlet {
	private static final Logger log = Logger.getLogger(MoveToActiveApp.class);
	private static final long serialVersionUID = 1L;
	private AppManageDao appDao;
	private AppPojo app;
	protected JSONObject jObj = null;
   
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONParser parser = new JSONParser();
        JSONObject jObj = null;
        String appid = null;
        String status = null;
		try {
			jObj = (JSONObject) parser.parse(request.getReader());
			appid = (String) jObj.get("appid");
			status = (String) jObj.get("status");
		} catch (org.json.simple.parser.ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int id= Integer.parseInt(appid);
		app = new AppPojo();
		app.setId(id);
		app.setStatus(status);
		appDao = new AppManageDao();
		HttpSession session=request.getSession(false);
	    JSONObject user=(JSONObject) session.getAttribute("userInfo");
		try {
			appDao.updateAppInfoFromMangoDB(app, user.get("userId").toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			log.error(e);
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

