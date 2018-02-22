package com.tpm.mobile.android.pojo;

import java.util.Date;
import java.util.List;

public class AppPojo {
	private int id;
	private String appName;
	private String fileName;
	
	private String filePath;
	private String fileType;
	private String version;
	private String appPackageName;
	private String activityName;
	private String appType;
	private Date uploadedDate;
	private String userId;
	private String projectId;
	private String downloadedAppPath;
	private String iconPath;
	private String icon;
	private String status;
	private String buildId;
	
	private String webUrl;
	private String webName;
	private String webVersion;
	private String defaultIcon;
	private List<String> appTestSuites;
	private int autId;
	
	public List<String> getAppTestSuites() {
		return appTestSuites;
	}
	public void setAppTestSuites(List<String> appTestSuites) {
		this.appTestSuites = appTestSuites;
	}
	public String getWebUrl() {
		return webUrl;
	}
	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}
	public String getWebName() {
		return webName;
	}
	public void setWebName(String webName) {
		this.webName = webName;
	}
	public String getWebVersion() {
		return webVersion;
	}
	public void setWebVersion(String webVersion) {
		this.webVersion = webVersion;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getBuildId() {
		return buildId;
	}
	public void setBuildId(String buildId) {
		this.buildId = buildId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getAppPackageName() {
		return appPackageName;
	}
	public void setAppPackageName(String appPackageName) {
		this.appPackageName = appPackageName;
	}
	
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	public String getAppType() {
		return appType;
	}
	public void setAppType(String appType) {
		this.appType = appType;
	}
	public Date getUploadedDate() {
		return uploadedDate;
	}
	public void setUploadedDate(Date uploadedDate) {
		this.uploadedDate = uploadedDate;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getDownloadedAppPath() {
		return downloadedAppPath;
	}
	public void setDownloadedAppPath(String downloadedAppPath) {
		this.downloadedAppPath = downloadedAppPath;
	}
	public String getIconPath() {
		return iconPath;
	}
	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}
	public String getDefaultIcon() {
		return defaultIcon;
	}
	public void setDefaultIcon(String defaultIcon) {
		this.defaultIcon = defaultIcon;
	}
	public int getAutId() {
		return autId;
	}
	public void setAutId(int autId) {
		this.autId = autId;
	}
	
	
	
}
