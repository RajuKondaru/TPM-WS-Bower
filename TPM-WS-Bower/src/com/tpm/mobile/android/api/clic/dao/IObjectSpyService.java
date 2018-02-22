package com.tpm.mobile.android.api.clic.dao;

import com.amazonaws.util.json.JSONObject;
import com.tpm.mobile.android.pojo.AppPojo;

public interface IObjectSpyService {
	public String verifyUser(String userid);
	public String getPageName(String projid,String pagename);
	public void addPageName(String userid,String pagename);
	public void updateObjRepositoryTemp(String objectsList[], String uid, String projectid);
	public Integer deviceStatus(String deviceId);
	public void updateDeviceStatus(String deviceId, Integer deviceStatus);
	public AppPojo getAppConfiguration(String configId);
	public void  updateMobileObjTemp(String sessionId, String type, String xpath);
	public void  insertMobileObjTemp(String sessionId, String type, String xpath, String userid);
	public String  getSessionMobileObjTemp(String sessionId, String userid);
	public JSONObject  getMobileObjTemp(String sessionId, String userid);
	public void  deleteMobileObjTemp(String sessionId);
}
