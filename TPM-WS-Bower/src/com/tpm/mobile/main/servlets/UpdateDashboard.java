package com.tpm.mobile.main.servlets;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.tpm.mobile.android.app.service.AppManageDao;
import com.tpm.mobile.android.dao.DeviceDAO;
import com.tpm.mobile.android.pojo.AppPojo;
import com.tpm.mobile.android.pojo.DeviceModel;
import com.tpm.mobile.results.dao.ExecResults;

/**
 * Servlet implementation class UpdateDashboard
 */
@WebServlet("/UpdateDashboard")
public class UpdateDashboard extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(UpdateDashboard.class);
	private AppManageDao appDao;
	private ExecResults results;
	protected JSONObject jObj = null;
	protected DeviceDAO deviceDao;
	
	
   
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<org.json.simple.JSONObject> resultlist = null;
		JSONObject up_dash =  new JSONObject();
		HttpSession session=request.getSession(false);
	    JSONObject user=(JSONObject) session.getAttribute("userInfo");
	    log.info(user.get("userId"));
		try {
			appDao = new AppManageDao();
			List<AppPojo> apps = appDao.getAppsInfoFromMangoDB(user.get("userId").toString());
			deviceDao= new DeviceDAO();
			List <DeviceModel> connectedDevices = deviceDao.getAllDevicesInfoFromMango();
			results= new ExecResults();
			resultlist = results.getAllResults(user);
			Gson gson = new Gson();
			String jsonArrayApps = gson.toJson(apps);
			String jsonArrayDevices = gson.toJson(connectedDevices);
			//log.info(resultlist.toString());
			up_dash.put("apps",jsonArrayApps);
			up_dash.put("devices",jsonArrayDevices);
			up_dash.put("results",resultlist.toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			log.error(e);
		} 
		
		//log.info("Json Response to client :: " + up_dash);
		//response.setContentType("application/json; charset=UTF-8");
		response.getWriter().print(up_dash);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
