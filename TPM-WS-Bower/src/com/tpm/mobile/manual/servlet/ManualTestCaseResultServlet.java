package com.tpm.mobile.manual.servlet;

import java.io.IOException;
import java.io.PrintWriter;
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

import com.tpm.mobile.manual.dao.ManualTest;

/**
 * Servlet implementation class SaveWriteManualTestCaseServlet
 */
@WebServlet("/getManualTestCaseResultServlet")
public class ManualTestCaseResultServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(ManualTestCaseResultServlet.class);
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
		JSONParser parser = new JSONParser();
		JSONObject obj = null;
		try {
			obj = (JSONObject) parser.parse(request.getReader());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String testcaseName = null;
		JSONObject appobj = null;
		String deviceId = null;
		testcaseName = obj.get("testcasename").toString();
		appobj = (JSONObject) obj.get("app");
		deviceId = obj.get("deviceId").toString();
	
		testcaseName=testcaseName.replaceAll("^\"|\"$", "");
		
		HttpSession session=request.getSession(false);
	    JSONObject user=(JSONObject) session.getAttribute("userInfo");
		
		
		deviceId=deviceId.replaceAll("^\"|\"$", "");
		/*log.info(appobj);
		log.info(deviceId);
		log.info(testcaseName);*/
		ManualTest manual=new ManualTest();
		JSONObject result = manual.getManualtestResultFromMangoDb(testcaseName, appobj, deviceId, user);
		
		if(result!=null){
			pw.print(result);
			log.info("success");
		}else {
			pw.print("failed");
			log.warn("failed");
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
