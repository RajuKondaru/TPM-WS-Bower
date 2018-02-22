package test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import com.tpm.mobile.android.dao.DeviceDAO;
import com.tpm.mobile.android.device.service.DeviceService;
import com.tpm.mobile.android.pojo.DeviceModel;
import com.tpm.mobile.android.utils.CommandPrompt;

public class GetDeviceUDIDTest {
	public static CommandPrompt cmd = null;
	public static void main(String[] args) throws InterruptedException, IOException {
		// TODO Auto-generated method stub
				cmd = new CommandPrompt();
				String adbCommand ="adb devices";
				List<String> results =cmd.runCommand(adbCommand);
				
				int i = 1;
				Map<String, List> devicesmap= new HashMap<String, List>();
				for (String string : results) {
					//System.out.println(i +" >"+string);
					List<String> devicesinfo= new ArrayList<String>();
					String deviceId =null;
					if(i!=1){
						string =string.replaceAll("\\s+", " ");

						String [] deviceInfo=string.split(" ");
						for(int j=0; j<deviceInfo.length; j++){
							if(j==0){
								System.out.println("UDID ::"+deviceInfo[j]);
								deviceId = deviceInfo[j];
								String ip_cmd ="adb -s "+deviceId+" shell ip -f inet addr show";
								List<String> ip_results =cmd.runCommand(ip_cmd);
								for (String ipres : ip_results) {
								//	log.info(string);
									if(ipres.contains("inet") && ipres.contains("scope global wlan0")  ){
										String[] srt=ipres.split(" ");
										String[] srt2=srt[5].split("/");
										String ip= srt2[0];
										devicesinfo.add(ip);
										//log.info(srt2[0]);
										System.out.println(deviceId+ " is connected through global host :: "+ip);
									}else if(ipres.contains("inet") && ipres.contains("scope host lo")){
										String[] srt=ipres.split(" ");
										String[] srt2=srt[5].split("/");
										String ip= srt2[0];
										//log.info(srt2[0]);
										System.out.println(deviceId+ " is connected through local host :: "+ip);
									}
									
								}
								String mfr_cmd ="adb -s "+deviceId+" shell getprop ro.product.manufacturer ";
								List<String> mrf_results =cmd.runCommand(mfr_cmd);
								for (String mrfr : mrf_results) {
									System.out.println("Manufacturer : "+mrfr);
									devicesinfo.add(mrfr);
								}
								
								String disp_cmd ="adb -s "+deviceId+" shell getprop ro.product.display ";
								List<String> disp_results =cmd.runCommand(disp_cmd);
								if(!disp_results.isEmpty()){
									for (String disp : disp_results) {
										System.out.println("Device Name : "+disp);
										devicesinfo.add(disp);
									}
								} 
								String model_cmd ="adb -s "+deviceId+" shell getprop ro.product.model ";
								List<String> model_results =cmd.runCommand(model_cmd);
									 for (String model : model_results) {
											System.out.println("Model No : "+model);
											devicesinfo.add(model);
										}
								
								
								String sdk_cmd ="adb -s "+deviceId+" shell getprop ro.build.version.sdk ";
								List<String> sdk_results =cmd.runCommand(sdk_cmd);
								for (String sdk : sdk_results) {
									System.out.println("API Level : "+sdk);
									devicesinfo.add(sdk);
								}
								String rel_vr_cmd ="adb -s "+deviceId+" shell getprop ro.build.version.release ";
								List<String> vr_results =cmd.runCommand(rel_vr_cmd);
								for (String vr : vr_results) {
									System.out.println("OS Version : "+vr);
									devicesinfo.add(vr);
								}
								
								String rsln_cmd ="adb -s "+deviceId+" shell  wm size ";
								List<String> rsln_results =cmd.runCommand(rsln_cmd);
								for (String rsln : rsln_results) {
									System.out.println("Resolution : "+rsln);
									String[] rsln_array=rsln.split(":");
									devicesinfo.add(rsln_array[1]);
								}
								
							} else {
								System.out.println("Status ::"+deviceInfo[j]);
								devicesinfo.add(deviceInfo[j]);
							}
							
							
						}
						devicesmap.put(deviceId, devicesinfo);
					}
					
					
					
					
					i++;
					
				}
				System.out.println(devicesmap.toString());
				
				
				
				
				
	}
}
