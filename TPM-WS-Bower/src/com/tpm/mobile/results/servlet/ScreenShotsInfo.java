package com.tpm.mobile.results.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.tpm.mobile.results.dao.ExecResults;

/**
 * Servlet implementation class VideoStream
 */
@WebServlet("/getScreenShotsInfo")
public class ScreenShotsInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(ScreenShotsInfo.class);
   

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
		String sessionId = obj.get("sessionId").toString();
		
		log.info(sessionId);
		ExecResults testResult=new ExecResults();
		JSONObject result = testResult.getScreenShotInfo(sessionId);
		String jsonString=null;
		if(!result.isEmpty()){
			Gson gson = new Gson();
			jsonString = gson.toJson(result);
		}
		
		if(jsonString!=null){
			
			pw.print(jsonString);
			log.info(jsonString);
		}else {
			log.info("failed");
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
