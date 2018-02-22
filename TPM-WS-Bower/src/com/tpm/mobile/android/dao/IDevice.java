package com.tpm.mobile.android.dao;

import java.sql.SQLException;
import java.util.List;

import org.json.simple.JSONObject;

import com.tpm.mobile.android.pojo.DeviceModel;
import com.tpm.mobile.android.pojo.WebConfigModel;

public interface IDevice {
	
	public DeviceModel getDevice(String deviceName)throws SQLException;
	
	public List<DeviceModel> getAllDevices()throws SQLException;
	
	public int saveWebConfigs(WebConfigModel webConfigs)throws SQLException;
	public List<WebConfigModel> getAllWebConfigs( )throws SQLException;
	
	public List <DeviceModel> configDevices(List <DeviceModel> devices);
	public void updateDevicesInfo(List <DeviceModel> devices);
	public List<DeviceModel> getAllDevicesFromMango();
	public List<DeviceModel> getAllDevicesInfoFromMango( ) ;
	public boolean updateDeviceConnectionStatus(String deviceId, int statusCode);
	public int checkDeviceConnectionStatus(String deviceId);
	public JSONObject getDeviceByModel(String deviceName)throws SQLException;

}
