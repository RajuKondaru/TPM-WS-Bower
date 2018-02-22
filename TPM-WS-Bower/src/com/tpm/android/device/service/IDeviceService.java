package com.tpm.android.device.service;

import java.io.IOException;

public interface IDeviceService {
	
	public Object[] checkDeviceAdbConnection(String deviceId) throws InterruptedException, IOException ;
	
	public Object[] checkDeviceNetWorkConnection(String deviceId) throws InterruptedException, IOException ;
	
	public Object[] launchDeviceVNCServer(String deviceId) throws InterruptedException, IOException ;
	public Object[] startDeviceVNCServer(String deviceId) throws InterruptedException, IOException ;
	public Object[] stopDeviceVNCServer(String deviceId) throws InterruptedException, IOException ;
	public Object[] cleanDeviceVNCServer(String deviceId) throws InterruptedException, IOException ;
	public void chageDeviceOrientation(String deviceId, String orientation) throws InterruptedException, IOException ;
}
