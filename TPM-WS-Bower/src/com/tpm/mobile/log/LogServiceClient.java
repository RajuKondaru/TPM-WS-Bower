package com.tpm.mobile.log;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Properties;

import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

/**
 * The Class LogServiceClient.
 */
public class LogServiceClient {
	private static final Logger log = Logger.getLogger(LogServiceClient.class);
	/** The url. */
	public static String url = null;

	/** The user authurl. */
	public static String userAuthurl = null;
	static {
		Properties prop = new Properties();
		try {
			prop.load(LogServiceClient.class.getClassLoader()
					.getResourceAsStream("config.properties"));
			url = prop.getProperty("logServiceurl");
			if (prop.getProperty("logServiceurl") != null
					&& !"".equals(prop.getProperty("logServiceurl")))
				url = prop.getProperty("logServiceurl");

			if (prop.getProperty("userAuthurl") != null
					&& !"".equals(prop.getProperty("userAuthurl")))
				userAuthurl = prop.getProperty("userAuthurl");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Savelogdetails.
	 * 
	 * @param msg
	 *            the msg
	 * @param exceptionmsg
	 *            the exceptionmsg
	 * @param type
	 *            the type
	 */
	@SuppressWarnings("deprecation")
	public void savelogdetails(String msg, String exceptionmsg, String type) {
		try {
			ClientConfig config = new DefaultClientConfig();
			Client client = Client.create(config);
			// System.out.println("save requ >>> "+url+"?msg="+msg+"&exceptionmsg="+exceptionmsg+"&type="+type);
			WebResource service = client.resource(url + "?msg="
					+ URLEncoder.encode(msg) + "&exceptionmsg="
					+ URLEncoder.encode(exceptionmsg) + "&type=" + type);
			ClientResponse response = service.accept(MediaType.TEXT_PLAIN).get(
					ClientResponse.class);
			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatus());
			} else {
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Check user authentication.
	 * 
	 * @param userid
	 *            the userid
	 * @return the string
	 */
	@SuppressWarnings("deprecation")
	public String checkUserAuthentication(String userid) {
		String status = null;
		try {
			log.info("userid :: " + userid);
			log.info("userAuthurl :: " + userAuthurl);
			ClientConfig config = new DefaultClientConfig();
			Client client = Client.create(config);
			WebResource service = client.resource(userAuthurl + "?userid="
					+ URLEncoder.encode(userid));
			ClientResponse response = service.accept(MediaType.TEXT_PLAIN).get(
					ClientResponse.class);
			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatus());
			} else {
				status = response.getEntity(String.class);
				log.info("status>>>>>>" + status);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

}
