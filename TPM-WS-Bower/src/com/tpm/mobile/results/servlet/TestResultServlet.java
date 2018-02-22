package com.tpm.mobile.results.servlet;

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

import com.tpm.mobile.results.dao.ExecResults;

/**
 * Servlet implementation class TestResultServlet
 */
@WebServlet("/getTestResult")
public class TestResultServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(TestResultServlet.class);
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
			log.error(e);
		}
		String testName = (String) obj.get("testName");
		testName=testName.replaceAll("^\"|\"$", "");
		JSONObject app =(JSONObject) obj.get("app");
		String deviceId = (String) obj.get("deviceId");
		deviceId=deviceId.replaceAll("^\"|\"$", "");
		log.info(testName);
		//log.info(app);
		log.info(deviceId);
		JSONObject result=null;
		try {
			HttpSession session=request.getSession(false);
			JSONObject user=(JSONObject) session.getAttribute("userInfo");
			//log.info(user.toJSONString());
			ExecResults testResult=new ExecResults();
			result = testResult.getResult(testName, app, deviceId, user);
			System.out.println(result);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		if(result!=null){
			//log.info(request);
			pw.print(result);
		}else {
			log.error("failed");
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
