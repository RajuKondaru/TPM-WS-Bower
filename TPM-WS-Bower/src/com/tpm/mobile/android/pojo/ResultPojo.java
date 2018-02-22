package com.tpm.mobile.android.pojo;

import java.util.Date;

import org.json.simple.JSONObject;

public class ResultPojo {
	private String testType;
	private String testName;
	private Date UploadTime;
	private JSONObject logs;
	private String AppId;
	private String UserID;
	private String filename;
	private String Deviceid;
	private String testSenario;
	public String getTestType() {
		return testType;
	}
	public void setTestType(String testType) {
		this.testType = testType;
	}
	public String getTestName() {
		return testName;
	}
	public void setTestName(String testName) {
		this.testName = testName;
	}
	public Date getUploadTime() {
		return UploadTime;
	}
	public void setUploadTime(Date uploadTime) {
		UploadTime = uploadTime;
	}
	public JSONObject getLogs() {
		return logs;
	}
	public void setLogs(JSONObject logs) {
		this.logs = logs;
	}
	public String getAppId() {
		return AppId;
	}
	public void setAppId(String appId) {
		AppId = appId;
	}
	public String getUserID() {
		return UserID;
	}
	public void setUserID(String userID) {
		UserID = userID;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getDeviceid() {
		return Deviceid;
	}
	public void setDeviceid(String deviceid) {
		Deviceid = deviceid;
	}
	public String getTestSenario() {
		return testSenario;
	}
	public void setTestSenario(String testSenario) {
		this.testSenario = testSenario;
	}
	

}
