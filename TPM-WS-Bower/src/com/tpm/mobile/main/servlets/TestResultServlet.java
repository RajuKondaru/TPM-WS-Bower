package com.tpm.mobile.main.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

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
import com.tpm.mobile.android.dao.DeviceDAO;
import com.tpm.mobile.results.dao.ExecResults;

/**
 * Servlet implementation class TestResultServlet
 */
@WebServlet("/TestResult")
public class TestResultServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(TestResultServlet.class);
   /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
		JSONParser parser = new JSONParser();
		JSONObject obj = null;
		try {
			obj = (JSONObject) parser.parse(request.getReader());
		} catch (ParseException e) {
			log.error(e);
		}
		String testName = (String) obj.get("testName");
		testName=testName.replaceAll("^\"|\"$", "");
		String appName = (String) obj.get("appName");
		String deviceName = (String) obj.get("deviceName");
		String[] d=deviceName.split(" ");
		String deviceModel = d[1];
		HttpSession session=request.getSession(false);
		JSONObject user=(JSONObject) session.getAttribute("userInfo");
	  
		AppManageDao appServ= new AppManageDao();
		JSONObject app = appServ.getAppInfoByNameFromMongoDB(appName, user.get("userId").toString());
		DeviceDAO deviceDao= new DeviceDAO();
		JSONObject device= null;
		
		try {
			device=deviceDao.getDeviceByModel(deviceModel);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*log.info(testName);
		log.info(app);
		log.info( device.get("DeviceUDID").toString());
		log.info(user.toJSONString());	*/
		ExecResults testResult=new ExecResults();
		JSONObject result = testResult.getResult(testName, app, device.get("DeviceUDID").toString(), user);
		JSONObject resultInfo= new JSONObject();
		resultInfo.put("result", result.toString());
		resultInfo.put("app", app.toString());
		resultInfo.put("device", device.toString());
		if(resultInfo!=null){
			pw.print(resultInfo);
			log.info("Success");
			//System.out.println(resultInfo);
		}else {
			pw.print("failed");
			log.info("Failed");
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
