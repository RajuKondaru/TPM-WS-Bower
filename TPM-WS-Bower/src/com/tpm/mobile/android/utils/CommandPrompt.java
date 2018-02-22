package com.tpm.mobile.android.utils;
/**
 * Command Prompt - this class contains method to run windows and mac commands  
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class CommandPrompt {
	private static final Logger log = Logger.getLogger(CommandPrompt.class);
	Process p;
	ProcessBuilder builder;
	
	/**
	 * This method run command on windows and mac
	 * @param command to run  
	 */
	public List<String> runCommand(String[] command) throws InterruptedException, IOException {
		String os = System.getProperty("os.name");
		log.info(os);
		
		// build cmd proccess according to os
		if(os.contains("windows")) // if windows
		{
			//log.info(command);
			builder = new ProcessBuilder( command);
			Map<String, String> env = builder.environment();
	        env.put("PGPASSWORD", "mypass");
	        builder.redirectErrorStream(true);
			Thread.sleep(1000);
			p = builder.start();
			log.info(command);
			
			
		} else  { // If Mac //if(os.contains("Windows"))
			log.info(command);
			p = Runtime.getRuntime().exec(command);
		}
		// get std output
		BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line="";
		List<String> lineList = new ArrayList<String>();
		while((line=r.readLine()) != null){
			if(!line.isEmpty() & !line.contains("package:")){
				//log.info(line);
			}
			lineList.add(line);
			if(line.contains("Console LogLevel: debug"))
				break;
			
		}
		if(p!=null){
			p.destroy();
		}
		
		r.close();
	
		return lineList;
	}
	/**
	 * This method run command on windows and mac
	 * @param command to run  
	 */
	public List<String> runCommand(String command) throws InterruptedException, IOException {
		List<String> lineList = null;
		BufferedReader r=null;
		try{
			
		
			String os = System.getProperty("os.name");
			//log.info(os);
			
			// build cmd proccess according to os
			if(os.contains("Windows")) // if windows
			{
				builder = new ProcessBuilder("cmd.exe", "/C", command);
				//log.info(command);
				builder.redirectErrorStream(true);
				Thread.sleep(1000);
				p = builder.start();
			} else if(os.contains("Mac")) { // If Mac 
				p = Runtime.getRuntime().exec(command, null);
				//log.info(command);
			}
			// get std output
			r = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line="";
			lineList = new ArrayList<String>();
			line=r.readLine();
			int count=1;
			while(line != null){
				count++;
				if(!line.isEmpty() ){
					lineList.add(line);
					
				}
				if(line.contains("Console LogLevel: debug")) {
					break;
				}
				
				if(line.contains("** No activities found to run, monkey aborted.") ){
					lineList=null;
					break;	
				}
					
				
				if(command.contains("/data/data/com.vmlite.vncserver/files/vmlitevncserver")){
					if(count>7) {
						break;
					}
				}
				line=r.readLine();
			
			}
			
		} catch(Exception e){
			e.printStackTrace();
		} finally {
			if(p!=null){
				p.destroy();
			}
			if(r!=null){
				r.close();
			}
		}
	
		return lineList;
	}
	
	
	/**
	 * This method run command on windows and mac
	 * @param command to run  
	 */
	public String runLogCommand(String command) throws InterruptedException, IOException {
		String os = System.getProperty("os.name");
		//log.info(os);
		
		// build cmd proccess according to os
		if(os.contains("Windows")) // if windows
		{
			builder = new ProcessBuilder("cmd.exe", "/C", command);
			//log.info(command);
			builder.redirectErrorStream(true);
			Thread.sleep(1000);
			p = builder.start();
		} else if(os.contains("Mac")) { // If Mac 
			p = Runtime.getRuntime().exec(command, null);
			//log.info(command);
		}
		// get std output
		BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line="";
		StringBuilder lineList = new StringBuilder();
		line=r.readLine();
		int count=1;
		while(line != null){
			count++;
			if(!line.isEmpty() ){
				lineList.append(line);
				
			}
			if(line.contains("Console LogLevel: debug")){
				break;	
			}
				
			line=r.readLine();
			if(command.contains("/data/data/com.vmlite.vncserver/files/vmlitevncserver")){
				if(count>7) {
					break;
				}
			}
			
		
		}
		String log=lineList.toString();
		if(p!=null){
			p.destroy();
		}
		
		r.close();
	
		return log;
	}
	
	
	
}
