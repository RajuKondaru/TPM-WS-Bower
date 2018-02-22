package com.tpm.mobile.manual.dao;



import org.json.simple.JSONArray;
import org.json.simple.JSONObject;



public interface IManualTest {
	public boolean storeManualtestInMangoDb(JSONArray test, String testName, JSONObject appId, JSONObject user);
	public boolean updateManualtestInMangoDbWithResult(JSONArray test, String testName, JSONObject appId, String deviceId, JSONObject user);
	public JSONObject getManualtestResultFromMangoDb(String testName, JSONObject appId, String deviceId, JSONObject user);
	public void checkManualtestNameInMangoDb(String testName, JSONObject appId, JSONObject user);
	public boolean storeManualTestResultsInMangoDb(JSONObject results, JSONObject user);
	

}
