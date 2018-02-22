package com.tpm.mobile.android.pojo;

import org.openqa.selenium.WebElement;

import com.tpm.mobile.log.LogServiceClient;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;

public class MobileConfigs {
	
	private String deviceName;
	private String userId;
	private String browserName;
	private String projectId;
	private String platformName;
	private String platformVersion;
	private String testURL;
	private String scriptURL;
	private AndroidDriver<WebElement> driver;
	private LogServiceClient loClient;
	private AppiumDriverLocalService service;
	private String  objectSpyWebURL;
	
	
	public String getObjectSpyWebURL() {
		return objectSpyWebURL;
	}
	public void setObjectSpyWebURL(String objectSpyWebURL) {
		this.objectSpyWebURL = objectSpyWebURL;
	}
	public AndroidDriver<WebElement> getDriver() {
		return driver;
	}
	public void setDriver(AndroidDriver<WebElement> driver) {
		this.driver = driver;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getPlatformName() {
		return platformName;
	}
	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}
	public String getPlatformVersion() {
		return platformVersion;
	}
	public void setPlatformVersion(String platformVersion) {
		this.platformVersion = platformVersion;
	}
	
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getBrowserName() {
		return browserName;
	}
	public void setBrowserName(String browserName) {
		this.browserName = browserName;
	}
	public String getTestURL() {
		return testURL;
	}
	public void setTestURL(String testURL) {
		this.testURL = testURL;
	}
	public String getScriptURL() {
		return scriptURL;
	}
	public void setScriptURL(String scriptURL) {
		this.scriptURL = scriptURL;
	}
	public LogServiceClient getLoClient() {
		return loClient;
	}
	public void setLoClient(LogServiceClient loClient) {
		this.loClient = loClient;
	}
	public AppiumDriverLocalService getService() {
		return service;
	}
	public void setService(AppiumDriverLocalService service) {
		this.service = service;
	}
	
	

}
