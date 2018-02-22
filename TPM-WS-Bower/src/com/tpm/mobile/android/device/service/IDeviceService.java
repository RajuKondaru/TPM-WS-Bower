package com.tpm.mobile.android.device.service;

import java.io.IOException;
import java.util.List;

import com.tpm.mobile.android.pojo.DeviceModel;

public interface IDeviceService {
	
	public boolean checkDeviceAdbConnection(String deviceId) throws InterruptedException, IOException ;
	public boolean checkDeviceNetWorkConnection(String deviceId) throws InterruptedException, IOException ;
	public boolean launchDeviceVNCServer(String deviceId) throws InterruptedException, IOException ;
	public boolean startDeviceVNCServer(String deviceId) throws InterruptedException, IOException ;
	public boolean stopDeviceVNCServer(String deviceId) throws InterruptedException, IOException ;
	public boolean cleanDeviceVNCServer(String deviceId) throws InterruptedException, IOException ;
	public List <DeviceModel> getConnectedDevicesToServer() throws InterruptedException, IOException;
	public List <DeviceModel> getAllConnectedDevicesToServer() throws InterruptedException, IOException;
	
}
