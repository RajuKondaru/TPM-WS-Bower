package com.tpm.mobile.db.utils;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;

public class SqlDBConnector {
	static BasicDataSource dataSource;
	static Connection con;
	static {
		// Creates a BasicDataSource
				dataSource = new BasicDataSource();
		 
				// Define Driver Class
				dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		 
				// Define Server URL
				dataSource.setUrl("jdbc:mysql://localhost:3306/mobiledb");
		 
				// Define Username
				dataSource.setUsername("");
		 
				// Define Your Password
				dataSource.setPassword("");
				
				//Set the connection pool size
				
				dataSource.setInitialSize(5);

	}
	private static Connection getDBConnection() throws SQLException {
		con=dataSource.getConnection();
		return con;
	}
	public static void main(String[] args) throws SQLException {
		SqlDBConnector.getDBConnection();
	}
}
