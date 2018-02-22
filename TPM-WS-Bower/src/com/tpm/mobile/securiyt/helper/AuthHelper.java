package com.tpm.mobile.securiyt.helper;
public class AuthHelper {
  public static boolean isAllowed(String username, String password) {
	  
    return username.contentEquals("admin@xyz.com") && password.contentEquals("password");
  }
}