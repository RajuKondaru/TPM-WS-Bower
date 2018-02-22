package com.tpm.mobile.main.servlets;
 
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
@WebServlet("/GetAllConnectedDevices")
public class GetAllDevice2 extends HttpServlet {
	private static final Logger log = Logger.getLogger(GetAllDevice2.class);
	protected List<DeviceModel> devices;
	protected DeviceDAO deviceDao;
	protected  JSONObject deviceJson;
	protected DeviceService deviceService;
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		deviceService = new DeviceService();
		deviceDao= new DeviceDAO();
		JSONArray devicejsonArray =null;
		try {
			/*List <DeviceModel> devices=deviceService.getConnectedDevicesToServer();
			if(devices.isEmpty()){
				log.error("No Devices Connected to Server, Please Check Devices USB Connection to Server");
			} else {
			
				deviceDao.updateDevicesInfo(devices);*/
			
			List <DeviceModel> connectedDevices = deviceDao.getAllDevicesFromMango();
			devicejsonArray = new JSONArray();
			//log.info(connectedDevices.toString());
			if(connectedDevices.isEmpty()){
				log.error("No Devices Connected to Server, Please Check Devices USB Connection to Server");
			} else {
				
					for (DeviceModel device : connectedDevices) {
						deviceJson = new JSONObject();
						deviceJson.put("DeviceUDID", device.getDeviceUdid());
						deviceJson.put("DeviceName", device.getDeviceName());
						deviceJson.put("DeviceType", device.getDeviceType());
						deviceJson.put("Plateform", device.getDeviceOs());
						deviceJson.put("Version", device.getDeviceOsVersion());
						deviceJson.put("Host", device.getDeviceIp());
						deviceJson.put("TcpPort", device.getDeviceTcpPort());
						deviceJson.put("ForwardPort", device.getDeviceForwardPort());
						deviceJson.put("APILevel", device.getApiLevel());
						deviceJson.put("Manufacturer", device.getManufacturer());
						deviceJson.put("Model", device.getModel());
						deviceJson.put("Resolution", device.getResolution());
						deviceJson.put("ADBStatus", device.getConnectionStatus());
						deviceJson.put("Icon", device.getDeviceIcon());
						if(device.getDeviceStatus()==0){
							deviceJson.put("UsageStatus", "Available");
						}else {
							deviceJson.put("UsageStatus", "InUse");
						}
						//log.info(deviceJson.toString());
						devicejsonArray.put(deviceJson);
					}
					//log.info(devicejsonArray.toString());
					response.setContentType("application/json; charset=UTF-8");
				
				
		       
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			log.error(e);
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
