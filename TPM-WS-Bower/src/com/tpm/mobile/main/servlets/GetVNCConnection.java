package com.tpm.mobile.main.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.tpm.android.app.service.VMLiteVNCServerService;
import com.tpm.mobile.android.dao.DeviceDAO;
import com.tpm.mobile.android.dao.IDevice;
import com.tpm.mobile.android.pojo.DeviceModel;

/**
 * Servlet implementation class GetDevice
 */
@SuppressWarnings("serial")
@WebServlet("/GetVNCConnection")
public class GetVNCConnection extends HttpServlet {
	private static final Logger log = Logger.getLogger(GetVNCConnection.class);

	protected DeviceModel device;
	protected DeviceDAO deviceDao;
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
		StringBuilder sb = new StringBuilder();
        BufferedReader br = request.getReader();
        String str = null;
        while ((str = br.readLine()) != null) {
            sb.append(str);
        }
        JSONObject jObj = null;
        String deviceId = null;
        int status = 0;
        String orientation = null;
		try {
			jObj = new JSONObject(sb.toString());
			deviceId = jObj.getString("deviceId");
			orientation = jObj.getString("orientation");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			log.error(e);
		}
		IDevice dService= new DeviceDAO();
		status=dService.checkDeviceConnectionStatus(deviceId);
		if(status==0){
			VMLiteVNCServerService vncService= new VMLiteVNCServerService();
			Object[] isStart = null;
			try {
				isStart = vncService.startVNCServer(deviceId, orientation);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if((Boolean) isStart[0]){
				dService.updateDeviceConnectionStatus(deviceId, 1);
				pw.print("Success");
				log.info("Success");
			}else {
				pw.print(isStart[1]);
				log.error(isStart[1]);
			}
		}else {
			pw.print("Device is busy or not avialble at this time, Choose another device to continue");
			log.error(deviceId+" Device is busy or not avialble at this time, Choose another device to continue");
		}
	}
	 
	

	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
