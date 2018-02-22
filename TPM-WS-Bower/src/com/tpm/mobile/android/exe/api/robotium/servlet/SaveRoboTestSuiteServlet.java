package com.tpm.mobile.android.exe.api.robotium.servlet;

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

import com.mongodb.MongoClient;
import com.tpm.mobile.android.exe.api.robotium.dao.RoboTestDao;

/**
 * Servlet implementation class SaveWriteManualTestCaseServlet
 */
@WebServlet("/saveRoboTestSuiteServlet")
public class SaveRoboTestSuiteServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(SaveRoboTestSuiteServlet.class);
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
		String testname = obj.get("testname").toString();
		testname=testname.replaceAll("^\"|\"$", "");
		JSONArray test = (JSONArray) obj.get("test");
		JSONObject app =(JSONObject) obj.get("app");
		//log.info(test.get(0));
		//log.info(app);
		//log.info(testname);
		HttpSession session=request.getSession(false);
	    JSONObject user=(JSONObject) session.getAttribute("userInfo");
		RoboTestDao robo=new RoboTestDao();
		boolean isExist = robo.storeRoboTestInMangoDb(test,testname, app, user.get("userId").toString());
		
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
