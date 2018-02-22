package com.tpm.mobile.security.filter;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Servlet Filter implementation class MyFilter
 */
@WebFilter("/test")
public class AuthenticationFilter implements Filter {
	ArrayList<String> excludeURLs= null;
    /**
     * Default constructor. 
     */
    public AuthenticationFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		
		HttpServletRequest httpReq = (HttpServletRequest) request;
	    HttpSession session = httpReq.getSession(false); // if the fail login it doesnt create a session
	    String path= httpReq.getRequestURI();
	    if(path.endsWith(".css") || path.endsWith(".js")||path.endsWith(".png") ){
	      chain.doFilter(request,response);
	      return;
	    }
	    if (session != null && session.getAttribute("userInfo") == null && !excludeURLs.contains(httpReq.getServletPath()) ){
	    	session.invalidate();
	        RequestDispatcher rd = httpReq.getRequestDispatcher("index.html");
	        rd.forward(request, response);
	        return;
	    } else if(session != null && session.getAttribute("userInfo") != null && httpReq.getRequestURI().contains("logout")) {
	    	session.invalidate();
		} 

	    chain.doFilter(request, response);
		
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
		
		 	excludeURLs= new ArrayList<String>();
	        //Pages/Servlet
	        excludeURLs.add("/login");
	        excludeURLs.add("/login.html");
	        excludeURLs.add("/home.html");
	        excludeURLs.add("/index");
	        excludeURLs.add("/index.html");
	        excludeURLs.add("/about");
	        excludeURLs.add("/about.html");
	        excludeURLs.add("/register");
	        excludeURLs.add("/signup.html");
	        excludeURLs.add("/tpwsendpoint");
	        //Images
	        excludeURLs.add("/images");
	        //css files
	        excludeURLs.add("/css");
	        // JavaScript files
	        excludeURLs.add("/js");
	}

}
