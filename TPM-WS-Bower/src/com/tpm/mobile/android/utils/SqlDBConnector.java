package com.tpm.mobile.android.utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import org.apache.log4j.Logger;




public class SqlDBConnector {/*
	public static BasicDataSource dataSource;
	public static Connection con;

	private static PreparedStatement statement;
	private static ResultSet result;
	private static String dburl ;
	private static String dbuserName;
	private static String dbpassword;
	private static String dbdriverclass;
	

	static {
		Properties props = new Properties();
		try {
			props.load(SqlDBConnector.class.getClassLoader().getResourceAsStream("config.properties"));
			dburl = props.getProperty("dburl");
			dbuserName = props.getProperty("dbuname");
			dbpassword = props.getProperty("dbpassword");
			dbdriverclass = props.getProperty("dbdriverclass");
			
			// Creates a BasicDataSource
			dataSource = new BasicDataSource();
	 
			// Define Driver Class
			dataSource.setDriverClassName(dbdriverclass);
	 
			// Define Server URL
			dataSource.setUrl(dburl);
	 
			// Define Username
			dataSource.setUsername(dbuserName);
	 
			// Define Your Password
			dataSource.setPassword(dbpassword);
			
			//Set the connection pool size
			
			dataSource.setInitialSize(5);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
	public static Connection getConnection() throws SQLException {
		con=dataSource.getConnection();
		return con;
	}
	
	public static void main(String[] args) throws SQLException {
		con = SqlDBConnector.getConnection();
		
	
		statement = con.prepareStatement("select * from user where id=7");
	
		result = statement.executeQuery();
		while (result.next()) {
			System.out.println(result.getInt(1)); 
		}
	}

*/


	/** The log. */
	private static final Logger log = Logger.getLogger(SqlDBConnector.class);

	/** The con. */
	private static Connection con = null;

	/** The dbname. */
	private static  String dbname;

	/** The dbuname. */
	private static  String dbuname;

	/** The dbpassword. */
	private static  String dbpassword;

	static {
		Properties prop = new Properties();
		try {
			prop.load(DBConnection.class.getClassLoader().getResourceAsStream("config.properties"));
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
			//log.info(" dbname >> " + dbname + " >>dbuname >> " + dbuname+ "  >> dbpassword >>" + dbpassword);
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection(dbname, dbuname, dbpassword);
			
		} catch (Exception e) {
				log.error(e.toString());
		}
		return con;
	}

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String args[]) {
		try {
			SqlDBConnector.getConnection();
		} catch (Exception e) {
		
			log.error(e.toString());
		}
	}


}
