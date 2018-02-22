package com.tpm.mobile.main.servlets;
 
import java.io.BufferedReader;
import java.io.IOException;
 
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tpm.mobile.android.dao.DeviceDAO;
import com.tpm.mobile.android.device.service.DeviceService;
import com.tpm.mobile.android.pojo.DeviceModel;

/**
 * Servlet implementation class GetDevice
 */
@SuppressWarnings("serial")
@WebServlet("/GetAllDevices")
public class GetAllDevice extends HttpServlet {
	private static final Logger log = Logger.getLogger(GetAllDevice.class);
	protected List<DeviceModel> devices;
	protected DeviceDAO deviceDao;
	protected  JSONObject deviceJson;
	protected DeviceService deviceService;
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StringBuilder sb = new StringBuilder();
        BufferedReader br = request.getReader();
        String str = null;
        while ((str = br.readLine()) != null) {
            sb.append(str);
        }
        JSONObject jObj = null;
        String appName = null;
		try {
			jObj = new JSONObject(sb.toString());
			appName = jObj.getString("appid");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			log.error(e);
		}
		
		log.info("App Name ::"+appName);
		HttpSession session=request.getSession();
		session.setAttribute("appid", appName);
		deviceService = new DeviceService();
		deviceDao= new DeviceDAO();
		devices=deviceDao.getAllDevices();
		JSONArray devicejsonArray =null;
		try {
			List devicesresult=deviceService.getConnectedDevicesToServer();
			if(devicesresult.isEmpty()){
				log.error("No Devices Connected to Server, Please Check Devices USB Connection to Server");
			} else {
				devicejsonArray = new JSONArray();
				log.info(devicesresult);
				try {
					for (DeviceModel device : devices) {
						deviceJson = new JSONObject();
						deviceJson.put("deviceId", device.getDeviceUdid());
						deviceJson.put("deviceName", device.getDeviceName());
						deviceJson.put("deviceOs", device.getDeviceOs());
						deviceJson.put("deviceOsVersion", device.getDeviceOsVersion());
						deviceJson.put("deviceIp", device.getDeviceIp());
						deviceJson.put("deviceTcpPort", device.getDeviceTcpPort());
						deviceJson.put("deviceForwardPort", device.getDeviceForwardPort());
						if(device.getDeviceStatus()==0){
							deviceJson.put("deviceStatus", "Available");
						}else {
							deviceJson.put("deviceStatus", "InUse");
						}
						
						devicejsonArray.put(deviceJson);
					}
					response.setContentType("application/json; charset=UTF-8");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					log.error(e);
				}
				
		       
			}
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			log.error(e1);
		}
		 response.getWriter().print(devicejsonArray);
	 
	}
	 
	

	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
