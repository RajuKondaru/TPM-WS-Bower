package com.tpm.mobile.db.utils;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DataBase {
	private DataSource dataSource;

	public DataBase(String jndiname) {
	    try {
	        dataSource = (DataSource) new InitialContext().lookup("java:comp/env/" + jndiname);
	    } catch (NamingException e) {
	        // Handle error that it's not configured in JNDI.
	        throw new IllegalStateException(jndiname + " is missing in JNDI!", e);
	    }
	}

	public Connection getConnection() throws SQLException {
	    return dataSource.getConnection();
	}

}
