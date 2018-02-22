package com.tpm.mobile.security.servlet;

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

import com.google.gson.Gson;
import com.tpm.mobile.securiyt.helper.RegisterHelper;

/**
 * Servlet implementation class AccountServlet
 */
@WebServlet("/account")
public class AccountServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private RegisterHelper regHelper= null;
	private static final Logger log = Logger.getLogger(AccountServlet.class);

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			PrintWriter out= response.getWriter();
			JSONParser parser = new JSONParser();
			JSONObject obj = null;
			try {
				obj = (JSONObject) parser.parse(request.getReader());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			regHelper= new RegisterHelper();
			JSONObject userInfo=regHelper.isAuthenticated(obj);
			
			if( userInfo!=null && !userInfo.isEmpty() && (boolean)userInfo.get("login")==false){
				HttpSession session=request.getSession();
				JSONObject updateUser = null;
				try {
					updateUser = regHelper.updateLoginStaus(userInfo);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				session.setAttribute("userInfo", updateUser);
				//log.info(session.getAttribute("userInfo").toString());
				Gson gson = new Gson();
				String jsonString = gson.toJson(updateUser);
				
				out.write(jsonString);
				
			} else if(userInfo!=null && !userInfo.isEmpty() && (boolean)userInfo.get("login")==true) {
				out.write("already logged in");
			} else {
				out.write("falied");
			}
		
	}

}
