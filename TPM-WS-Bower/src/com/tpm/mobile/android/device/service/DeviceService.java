package com.tpm.mobile.android.device.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

import com.tpm.mobile.android.pojo.DeviceModel;
import com.tpm.mobile.android.utils.CommandPrompt;

public class DeviceService implements IDeviceService {
	private static final Logger log = Logger.getLogger(DeviceService.class);
	private static CommandPrompt cmd ;
	@Override
	public boolean checkDeviceAdbConnection(String deviceId) throws InterruptedException, IOException {
		// TODO Auto-generated method stub
		boolean connection= false;
		cmd = new CommandPrompt();
		String adbCommand ="adb devices";
		List<String> results =cmd.runCommand(adbCommand);
		StringBuilder sb = new StringBuilder();
		for (String string : results) {
			sb.append(string);
		}
		String totalResult=sb.toString();
	
		if(totalResult.contains(deviceId) && totalResult.contains("device")){
			log.info(deviceId+ " device is connected");
			connection= true;
		}else if(totalResult.contains(deviceId) && totalResult.contains("offline")){
			log.error(deviceId+ " device is offline");
		}if(totalResult.contains(deviceId) && totalResult.contains("unautherized")){
			log.error(deviceId+ " device is unautherized");
		}
		if(!connection){
			log.error(deviceId+ " device not connected, Please check adb connection");
		}
		
		return connection;
	}

	


	@Override
	public boolean checkDeviceNetWorkConnection(String deviceId) throws InterruptedException, IOException {
		// TODO Auto-generated method stub
		boolean connection= false;
		cmd = new CommandPrompt();
		String adbCommand ="adb -s "+deviceId+" shell ip -f inet addr show";
		// adb -s TA93300GXV shell ip -f inet addr show
		List<String> results =cmd.runCommand(adbCommand);
		for (String string : results) {
			if(string.contains("inet") && string.contains("scope global wlan0")  ){
				String[] srt=string.split(" ");
				String[] srt2=srt[5].split("/");
				String ip= srt2[0];
				//log.info(srt2[0]);
				connection= true;
				log.info(deviceId+ " is connected through global host :: "+ip);
			}else if(string.contains("inet") && string.contains("scope host lo")){
				String[] srt=string.split(" ");
				String[] srt2=srt[5].split("/");
				String ip= srt2[0];
				//log.info(srt2[0]);
				connection= false;
				log.info(deviceId+ " is connected through local host :: "+ip);
			}
			//log.info(string);
		}
		if(!connection){
			log.error(deviceId+ " device not connected to wifi network, Please check device wifi network connection");
		}
		return connection;
	}
	

	@Override
	public boolean launchDeviceVNCServer(String deviceId) throws InterruptedException, IOException {
		// TODO Auto-generated method stub
		boolean connection= false;
		cmd = new CommandPrompt();
		String adbCommand ="adb -s "+deviceId+" shell am start -a android.intent.action.MAIN -n com.vmlite.vncserver/.MainActivity";
		//adb -s TA93300GXV shell am start -a android.intent.action.MAIN -n com.vmlite.vncserver/.MainActivity
		List<String> results =cmd.runCommand(adbCommand);
		StringBuilder sb = new StringBuilder();
		for (String string : results) {
			sb.append(string);
		}
		String totalResult=sb.toString();
		if(totalResult.contains("Starting")) {
			connection = true;
			if(totalResult.contains("Warning: Activity not started")) {
				log.info(deviceId +" device VMLite VNC Srever already Launched");
			}else {
				log.info(deviceId+ " device VMLite VNC Srever is Successfully Launched");
			}
		}
		if(!connection){
			log.error(deviceId+ " device VMLite VNC Server is not Launched, Please conatct network admin");
		}
		return connection;
	}

