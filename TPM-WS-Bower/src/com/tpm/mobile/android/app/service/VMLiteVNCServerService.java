package com.tpm.mobile.android.app.service;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.tpm.mobile.android.device.service.DeviceService;

public class VMLiteVNCServerService implements IVMLiteVNCServerService {
	private static final Logger log = Logger.getLogger(VMLiteVNCServerService.class);
	DeviceService deviceServ;
	@Override
	public boolean startVNCServer(String deviceID) {
		// TODO Auto-generated method stub
		boolean isStarted= false;
		deviceServ= new DeviceService();
		try {
			boolean isConnected = deviceServ.checkDeviceAdbConnection(deviceID);
			if(isConnected){
				boolean isOnline = deviceServ.checkDeviceNetWorkConnection(deviceID);
				if(isOnline){
					boolean isLaunch = deviceServ.launchDeviceVNCServer(deviceID);
					if(isLaunch){
						isStarted= deviceServ.startDeviceVNCServer(deviceID);
					}
				}
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		}
		return isStarted;
	}

	@Override
	public boolean stopVNCServer(String deviceID) {
		// TODO Auto-generated method stub
		boolean isCleaned= false;
		deviceServ= new DeviceService();
		try {
			boolean isConnected = deviceServ.checkDeviceAdbConnection(deviceID);
			if(isConnected){
				boolean isOnline = deviceServ.checkDeviceNetWorkConnection(deviceID);
				if(isOnline){
					boolean isStopped =  deviceServ.stopDeviceVNCServer(deviceID);
					if(isStopped){
						isCleaned =  deviceServ.cleanDeviceVNCServer(deviceID);
					}	
				}
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		}
		return isCleaned;
	}

}
