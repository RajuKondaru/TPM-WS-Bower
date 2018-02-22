package com.tpm.android.app.service;

import java.io.IOException;

public interface IVMLiteVNCServerService {
	public Object[] startVNCServer(String deviceID, String orientation) throws InterruptedException, IOException;
	public Object[] stopVNCServer(String deviceID, String orientation) throws InterruptedException, IOException;
}
