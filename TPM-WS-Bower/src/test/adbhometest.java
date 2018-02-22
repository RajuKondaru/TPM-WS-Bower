package test;

public class adbhometest {
	static String adbLocation = System
			.getProperty("com.android.screenshot.bindir");
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if(adbLocation==null){
			adbLocation = System.getenv("ANDROID_HOME");
		}
	
		System.out.println(adbLocation);

	}

}
