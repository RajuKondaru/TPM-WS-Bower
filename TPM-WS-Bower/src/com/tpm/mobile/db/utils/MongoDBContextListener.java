package com.tpm.mobile.db.utils;
import java.net.UnknownHostException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.log4j.Logger;

import com.mongodb.MongoClient;


public class MongoDBContextListener implements ServletContextListener {
	private static final Logger log = Logger.getLogger(MongoDBContextListener.class);
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		MongoClient mongo = (MongoClient) sce.getServletContext()
							.getAttribute("MONGO_CLIENT");
		mongo.close();
		log.info("MongoClient closed successfully");
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try {
			ServletContext ctx = sce.getServletContext();
			MongoClient mongo = new MongoClient(
					ctx.getInitParameter("MONGODB_HOST"), 
					Integer.parseInt(ctx.getInitParameter("MONGODB_PORT")));
			log.info("MongoClient initialized successfully");
			sce.getServletContext().setAttribute("MONGO_CLIENT", mongo);
		} catch (UnknownHostException e) {
			throw new RuntimeException("MongoClient init failed");
		}
	}

}