package com.tpm.android.device.service;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import com.tpm.mobile.android.utils.CommandPrompt;

public class DeviceService implements IDeviceService {
	private static final Logger log = Logger.getLogger(DeviceService.class);
	private static CommandPrompt cmd ;
	@Override
	public Object[] checkDeviceAdbConnection(String deviceId) throws InterruptedException, IOException {
		// TODO Auto-generated method stub
		Object[] status = new Object[2];
		status[0] = false;
		String message = null;
		
		cmd = new CommandPrompt();
		String adbCommand = "adb devices";
		List<String> results = cmd.runCommand(adbCommand);
		StringBuilder sb = new StringBuilder();
		for (String string : results) {
			sb.append(string+" ");
		}
		String totalResult = sb.toString();
		System.out.println(totalResult);
		if(totalResult.contains(deviceId) && totalResult.contains("device")){
			log.info(deviceId+ " Device is connected");
			status[0] = true;
		}else if(totalResult.contains(deviceId) && totalResult.contains("offline")){
			log.error(deviceId+ " Device is offline");
		}else if(totalResult.contains(deviceId) && totalResult.contains("unautherized")){
			log.error(deviceId+ " Device is unautherized");
		} else {
			log.error(deviceId+ " Device is not connected to Server");
		}
		status[0] = true;
		if(!(Boolean) status[0]){
			message =" Device is not Available, Please contact support-apps";
			//log.error(deviceId + message);
		}
		status[1] = message;
		return status;
	}

	


	@Override
	public Object[] checkDeviceNetWorkConnection(String deviceId) throws InterruptedException, IOException {
		// TODO Auto-generated method stub
		Object[] status = new Object[2];
		status[0] = false;
		String message = null;
		cmd = new CommandPrompt();
		String adbCommand = "adb -s "+ deviceId +" shell ip -f inet addr show";
		// adb -s TA93300GXV shell ip -f inet addr show
		List<String> results = cmd.runCommand(adbCommand);
		for (String string : results) {
			if(string.contains("inet") && string.contains("scope global wlan0")  ){
				String[] srt = string.split(" ");
				String[] srt2 = srt[5].split("/");
				String ip = srt2[0];
				//log.info(srt2[0]);
				status[0] = true;
				message = deviceId + " is connected through global host :: "+ ip;
				log.info(message);
			}else if(string.contains("inet") && string.contains("scope host lo")){
				String[] srt = string.split(" ");
				String[] srt2 = srt[5].split("/");
				String ip = srt2[0];
				//log.info(srt2[0]);
				status[0] = false;
				message = deviceId + " is connected through local host :: "+ ip;
				log.info(message);
				
			}
			//log.info(string);
		}
		if(!(Boolean) status[0]){
			message = " Device network failure, Please contact support-apps";
			//log.error(deviceId + message);
			
		}
		status[1] = message;
		return status;
	}
	

	@Override
	public Object[] launchDeviceVNCServer(String deviceId) throws InterruptedException, IOException {
		// TODO Auto-generated method stub
		Object[] status = new Object[2];
		status[0] = false;
		String message = null;
		cmd = new CommandPrompt();
		String adbCommand = "adb -s "+deviceId+" shell am start -a android.intent.action.MAIN -n com.vmlite.vncserver/.MainActivity";
		//adb -s TA93300GXV shell am start -a android.intent.action.MAIN -n com.vmlite.vncserver/.MainActivity
		List<String> results =cmd.runCommand(adbCommand);
		StringBuilder sb = new StringBuilder();
		for (String string : results) {
			sb.append(string);
		}
		String totalResult=sb.toString();
		if(totalResult.contains("Starting")) {
			status[0] = true;
			if(totalResult.contains("Warning: Activity not started")) {
				message = deviceId +" device VMLite VNC Srever already Launched";
				log.info(message);
			}else {
				message = deviceId + " device VMLite VNC Srever is Launched Successfully";
				log.info(message);
			
			}
		} else {
			message = " device VMLite VNC Server is not Launched, Please contact support-apps";
			log.error(deviceId + message);
		}
		if(!(Boolean)status[0]){
			message=" Device startup activity failure, Please contact support-apps";
			//log.error(deviceId + message);
			
		}
		status[1]=message;
		return status;
	}

	@Override
	public Object[] startDeviceVNCServer(String deviceId) throws InterruptedException, IOException {
		// TODO Auto-generated method stub
		Object[] status= new Object[2];
		status[0]= false;
		String message=null;
		cmd = new CommandPrompt();
		String adbCommand ="adb -s "+deviceId+" shell /data/data/com.vmlite.vncserver/files/vmlitevncserver";
		//adb -s TA93300GXV shell /data/data/com.vmlite.vncserver/files/vmlitevncserver
		List<String> results =cmd.runCommand(adbCommand);
		StringBuilder sb = new StringBuilder();
		for (String string : results) {
			sb.append(string);
		}
		String totalResult=sb.toString();
		if(totalResult.contains("init_flinger")) {
			status[0] = true;
			message=deviceId+ " device VMLite VNC Server is started successfully ";
			log.info(message);
		} else {
			
			message=deviceId+ " device VMLite VNC Server is not started.";
			log.info(message);
		}
		if(!(Boolean)status[0]) {
			
			message=" Device startup activity failure, Please contact support-apps";
			//log.error(deviceId + message);
		}else{
			adbCommand ="adb -s "+deviceId+" shell input keyevent 4";
			cmd.runCommand(adbCommand);
		}
		status[1]=message;
		return status;
	}




	
	@Override
	public Object[] stopDeviceVNCServer(String deviceId) throws InterruptedException, IOException {
		// TODO Auto-generated method stub
		Object[] status= new Object[2];
		status[0]= false;
		String message=null;
		cmd = new CommandPrompt();
		String adbCommand ="adb -s "+deviceId+" shell /data/data/com.vmlite.vncserver/files/vmlitevncserver --stop";
		//  adb -s TA93300GXV shell /data/data/com.vmlite.vncserver/files/vmlitevncserver --stop
		List<String> results =cmd.runCommand(adbCommand);
		StringBuilder sb = new StringBuilder();
		for (String string : results) {
			sb.append(string);
		}
		String totalResult=sb.toString();
		log.info(totalResult);
		if(totalResult.contains("udp port")) {
			status[0] = true;
			message=deviceId+" device VMLite VNC server has to be stoped successfully";
			log.info(message);
		} else {
			message=deviceId+" device VMLite VNC server is not to be stoped, Please stop VMLite VNC server manually";
			log.error(message);
		}
		if(!(Boolean)status[0]) {
			message=" Device cleanup activity failure, Please contact support-apps";
			//log.error(deviceId + message);
		}
		status[1]=message;
		return status;
	}
	
	

