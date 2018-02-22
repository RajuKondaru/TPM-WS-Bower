package test;

import java.io.IOException;

import org.apache.commons.lang3.SystemUtils;

public class AppUnsignTest {
	
	public static void main(String[] args) throws IOException {
		System.out.println(SystemUtils.getUserDir());
		
		System.out.println(System.getProperty("catalina.base"));
		/*AppPojo app = new AppPojo();
		app.setDownloadedAppPath("C:/Users/raju.kondaru/AppData/Local/Temp/abhibusnew.apk");
		AppManageService  appSer = new AppManageService();
		boolean isUnsigned = appSer.appUnsignService(app);
		if(isUnsigned){
			boolean isResigned = appSer.appResignService(app);
		}*/

	}

}
