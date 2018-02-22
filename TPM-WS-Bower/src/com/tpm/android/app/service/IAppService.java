package com.tpm.android.app.service;

import java.io.IOException;
import com.mongodb.MongoException;
import com.tpm.mobile.android.pojo.AppPojo;

public interface IAppService {
	public boolean appInstallService(String deviceId,  AppPojo app);
	public boolean appLaunchService( String deviceId, AppPojo app) ;
	public boolean appUnInstallService(String deviceId,  AppPojo app);
	public AppPojo getAppInfoService(  AppPojo app) throws IOException;

	public void deleteAppFromTemp(AppPojo app);
	public boolean getAppFromMongoDB(AppPojo app) throws NumberFormatException, MongoException, IOException ;
	public void saveAppInMongoDB(String filename)throws NumberFormatException, MongoException, IOException ;
}
