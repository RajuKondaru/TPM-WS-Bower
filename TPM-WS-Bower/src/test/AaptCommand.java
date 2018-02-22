package test;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import com.tpm.mobile.android.utils.CommandPrompt;

public class AaptCommand {
	public static CommandPrompt cmd = null;
	public static void main1(String[] args) throws InterruptedException, IOException {
		// TODO Auto-generated method stub
		cmd = new CommandPrompt();
		String adbCommand ="C:\\AndroidSDK\\build-tools\\19.1.0\\aapt dump xmltree C:\\Users\\raju.kondaru\\Desktop\\apks\\Amazon.apk AndroidManifest.xml";
		List<String> results =cmd.runCommand(adbCommand);
		for (Iterator iterator = results.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			//System.out.println(string);
			if(string.contains("activity")){
				
				String[] activityInfo= string.split("=");
				System.out.println(activityInfo[1]);
				String[] activityInfo2 =activityInfo[1].split(" "); 
				String activity = activityInfo2[0].replaceAll("\"", "");
				if(!activity.contains(")")){
					//System.out.println(activity);
				}
				
				//return;
			}
			
		}
	}
	public static void main(String[] args) throws IOException {
		Runtime.getRuntime().exec("C:\\AndroidSDK\\platform-tools\\apktool.bat d C:\\Users\\raju.kondaru\\Desktop\\Amazon\\Ebay.apk", null, new File("C:\\Users\\raju.kondaru\\Desktop\\Amazon\\"));
	}

}
