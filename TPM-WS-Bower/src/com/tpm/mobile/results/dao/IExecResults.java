package com.tpm.mobile.results.dao;

import java.io.InputStream;
import java.util.List;

import org.json.simple.JSONObject;

public interface IExecResults {
	public List<JSONObject>  getAllResults(JSONObject user);
	public JSONObject  getResult(String testName, JSONObject app, String deviceId, JSONObject user);
	public InputStream getVideoAsStream(String filename, JSONObject user);
	public InputStream getScreenShotAsStream(String filename);
	public InputStream getLogsAsStream(String testName, String logType, String deviceId);
	public JSONObject getScreenShotInfo(String session);
}
