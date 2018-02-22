package com.tpm.mobile.android.app.service;

public interface IVMLiteVNCServerService {
	public boolean startVNCServer(String deviceID);
	public boolean stopVNCServer(String deviceID);
}
