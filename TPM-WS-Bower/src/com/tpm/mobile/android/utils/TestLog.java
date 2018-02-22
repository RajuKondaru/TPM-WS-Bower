package com.tpm.mobile.android.utils;

import org.apache.log4j.Logger;

/**
 * The Class TestLog.
 */
public class TestLog {

	/** The log. */
	public static Logger log = Logger.getLogger(TestLog.class);

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {

		log.info("objectspyinfo");
		log.error("objectspyerror");

	}

}
