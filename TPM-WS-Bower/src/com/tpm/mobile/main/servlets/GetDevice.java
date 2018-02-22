package com.tpm.mobile.main.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.tpm.mobile.android.dao.DeviceDAO;
import com.tpm.mobile.android.pojo.DeviceModel;

/**
 * Servlet implementation class GetDevice
 */
@SuppressWarnings("serial")
@WebServlet("/GetDevice")
public class GetDevice extends HttpServlet {
	private static final Logger log = Logger.getLogger(GetDevice.class);

	protected DeviceModel device;
	protected DeviceDAO deviceDao;
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
	        String deviceName = null;
			try {
				jObj = new JSONObject(sb.toString());
				deviceName = jObj.getString("deviceName");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				log.error(e);
			}
			log.info(deviceName);
			deviceDao= new DeviceDAO();
			device=deviceDao.getDevice(deviceName);
			HttpSession session=request.getSession();
			session.setAttribute("deviceName", device.getDeviceName());
			
			log.info(device.getDeviceUdid());
			log.info(device.getDeviceName());
			log.info(device.getDeviceIp());
			log.info(device.getDeviceTcpPort());
			log.info(device.getDeviceForwardPort());
			log.info(device.getDeviceStatus());
			response.setContentType("application/json; charset=UTF-8");
			String deviceUrl="http://"+device.getDeviceIp()+":"+ device.getDeviceTcpPort()+"/novnc/vnc_auto.html?host="+device.getDeviceIp()+"&port="+device.getDeviceForwardPort()+"&true_color=1";
			try {
				jObj.put("deviceName", device.getDeviceName());
				jObj.put("deviceUrl", deviceUrl);
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				log.error(e);
			}
			
	        response.getWriter().print(jObj);
	 
	   }
	 
	

	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
