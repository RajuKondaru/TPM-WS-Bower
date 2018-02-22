package test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class VedioRecordTest {
	public static Process p;
	public static void main(String[] args) throws IOException, InterruptedException {
		
		Scanner scanner = new Scanner(new InputStreamReader(System.in));
        System.out.println("Reading input from console using Scanner in Java ");
        System.out.println("Please enter your device name: ");
        String deviceId = scanner.nextLine();
        System.out.println("User Input from console: " + deviceId);
        System.out.println("Please enter your file name: ");
        String vFileName = scanner.nextLine();
        System.out.println("User Input from console: " + vFileName);
        
        System.out.println("Give command to Start: ");
        String action = scanner.nextLine();
        System.out.println("Action input: " + action);


		String filelocation="/sdcard/"+vFileName+".mp4";
		String vedioRecord = "adb -s "+deviceId+" shell screenrecord "+filelocation;
		System.out.println("Test Video Recoring Starting.. "+vedioRecord);
		
		
		
		
		p = Runtime.getRuntime().exec(vedioRecord);
		/*ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/C", vedioRecord);
		builder.redirectErrorStream(true);
		Thread.sleep(1000);
		p = builder.start();
		*/
		System.out.println("Test Video Recoring Starting.. U can stop using commad stop :");
		action = scanner.nextLine();
	    System.out.println("Action input: " + action);
	    //builder.directory();
	    p.destroy();
	   // p.destroyForcibly();

	}

}
