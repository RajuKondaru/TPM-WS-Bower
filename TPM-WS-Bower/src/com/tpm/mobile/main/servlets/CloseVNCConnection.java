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

/**
 * Servlet implementation class StopExecuterServlet
 */
@SuppressWarnings("serial")
@WebServlet("/CloseVNCConnection")
public class CloseVNCConnection extends HttpServlet {
	private static final Logger log = Logger.getLogger(CloseVNCConnection.class);
	protected String deviceID;
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
       
		try {
			jObj = new JSONObject(sb.toString());
			deviceId = jObj.getString("deviceId");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			log.error(e);
		}
		IDevice dService= new DeviceDAO();
	
		
		status = dService.checkDeviceConnectionStatus(deviceId);
		if(status==1){
			VMLiteVNCServerService vncService= new VMLiteVNCServerService();
			
			Object[] isStop = null;
			try {
				isStop = vncService.stopVNCServer(deviceId, "portrait");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if((Boolean) isStop[0]){
				dService.updateDeviceConnectionStatus(deviceId, 0);
				log.info("Success");
				pw.print("Success");
			}else {
				pw.print(isStop[1]);
				log.error(isStop[1]);
			}
		}else {
			pw.print("Device cleanup activity failure, Plaese contact support-apps");
			log.error("Device cleanup activity failure, Plaese contact support-apps");
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
