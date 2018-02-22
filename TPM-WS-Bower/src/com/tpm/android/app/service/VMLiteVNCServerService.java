package com.tpm.android.app.service;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.tpm.android.device.service.DeviceService;

public class VMLiteVNCServerService implements IVMLiteVNCServerService {
	private static final Logger log = Logger.getLogger(VMLiteVNCServerService.class);
	DeviceService deviceServ;
	@Override
	public Object[] startVNCServer(String deviceID, String orientation) throws InterruptedException, IOException {
		// TODO Auto-generated method stub
		
		deviceServ= new DeviceService();
		//deviceServ.chageDeviceOrientation(deviceID, orientation);
		try {
			Object[] isConnected = deviceServ.checkDeviceAdbConnection(deviceID);
			if((Boolean) isConnected[0]){
				Object[] isOnline = deviceServ.checkDeviceNetWorkConnection(deviceID);
				if((Boolean) isOnline[0]){
					Object[] isLaunch = deviceServ.launchDeviceVNCServer(deviceID);
					if((Boolean) isLaunch[0]){
						return deviceServ.startDeviceVNCServer(deviceID);
					}else {
						return isLaunch;
					}
				}else {
					return isOnline;
				}
			} else {
				return isConnected;
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		}
		return null;
	}

	@Override
	public Object[] stopVNCServer(String deviceID, String orientation) throws InterruptedException, IOException {
		// TODO Auto-generated method stub
		
		deviceServ= new DeviceService();
		deviceServ.chageDeviceOrientation(deviceID, orientation);
		try {
			Object[] isConnected= deviceServ.checkDeviceAdbConnection(deviceID);
			if((Boolean) isConnected[0]){
				Object[] isOnline = deviceServ.checkDeviceNetWorkConnection(deviceID);
				if((Boolean) isOnline[0]){
					Object[] isStopped =  deviceServ.stopDeviceVNCServer(deviceID);
					if((Boolean) isStopped[0]){
						return  deviceServ.cleanDeviceVNCServer(deviceID);
					} else {
						return isStopped;
					}	
				} else {
					return isOnline;
				}
			} else {
				return isConnected;
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		}
		return null;
	}

}