	public static void main1(String[] args) throws InterruptedException, IOException {
		String deviceId= "TA93300GXV";
		cmd = new CommandPrompt();
		String adbCommand ="adb devices";
		List<String> results =cmd.runCommand(adbCommand);
		for (String string : results) {
			if(string.contains(deviceId) && string.contains("device")){
				log.info(deviceId+ " is connected");
			}else if(string.contains(deviceId) && string.contains("offline")){
				log.info(deviceId+ " is offline");
			}if(string.contains(deviceId) && string.contains("unautherized")){
				log.info(deviceId+ " is unautherized");
			}
			
		}
	}
	public static void main2(String[] args) throws InterruptedException, IOException {
		String deviceId= "TA93300GXV";
		cmd = new CommandPrompt();
		String adbCommand ="adb -s "+deviceId+" shell ip -f inet addr show";
		List<String> results =cmd.runCommand(adbCommand);
		for (String string : results) {
		//	log.info(string);
			if(string.contains("inet") && string.contains("scope global wlan0")  ){
				String[] srt=string.split(" ");
				String[] srt2=srt[5].split("/");
				String ip= srt2[0];
				//log.info(srt2[0]);
				log.info(deviceId+ " is connected through global host :: "+ip);
			}else if(string.contains("inet") && string.contains("scope host lo")){
				String[] srt=string.split(" ");
				String[] srt2=srt[5].split("/");
				String ip= srt2[0];
				//log.info(srt2[0]);
				log.info(deviceId+ " is connected through local host :: "+ip);
			}
			
		}
	}

	public static void main(String[] args) throws InterruptedException, IOException {
		String deviceId= "TA93300GXV";
		cmd = new CommandPrompt();
		String adbCommand ="adb -s "+deviceId+" shell am start -a android.intent.action.MAIN -n com.vmlite.vncserver/.MainActivity";
		List<String> results =cmd.runCommand(adbCommand);
		
		for (String string : results) {
			
			if(string.contains("Starting")) {
				adbCommand ="adb -s "+deviceId+" shell /data/data/com.vmlite.vncserver/files/vmlitevncserver";
				results = cmd.runCommand(adbCommand);
				for (String result : results) {
					log.info(result);
				}
				//log.info(deviceId+ " is connected through global host :: "+ip);
			}else {
				log.info(string);
			}
			
		}
	}




	@Override
	public Object[] cleanDeviceVNCServer(String deviceId) throws InterruptedException, IOException {
		// TODO Auto-generated method stub
		Object[] status= new Object[2];
		status[0]= false;
		String message=null;
		cmd = new CommandPrompt();
		String adbCommand ="adb -s "+deviceId+" shell am force-stop com.vmlite.vncserver";
		//  adb -s TA93300GXV shell pm clear com.vmlite.vncserver
		List<String> results =cmd.runCommand(adbCommand);
		StringBuilder sb = new StringBuilder();
		for (String string : results) {
			sb.append(string);
		}
		String totalResult=sb.toString();
		log.info(totalResult);
		if(totalResult.contains("") || totalResult.isEmpty()) {
			status[0] = true;
			message=deviceId+" device VMLite VNC server App has to be closed successfully";
			log.info(message);
		} else {
			message=deviceId+" device VMLite VNC server App is not to be closed";
			log.warn(message);
		}
		if(!(Boolean)status[0]) {
			message=" Device cleanup activity failure, Please contact support-apps";
			//log.error(deviceId + message);
		}
		status[1]=message;
		return status;
	}




	@Override
	public void chageDeviceOrientation(String deviceId, String orientation) throws InterruptedException, IOException {
		cmd = new CommandPrompt();
		String adbCommand =null;
		if(orientation.equalsIgnoreCase("landscape")){
			adbCommand ="adb -s "+deviceId+" shell content insert --uri content://settings/system --bind name:s:user_rotation --bind value:i:1";
		} else if(orientation.equalsIgnoreCase("portrait")){ 
			adbCommand ="adb -s "+deviceId+" shell content insert --uri content://settings/system --bind name:s:user_rotation --bind value:i:0";
		} 
		
		//  adb -s TA93300GXV shell pm clear com.vmlite.vncserver
		List<String> results =cmd.runCommand(adbCommand);
		StringBuilder sb = new StringBuilder();
		for (String string : results) {
			sb.append(string);
		}
		String totalResult=sb.toString();
		log.info(totalResult);
		//return null;
	}
	
}
