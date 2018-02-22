package com.tpm.mobile.main.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tpm.mobile.android.dao.DeviceDAO;
import com.tpm.mobile.android.pojo.WebConfigModel;

/**
 * Servlet implementation class getAllWebConfigurationsServlet
 */
@WebServlet("/GetAllWebConfigs")
public class GetAllWebConfigs extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(GetAllWebConfigs.class);
	
	protected WebConfigModel webConfigs;
	protected DeviceDAO deviceDao;
	protected List<WebConfigModel> webConfigsList;
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		deviceDao= new DeviceDAO();
		webConfigsList = deviceDao.getAllWebConfigs();
		log.info("Web Configs List  ::  "+ webConfigsList);
		response.setContentType("application/json; charset=UTF-8");
		JSONArray webConfigjsonArray = new JSONArray();
		try {
			for (WebConfigModel webConfigModel : webConfigsList) {
				log.info("Web Config Model  ::  "+ webConfigModel);
				JSONObject webConfigJson = new JSONObject();
				webConfigJson.put("weburl", webConfigModel.getWebUrl());
				webConfigJson.put("webname", webConfigModel.getWebName());
				webConfigJson.put("webversion", webConfigModel.getWebVersion());
				webConfigjsonArray.put(webConfigJson);
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			log.error(e);
		}
		log.info("Web Config json Array   ::  "+webConfigjsonArray);
        response.getWriter().print(webConfigjsonArray);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
