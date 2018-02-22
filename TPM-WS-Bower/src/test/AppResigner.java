package test;

import java.io.IOException;

import com.tpm.mobile.android.app.service.AppManageService;
import com.tpm.mobile.android.pojo.AppPojo;

public class AppResigner {
	public static AppManageService appSer=null;
	public static void main(String[] args) throws IOException {
		appSer = new AppManageService();
		AppPojo app= new AppPojo();
		String appLoaction="C:\\Users\\raju.kondaru\\Desktop\\apks\\Amazon\\Jabong.apk";
		app.setDownloadedAppPath(appLoaction);
		boolean isResigned = appSer.appResignService(app);
		System.out.println(isResigned);
	}

}
