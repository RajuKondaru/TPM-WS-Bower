package com.tpm.mobile.android.exe.api.robotium.dao;

import java.io.InputStream;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.tpm.mobile.android.pojo.ResultPojo;

public interface IRoboTestDao {
	public boolean storeRoboTestInMangoDb(JSONArray test, String testName, JSONObject appId, String user);
	public boolean updateRoboTestInMangoDbWithResult(JSONArray test, String testName, JSONObject appId, String deviceId, String user);
	public JSONObject getRoboTestInfoFromMangoDb(String testName, JSONObject appId, String deviceId, String user);
	public void checkRoboTestNameInMangoDb(String testName, JSONObject appId, String user);
	public boolean storeRoboTestResultsInMangoDb(JSONObject results, String user);
	public ResultPojo getRoboTestResultFromMangoDb(String testName, JSONObject appId, String deviceId, String user);
	
	

}
