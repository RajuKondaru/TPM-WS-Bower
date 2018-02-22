package com.tpm.mobile.main.servlets;

import java.io.IOException;
import java.io.PrintWriter;
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
@WebServlet("/DeleteApp")
public class DeleteApp extends HttpServlet {
	private static final Logger log = Logger.getLogger(DeleteApp.class);
	private static final long serialVersionUID = 1L;
	private AppManageDao appDao;
	private AppPojo app;
	protected JSONObject jObj = null;
   
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
		JSONParser parser = new JSONParser();
        JSONObject jObj = null;
        String appid = null;
        Boolean isDeleted=false;
    	try {
			jObj = (JSONObject) parser.parse(request.getReader());
			appid = (String) jObj.get("appid");
		} catch (org.json.simple.parser.ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int id= Integer.parseInt(appid);
		app = new AppPojo();
		app.setId(id);
		appDao = new AppManageDao();
		HttpSession session=request.getSession(false);
	    JSONObject user=(JSONObject) session.getAttribute("userInfo");
		try {
			isDeleted = appDao.deleteAppFromMangoDB(app, user.get("userId").toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			log.error(e);
		}
		
		if(isDeleted){
			pw.print("Success");
			log.info("App Deleted Successfully");
		}else {
			pw.print("Failure");
			log.info("App Deletion Failed");
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

