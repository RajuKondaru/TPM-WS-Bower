package com.tpm.mobile.main.servlets;
 
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import com.tpm.mobile.android.dao.DeviceDAO;
import com.tpm.mobile.android.device.service.DeviceService;
import com.tpm.mobile.android.pojo.DeviceModel;

/**
 * Servlet implementation class GetDevice
 */
@SuppressWarnings("serial")
@WebServlet("/ConfigDevices")
public class ConfigDevicesToServer extends HttpServlet {
	private static final Logger log = Logger.getLogger(ConfigDevicesToServer.class);
	protected List<DeviceModel> devices;
	protected DeviceDAO deviceDao;
	protected DeviceService deviceService;
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		deviceService = new DeviceService();
		deviceDao= new DeviceDAO();
	
		try {
			List <DeviceModel> devices=deviceService.getAllConnectedDevicesToServer();
			if(devices.isEmpty()){
				log.warn("No Devices Connected to Server, Please Check Devices USB Connection to Server");
			} else {
				
				deviceDao.configDevices(devices);
				log.info(devices.toString());
			}
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			log.error(e1);
		}
		
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().print(devices.toString());
	 
	}
	 
	

	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
