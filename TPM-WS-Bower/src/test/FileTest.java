package test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileTest {
	public static void main(String[] args) throws IOException {
		
		File actfile = new File("C:\\Users\\RAJU~1.KON\\AppData\\Local\\Temp\\AbhibusMainActivityTest.apk");
		String dicName=actfile.getName().substring(0, actfile.getName().indexOf("."));
		File recopyfile = new File("C:\\Users\\RAJU~1.KON\\AppData\\Local\\Temp\\"+dicName);
        if (!recopyfile.exists()) {
        	
        	if (recopyfile.mkdir()) {
                System.out.println("Directory is created!");
                
            } else {
                System.out.println("Failed to create directory!");
            }
            
        }else{
        	
        }
        if(recopyfile.isDirectory()){
        	 Path temp = Files.copy
                     (Paths.get(actfile.getAbsolutePath()), 
                     Paths.get(recopyfile.getAbsolutePath()+"\\"+dicName+".zip"));
              
                     if(temp != null)
                     {
                         System.out.println("File renamed and moved successfully");
                         
                         
                         
                     }
                     else
                     {
                         System.out.println("Failed to move the file");
                     }
    	}else{
    		
    	}
       
	}
}