	@Override
	public boolean startDeviceVNCServer(String deviceId) throws InterruptedException, IOException {
		// TODO Auto-generated method stub
		boolean connection= false;
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
			connection = true;
			log.info(deviceId+ " device VMLite VNC Server is started successfully ");
		}
		if(!connection) {
			log.error(deviceId+ " device VMLite VNC Server is not started, Please conatct network admin");
		}
		return connection;
	}




	
	@Override
	public boolean stopDeviceVNCServer(String deviceId) throws InterruptedException, IOException {
		// TODO Auto-generated method stub
		boolean connection= false;
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
			connection = true;
			log.info(deviceId+" device VMLite VNC server has to be stoped successfully");
		}
		if(!connection) {
			log.error(deviceId+" device VMLite VNC server has not to be stoped, Please conatct network admin");
		}
		return connection;
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
	public boolean cleanDeviceVNCServer(String deviceId) throws InterruptedException, IOException {
		// TODO Auto-generated method stub
		boolean connection= false;
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
			connection = true;
			log.info(deviceId+" device VMLite VNC server App has to be closed successfully");
		}
		if(!connection) {
			log.error(deviceId+" device VMLite VNC server App has not to be closed, Please conatct network admin");
		}
		return connection;
	}




	@Override
	public List <DeviceModel> getConnectedDevicesToServer() throws InterruptedException, IOException {
		// TODO Auto-generated method stub
				cmd = new CommandPrompt();
				String adbCommand ="adb devices";
				List<String> results =cmd.runCommand(adbCommand);
				int i = 1;
				List <DeviceModel> devices= new ArrayList<DeviceModel>();
				for (String string : results) {
					//log.info(i +" >"+string);
					
					String deviceId =null;
					if(i!=1){
						DeviceModel device = new DeviceModel();
						string =string.replaceAll("\\s+", " ");

						String [] deviceInfo=string.split(" ");
						for(int j=0; j<deviceInfo.length; j++){
							if(j==0){
								log.info("UDID ::"+deviceInfo[j]);
								deviceId = deviceInfo[j];
								device.setDeviceUdid(deviceId);
								String ip_cmd ="adb -s "+deviceId+" shell ip -f inet addr show";
								List<String> ip_results =cmd.runCommand(ip_cmd);
								for (String ipres : ip_results) {
								//	log.info(string);
									if(ipres.contains("inet") && ipres.contains("scope global wlan0")  ){
										String[] srt=ipres.split(" ");
										String[] srt2=srt[5].split("/");
										String ip= srt2[0];
										device.setDeviceIp(ip);
										//log.info(srt2[0]);
										log.info(deviceId+ " is connected through global host :: "+ip);
									}else if(ipres.contains("inet") && ipres.contains("scope host lo")){
										String[] srt=ipres.split(" ");
										String[] srt2=srt[5].split("/");
										String ip= srt2[0];
										//log.info(srt2[0]);
										log.info(deviceId+ " is connected through local host :: "+ip);
									}
									
								}
								
							} else {
								//log.info("Status ::"+deviceInfo[j]);
								device.setConnectionStatus(deviceInfo[j]);
							}
						}
						
						
						devices.add(device);
					}
					i++;
				}
				return devices;
		
	}




	@Override
	public List <DeviceModel> getAllConnectedDevicesToServer() throws InterruptedException, IOException {
		// TODO Auto-generated method stub
		cmd = new CommandPrompt();
		String adbCommand ="adb devices";
		List<String> results =cmd.runCommand(adbCommand);
		
		int i = 1;
		List <DeviceModel> devices= new ArrayList<DeviceModel>();
		for (String string : results) {
			//log.info(i +" >"+string);
			
			String deviceId =null;
			if(i!=1){
				DeviceModel device = new DeviceModel();
				string =string.replaceAll("\\s+", " ");

				String [] deviceInfo=string.split(" ");
				for(int j=0; j<deviceInfo.length; j++){
					if(j==0){
						log.info("UDID ::"+deviceInfo[j]);
						deviceId = deviceInfo[j];
						device.setDeviceUdid(deviceId);
						String ip_cmd ="adb -s "+deviceId+" shell ip -f inet addr show";
						List<String> ip_results =cmd.runCommand(ip_cmd);
						for (String ipres : ip_results) {
						//	log.info(string);
							if(ipres.contains("inet") && ipres.contains("scope global wlan0")  ){
								String[] srt=ipres.split(" ");
								String[] srt2=srt[5].split("/");
								String ip= srt2[0];
								device.setDeviceIp(ip);
								//log.info(srt2[0]);
								log.info(deviceId+ " is connected through global host :: "+ip);
							}else if(ipres.contains("inet") && ipres.contains("scope host lo")){
								String[] srt=ipres.split(" ");
								String[] srt2=srt[5].split("/");
								String ip= srt2[0];
								//log.info(srt2[0]);
								log.info(deviceId+ " is connected through local host :: "+ip);
							}
							
						}
						String mfr_cmd ="adb -s "+deviceId+" shell getprop ro.product.manufacturer ";
						List<String> mrf_results =cmd.runCommand(mfr_cmd);
						for (String mrfr : mrf_results) {
							log.info("Manufacturer : "+mrfr);
							device.setManufacturer(mrfr);
						}
						
						String disp_cmd ="adb -s "+deviceId+" shell getprop ro.product.display ";
						List<String> disp_results =cmd.runCommand(disp_cmd);
						if(!disp_results.isEmpty()){
							for (String disp : disp_results) {
								log.info("Device Name : "+disp);
								device.setDeviceName(disp);
							}
						} 
						String model_cmd ="adb -s "+deviceId+" shell getprop ro.product.model ";
						List<String> model_results =cmd.runCommand(model_cmd);
						for (String model : model_results) {
							log.info("Model No : "+model);
							device.setModel(model);
						}
						String sdk_cmd ="adb -s "+deviceId+" shell getprop ro.build.version.sdk ";
						List<String> sdk_results =cmd.runCommand(sdk_cmd);
						for (String sdk : sdk_results) {
							log.info("API Level : "+sdk);
							device.setApiLevel(sdk);
						}
						String rel_vr_cmd ="adb -s "+deviceId+" shell getprop ro.build.version.release ";
						List<String> vr_results =cmd.runCommand(rel_vr_cmd);
						for (String vr : vr_results) {
							log.info("OS Version : "+vr);
							device.setDeviceOsVersion(vr);
						}
						String rsln_cmd ="adb -s "+deviceId+" shell  wm size ";
						List<String> rsln_results =cmd.runCommand(rsln_cmd);
						for (String rsln : rsln_results) {
							String[] rsln_array=rsln.split(":");
							log.info("Resolution : "+rsln_array[1]);
							device.setResolution(rsln_array[1]);
						}
					} else {
						log.info("Status ::"+deviceInfo[j]);
						device.setConnectionStatus(deviceInfo[j]);
						device.setDeviceStatus(0);
						device.setDeviceOs("Android");
						device.setDeviceTcpPort(5801);
						device.setDeviceForwardPort(5901);
						device.setDeviceIcon("images/devices/"+device.getManufacturer()+"-"+device.getModel()+".png");
					}
				}
				
				
				devices.add(device);
			}
			i++;
		}
		return devices;
	}



	
}
