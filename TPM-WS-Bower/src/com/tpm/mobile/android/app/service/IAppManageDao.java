package com.tpm.mobile.android.app.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.json.simple.JSONObject;

import com.tpm.mobile.android.pojo.AppPojo;

public interface IAppManageDao {
		
	
	public boolean storeAppInMangoDB(AppPojo app, String user)  throws ParseException;
	public void storeWebAppInMangoDB(AppPojo app, String user)  throws IOException;
	public List<AppPojo> getAppsInfoFromMangoDB(String user)  throws ParseException;
	public AppPojo getAppInfoFromMangoDB(AppPojo app, String user)  throws  ParseException, IOException ;
	public boolean updateAppInfoFromMangoDB(AppPojo app, String user)  throws ParseException;
	public AppPojo storeRobotiumAUTAppInMangoDB(AppPojo app, String user)  throws ParseException;
	
	public AppPojo getAppAUTInfoFromMangoDB(AppPojo app, String user) throws ParseException, IOException;
	public JSONObject getAppInfoByNameFromMongoDB( String  appName,  String user) throws IOException;
	
	public boolean deleteAppFromMangoDB(AppPojo app, String user)  throws ParseException;
}
