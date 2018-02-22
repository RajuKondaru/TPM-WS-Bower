package com.tpm.mobile.security.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

@WebFilter("/*")
public class LoginFilter implements Filter {
	private static final Logger log = Logger.getLogger(LoginFilter.class);
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws ServletException, IOException {    
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);
        String loginURI = request.getContextPath() + "/";
       
        boolean resourceRequest=false;
        String reqUri=request.getRequestURI();
        //log.info("reqUri :: "+reqUri);
        if( reqUri.contains("ws/hub") || reqUri.contains(".js") ||  reqUri.contains(".css") ||  reqUri.contains(".png") ||  reqUri.contains(".html") || reqUri.contains("login") ||  reqUri.contains("logout") 
        		|| reqUri.contains("register") ||reqUri.contains("forgotPassword") || reqUri.contains("account") || reqUri.contains("index") || reqUri.contains("ConfigDevices") || reqUri.contains("tpwsendpoint") ){
        	resourceRequest=true;
        }
       //excludeURLs.add("/tpwsendpoint");
        boolean loggedIn = session != null && session.getAttribute("userInfo") != null;
        boolean loginRequest = request.getRequestURI().equals(loginURI);
       
        //log.info("loggedIn :: "+loggedIn +" loginRequest ::"+loginRequest +" resourceRequest ::"+resourceRequest  ) ;
        if (loggedIn || loginRequest || resourceRequest) {
            chain.doFilter(request, response);
        } else   {
        	//request.getRequestDispatcher("./index.html").forward(request, response);
        	response.sendRedirect("./");
        }
    }

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

   
}