package com.tpm.mobile.android.utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 * The Class DBConnection.
 */
public class DBConnection {

	/** The log. */
	private static final Logger log = Logger.getLogger(DBConnection.class);

	/** The con. */
	private static Connection con = null;

	/** The dbname. */
	private static  String dbname;

	/** The dbuname. */
	private static  String dbuname;

	/** The dbpassword. */
	private static  String dbpassword;

	

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String args[]) {
		try {
			DBConnection.getConnection();
		} catch (Exception e) {
		
			log.error(e.toString());
		}
	}

	

	static {
		Properties prop = new Properties();
		try {
			prop.load(DBConnection.class.getClassLoader().getResourceAsStream(		"config.properties"));
			dbname = prop.getProperty("dburl");
			dbuname = prop.getProperty("dbuname");
			dbpassword = prop.getProperty("dbpassword");
		} catch (IOException e) {
			log.error(e.toString());
		}
	}

	/**
	 * Get connection.
	 * 
	 * @return Connection, if successful
	 */
	public static Connection getConnection() {
		try {
			log.info(" dbname >> " + dbname + " >>dbuname >> " + dbuname+ "  >> dbpassword >>" + dbpassword);
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection(dbname, dbuname, dbpassword);
			
		} catch (Exception e) {
				log.error(e.toString());
		}
		return con;
	}

	
}