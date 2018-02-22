package com.tpm.mobile.results.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.tpm.mobile.android.exe.api.appium.utils.AppiumProxy;
import com.tpm.mobile.results.dao.ExecResults;

/**
 * Servlet implementation class VideoStream
 */
@WebServlet("/LogStream")
public class LogStream extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(LogStream.class);
	
   

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json"); 
		response.setCharacterEncoding("UTF-8");
	    ServletOutputStream out;  
	    out = response.getOutputStream();  

	    String testName=request.getParameter("testName");
	    String logType=request.getParameter("logType");
	    String deviceId=request.getParameter("deviceId");
	    log.info(testName);
	    log.info(logType);
	    log.info(deviceId);
	    ExecResults exResults=new ExecResults();
		InputStream vStream = exResults.getLogsAsStream(testName, logType, deviceId);
	    BufferedInputStream bin = new BufferedInputStream(vStream);  
	    BufferedOutputStream bout = new BufferedOutputStream(out);  
	    int ch =0; ;  
	    while((ch=bin.read())!=-1)  
	    {  
	      bout.write(ch);  
	    }  
	      
	    bin.close();  
	    vStream.close();  
	    bout.close();  
	    out.close();  
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
