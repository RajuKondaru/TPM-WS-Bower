package test;

import java.io.File;
import java.io.IOException;

public class ZipUtilityTest {
	
	/*public static void main(String[] args) throws IOException {
		String targetFilePath="C:\\Users\\raju.kondaru\\AppData\\Local\\Temp\\Snapdeal.apk";
		ZipUtility zip=new ZipUtility();
		File change = zip.changeExt(targetFilePath, ".zip", ".apk");
		 String[] exts={".RSA",".SF",".MF"};
         try {
        	 zip.deleteZipEntry(change,exts);
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        zip.changeExt(change.getAbsolutePath(), ".apk", ".zip");
	}*/
	public static void main(String[] args) throws IOException {
		String targetFilePath="C:\\Users\\raju.kondaru\\AppData\\Local\\Temp\\AbhibusMainActivityTest.apk";
		ZipUtility zip=new ZipUtility();
		File zipFile = zip.changeExtenion(targetFilePath, ".zip", ".apk");
		String[] exts={"META-INF/"};
		File temp = null;
	    try {
	    	temp = zip.deleteZipEntry(zipFile,exts);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	    File apkFile = zip.changeExtenion(temp.getAbsolutePath(), ".apk", ".tmp");
	    temp.delete();
	}
	

}
