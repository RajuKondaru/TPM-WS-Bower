package com.tpm.mobile.android.exe.api.robotium.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.mongodb.MongoClient;
import com.tpm.mobile.android.exe.api.robotium.dao.RoboTestDao;
import com.tpm.mobile.android.exe.api.robotium.utils.InstrumentProxy;



/**
 * Servlet implementation class InstrumentExecuter
 */
@WebServlet("/InstrumentExecuter")
public class InstrumentExecuter extends HttpServlet {
	private static final Logger log = Logger.getLogger(InstrumentExecuter.class);
	private static final long serialVersionUID = 1L;
	private boolean isSuccess=false;
	 /**
     * @see HttpServlet#HttpServlet()
     */
    public InstrumentExecuter() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
       
		PrintWriter out = response.getWriter();
		JSONParser parser = new JSONParser();
		JSONObject obj = null;
		try {
			obj = (JSONObject) parser.parse(request.getReader());
		} catch (ParseException e) {
			log.error(e);
		}
		InstrumentProxy proxy= new InstrumentProxy();
		JSONObject resultJson =proxy.start(obj, null);
		HttpSession session=request.getSession(false);
	    JSONObject user=(JSONObject) session.getAttribute("userInfo");
	    RoboTestDao results= new RoboTestDao();
		isSuccess = results.storeRoboTestResultsInMangoDb( resultJson, user.get("userId").toString());
	    if(isSuccess){
            out.write("Success");
        } else {
        	out.write("Failed");
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
