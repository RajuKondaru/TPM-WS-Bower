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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.tpm.mobile.manual.dao.ManualTest;

/**
 * Servlet implementation class SaveWriteManualTestCaseServlet
 */
@WebServlet("/saveManualTestCaseResultServlet")
public class SaveManualTestCaseResultServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(SaveManualTestCaseResultServlet.class);
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
		JSONParser parser = new JSONParser();
		
		JSONObject obj = null;
		String testcaseName = null;
		String deviceId = null;
		JSONArray testcaseArray = null;
		JSONObject appobj = null;
		
		try {
			obj = (JSONObject) parser.parse(request.getReader());
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		testcaseName = obj.get("testName").toString();
		testcaseName=testcaseName.replaceAll("^\"|\"$", "");
		deviceId = obj.get("deviceId").toString();
		deviceId=deviceId.replaceAll("^\"|\"$", "");
		testcaseArray = (JSONArray) obj.get("testcase");
		System.out.println(testcaseArray.toJSONString());
		appobj =(JSONObject) obj.get("app");
		
		/*log.info(testcaseArray.get(0));
		log.info(appobj);
		log.info(deviceId);*/
	
	
		JSONObject resultJson = new JSONObject();
		
	
		resultJson.put("testName", testcaseName);
		resultJson.put("app", appobj);
		resultJson.put("deviceId", deviceId);
		resultJson.put("test", testcaseArray.toString());
		if(testcaseArray.toString().contains("pedding")){
			resultJson.put("testResult", "Pedding");
		} else if(testcaseArray.toString().contains("fail")){
			resultJson.put("testResult", "Failed");
		} else {
			resultJson.put("testResult", "Passed");
		}
		
		
		HttpSession session=request.getSession(false);
	    JSONObject user=(JSONObject) session.getAttribute("userInfo");
		    
			
		
		ManualTest manual=new ManualTest();
		boolean isupdate = false;
		isupdate = manual.storeManualTestResultsInMangoDb(resultJson, user);
		log.info(isupdate);
		if(isupdate){
			pw.print("Success");
		}else {
			pw.print("failed");
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
