package com.tpm.mobile.android.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.log4j.Logger;

public class DeviceMoniter {
	private static final Logger log = Logger.getLogger(DeviceMoniter.class);
	public static boolean isStop= false;
	public static String[] getAdbLogCat(String device) {

	    try {
	        Process p = Runtime.getRuntime().exec("adb -s "+device+" shell logcat");
	        InputStream is = p.getInputStream();
	        InputStreamReader isr = new InputStreamReader(is);
	        BufferedReader br = new BufferedReader(isr);

	        final StringBuffer output = new StringBuffer();
	        String line;
	        ArrayList<String> arrList = new ArrayList<String>();
	        while ((line = br.readLine()) != null) {
	           log.info(line);
	            if(isStop){
	            	break;
	            }
	        }
	        return (String[])arrList.toArray(new String[0]);
	    } catch (IOException e) {
	        log.error(e);
	        
	        return new String[]{};
	    }
	}
}
