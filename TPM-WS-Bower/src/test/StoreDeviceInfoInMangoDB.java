package test;

import java.io.IOException;
import java.util.List;

import com.tpm.mobile.android.dao.DeviceDAO;
import com.tpm.mobile.android.device.service.DeviceService;
import com.tpm.mobile.android.pojo.DeviceModel;

public class StoreDeviceInfoInMangoDB {

	public static void main(String[] args) throws IOException {
		DeviceService deviceService = new DeviceService();
		DeviceDAO deviceDao= new DeviceDAO();
	
		try {
			List <DeviceModel> devices=deviceService.getAllConnectedDevicesToServer();
			if(devices.isEmpty()){
				System.out.println("No Devices Connected to Server, Please Check Devices USB Connection to Server");
			} else {
				
				deviceDao.configDevices(devices);
			}
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			System.out.println(e1);
		}

	}

}
