package com.tpm.mobile.security.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

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
@WebFilter("/SessionFilter")
public class SessionFilter implements Filter {


	
	public void destroy() {
	}

	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		String requestedUri = request.getRequestURI();
	
		if(requestedUri.matches(".*(css|jpg|png|gif|js)")){
		    chain.doFilter(request, response);
		    return;
		}
		
	}

	public void init(FilterConfig config) throws ServletException {
		
		

		
	}
}