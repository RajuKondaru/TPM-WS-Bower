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
import org.json.simple.parser.ParseException;

import com.tpm.mobile.securiyt.helper.RegisterHelper;

/**
 * Servlet implementation class DashBoardServlet
 */

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
	/**
	 * 
	 */
	private RegisterHelper regHelper= null;
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(LogoutServlet.class);

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//response.sendRedirect(request.getContextPath() + "/");
		log.info("Logout Request");
		//request.getRequestDispatcher(request.getContextPath() + "/").forward(request, response);
		PrintWriter out= response.getWriter();
		HttpSession session=request.getSession();
		
		JSONObject user = (JSONObject)session.getAttribute("userInfo");
		regHelper= new RegisterHelper();
		JSONObject updateUser = null;
		
		if((boolean)user.get("login")==true) {
			session.invalidate();
	        //response.sendRedirect("./");
			try {
				updateUser = regHelper.updateLoginStaus(user);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        out.write("success");
	        log.info("Logout Successfully!");
		} else {
			  out.write("failed");
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
