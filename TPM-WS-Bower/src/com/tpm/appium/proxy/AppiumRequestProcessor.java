package com.tpm.appium.proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONObject;

public class AppiumRequestProcessor {
	
	public static String requstProcess(StringBuffer reqUrl, JSONObject jObj) {
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpResponse httpRes= null;
		String res = null;
		try {

		    HttpPost httpReq = new HttpPost(reqUrl.toString());
		   
		    httpReq.addHeader("content-type", "application/json");
		    httpReq.setEntity(new StringEntity(jObj.toJSONString()));
		    httpRes = httpClient.execute(httpReq);

		    //handle response here...

		}catch (Exception ex) {

		    //handle exception here

		} finally {
		    //Deprecated
		    //httpClient.getConnectionManager().shutdown(); 
		}
		
		try {
			HttpEntity entity = httpRes.getEntity();
			System.out.println(httpRes.getStatusLine());
			if (entity != null) {

	            // A Simple JSON Response Read
	            InputStream instream = entity.getContent();
	            res = convertStreamToString(instream);
	            // now you have the string representation of the HTML request
	            //System.out.println("RESPONSE: " + res);
	            instream.close();
	           

	        }
		}catch (Exception e) {
			// TODO: handle exception
		}
		return res;
	}
	private static String convertStreamToString(InputStream is) {

	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    StringBuilder sb = new StringBuilder();

	    String line = null;
	    try {
	        while ((line = reader.readLine()) != null) {
	            sb.append(line + "\n");
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            is.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    return sb.toString();
	}

}
