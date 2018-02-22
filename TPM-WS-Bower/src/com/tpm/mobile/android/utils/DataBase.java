package com.tpm.mobile.android.utils;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

public class DataBase {
	/** The log. */
	private static final Logger log = Logger.getLogger(DataBase.class);
	
	private DataSource dataSource;

	public DataBase(String jndiname) {
	    try {
	    	
	        dataSource = (DataSource) new InitialContext().lookup("java:comp/env/" + jndiname);
	    } catch (NamingException e) {
	        // Handle error that it's not configured in JNDI.
	    	log.error(jndiname + " is missing in JNDI!", e);
	        throw new IllegalStateException(jndiname + " is missing in JNDI!", e);
	    }
	}

	public Connection getConnection() throws SQLException {
	    return dataSource.getConnection();
	}

}
