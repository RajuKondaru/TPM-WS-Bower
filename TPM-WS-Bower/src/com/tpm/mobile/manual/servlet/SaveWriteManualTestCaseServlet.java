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
@WebServlet("/saveWriteManualTestCaseServlet")
public class SaveWriteManualTestCaseServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(SaveWriteManualTestCaseServlet.class);
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
		String testcaseName = obj.get("testcasename").toString();
		testcaseName=testcaseName.replaceAll("^\"|\"$", "");
		JSONArray testcaseArray = (JSONArray) obj.get("testcase");
		JSONObject appobj =(JSONObject) obj.get("app");
		//log.info(testcaseArray.get(0));
		//log.info(appobj);
		HttpSession session=request.getSession(false);
	    JSONObject user=(JSONObject) session.getAttribute("userInfo");
	       
		ManualTest manual=new ManualTest();
		boolean isExist = manual.storeManualtestInMangoDb(testcaseArray,testcaseName, appobj, user);
		if(isExist){
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
