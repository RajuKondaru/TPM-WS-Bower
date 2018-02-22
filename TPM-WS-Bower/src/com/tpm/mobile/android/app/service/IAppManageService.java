package com.tpm.mobile.android.app.service;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.tpm.mobile.android.pojo.AppPojo;

public interface IAppManageService {
	public boolean appInstallService(String deviceId,  AppPojo app);
	public boolean appLaunchService( String deviceId, AppPojo app) ;
	public boolean appUnInstallService(String deviceId,  AppPojo app);
	public AppPojo getAppInfoService(  AppPojo app) throws IOException;
	public boolean appAUTInstallService(String deviceId, AppPojo app);
	public AppPojo getMultipartDataFromRequest(HttpServletRequest request, AppPojo app);
	public AppPojo getFileFromRequest(HttpServletRequest request, AppPojo app) throws IOException, ServletException;
	public void deleteAppFromTemp(AppPojo app);
	public boolean browserLaunchService( String deviceId, AppPojo app) ;
	public boolean browserCleanUpService( String deviceId) ;
	public boolean appUnsignService( AppPojo app);
	public boolean appResignService( AppPojo app);
	
	
}
