package com.tpm.mobile.android.dao;

import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.tpm.mobile.android.pojo.DeviceModel;
import com.tpm.mobile.android.pojo.WebConfigModel;
import com.tpm.mobile.db.utils.DataBase;
import com.tpm.mobile.log.LogServiceClient;

public class DeviceDAO implements IDevice{
	private static final Logger log = Logger.getLogger(DeviceDAO.class);
	private DataBase db;
	private Connection con;
	private PreparedStatement statement;
	private ResultSet result;
	private DeviceModel device;
	private WebConfigModel webConfig;
	private List<DeviceModel> devicesList;
	private List<WebConfigModel> webConfigsList;
	private static String host;
	private static Integer port;
	private static String dbName;
	
	
	static{
		Properties prop = new Properties();
		try {
			prop.load(LogServiceClient.class.getClassLoader()
					.getResourceAsStream("config.properties"));
			host = prop.getProperty("MONGODB_HOST");
			port = Integer.parseInt(prop.getProperty("MONGODB_PORT")) ;
			dbName = prop.getProperty("MONGODB_NAME");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public DeviceModel getDevice(String deviceName)  {
		db= new DataBase("jdbc/mobiledb");
		try {
			con = db.getConnection();
			statement = con.prepareStatement("select * from vmlite_urls where Device_Name ='"+deviceName+"'");
			result = statement.executeQuery();
			if(result.next()){
				device= new DeviceModel();
				device.setDeviceUdid(result.getString("Device_Id"));
				device.setDeviceName(result.getString("Device_Name"));
				device.setDeviceIp(result.getString("Ip_Address"));
				device.setDeviceTcpPort(Integer.parseInt(result.getString("Tcp_Port")));
				device.setDeviceForwardPort(Integer.parseInt(result.getString("Forward_Port")));
				device.setDeviceStatus(Integer.parseInt(result.getString("Status")));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error(e);
		}finally {
			try {
				if(result!=null){
					result.close();
				}
				if(statement!=null){
					statement.close();
				}
				if(con!=null){
					con.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				log.error(e);
			}
		}
		
		return device;
	}
	
	
	
	@Override
	public List<DeviceModel> getAllDevices()  {
		db= new DataBase("jdbc/mobiledb");
		try {
			con = db.getConnection();
			statement = con.prepareStatement("select * from vmlite_urls");
			result = statement.executeQuery();
			devicesList= new ArrayList<DeviceModel>();
			while(result.next()){
				device= new DeviceModel();
				device.setDeviceUdid(result.getString("Device_Id"));
				device.setDeviceName(result.getString("Device_Name"));
				device.setDeviceOs(result.getString("Device_OS"));
				device.setDeviceOsVersion(result.getString("Device_OS_Version"));
				device.setDeviceIp(result.getString("Ip_Address"));
				device.setDeviceTcpPort(Integer.parseInt(result.getString("Tcp_Port")));
				device.setDeviceForwardPort(Integer.parseInt(result.getString("Forward_Port")));
				device.setDeviceStatus(Integer.parseInt(result.getString("Status")));
				devicesList.add(device);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error(e);
		}finally {
			try {
				if(result!=null){
					result.close();
				}
				if(statement!=null){
					statement.close();
				}
				if(con!=null){
					con.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				log.error(e);
			}
		}
		 
		return devicesList;
	}


	@Override
	public int saveWebConfigs(WebConfigModel webConfigs){
		int flag =0;
		db= new DataBase("jdbc/mobiledb");
		
		try {
			con = db.getConnection();
			String savewebconfigsQuery = "INSERT INTO weburlconfigds"
					+ "(web_name, web_url, web_version) VALUES"
					+ "(?,?,?)"; 
			statement = con.prepareStatement(savewebconfigsQuery);
			statement.setString(1, webConfigs.getWebName());
			statement.setString(2, webConfigs.getWebUrl());
			statement.setString(3, webConfigs.getWebVersion());
			flag = statement.executeUpdate();
			
		}catch(SQLException e) {
			// TODO Auto-generated catch block
			log.error(e);
		}finally {
			try {
				if(statement!=null){
					statement.close();
				}
				if(con!=null){
					con.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				log.error(e);
			}
		}
		return flag;
		
	}



	@Override
	public List<WebConfigModel> getAllWebConfigs() {
		db= new DataBase("jdbc/mobiledb");
			
			try {
				con = db.getConnection();
				statement = con.prepareStatement("select * from weburlconfigds");
				result = statement.executeQuery();
				webConfigsList= new ArrayList<WebConfigModel>();
				while(result.next()){
					webConfig= new WebConfigModel();
					webConfig.setWebName(result.getString("web_name"));
					webConfig.setWebUrl(result.getString("web_url"));
					webConfig.setWebVersion(result.getString("web_version"));
					webConfigsList.add(webConfig);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				log.error(e);
			}finally {
				try {
					if(result!=null){
						result.close();
					}
					if(statement!=null){
						statement.close();
					}
					if(con!=null){
						con.close();
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					log.error(e);
				}
			}
		return webConfigsList;
	}
	
	
	// Mango Db Utilites

	@Override
	public List <DeviceModel> configDevices(List <DeviceModel> devices)  {
		MongoClient mongo=null;
		try {
			
			mongo = new MongoClient(host, port);
			DB db = mongo.getDB(dbName);
		
			DBCollection collection = db.getCollection("devices");
			Integer deviceId=1;
			for(DeviceModel device : devices) {
				BasicDBObject document = new BasicDBObject();
				document.put("DeviceUDID", device.getDeviceUdid());
				document.put("DeviceName", device.getDeviceName());
				document.put("DeviceId", deviceId);
				if(deviceId<3){
					document.put("DeviceType", "free");
				} else {
					document.put("DeviceType", "premium");
				}
				document.put("Plateform", device.getDeviceOs());
				document.put("Version", device.getDeviceOsVersion());
				document.put("Host", device.getDeviceIp());
				document.put("ADBStatus", device.getConnectionStatus());
				document.put("TcpPort", device.getDeviceTcpPort());
				document.put("ForwardPort", device.getDeviceForwardPort());
				document.put("APILevel", device.getApiLevel());
				document.put("UsageStatus", device.getDeviceStatus());
				document.put("Manufacturer", device.getManufacturer());
				document.put("Model", device.getModel());
				document.put("Resolution", device.getResolution());
				document.put("Icon", device.getDeviceIcon());
				collection.insert(document);
				deviceId++;
			}
				
			
		} catch (UnknownHostException e) {
			log.error(e);
		} catch (MongoException e) {
			log.error(e);
		} finally {
			if(mongo!=null){
				mongo.close();
			}
		}
		return devices;
	}



	@Override
	public void updateDevicesInfo(List<DeviceModel> devices) {

		MongoClient mongo=null;
		try {
			
			mongo = new MongoClient(host, port);
			DB db = mongo.getDB(dbName);
			DBCollection collection = db.getCollection("devices");
			
			for(DeviceModel device : devices) {
				BasicDBObject newDocument = new BasicDBObject();
				newDocument.append("$set", new BasicDBObject().append("ADBStatus", device.getConnectionStatus()));
				newDocument.append("$set", new BasicDBObject().append("Host", device.getDeviceIp()));
				
				BasicDBObject searchQuery = new BasicDBObject().append("DeviceUDID", device.getDeviceUdid());
				collection.update(searchQuery, newDocument);
			}
			
		} catch (UnknownHostException e) {
			log.error(e);
		} catch (MongoException e) {
			log.error(e);
		} finally {
			if(mongo!=null){
				mongo.close();
			}
		}
		
	}



	@Override
	public List<DeviceModel> getAllDevicesFromMango( )  {
		List<DeviceModel> devices= null;
		MongoClient mongo=null;
		try {
			
			mongo = new MongoClient(host, port);
			DB db = mongo.getDB(dbName);
			DBCollection collection = db.getCollection("devices");
			
			BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("ADBStatus", "device");
			DBCursor cursor = collection.find(whereQuery);
			
			DeviceModel device= null;
			DBObject deviceinfo;
			devices = new ArrayList<DeviceModel>();
			while(cursor.hasNext()) {
				deviceinfo=cursor.next();
				device = new DeviceModel();
				device.setDeviceUdid(deviceinfo.get("DeviceUDID").toString());
				try{
					device.setDeviceName(deviceinfo.get("DeviceName").toString());
				}catch(NullPointerException e){
					
				}
				device.setDeviceType(deviceinfo.get("DeviceType").toString());
				device.setDeviceOs(deviceinfo.get("Plateform").toString());
				device.setDeviceOsVersion(deviceinfo.get("Version").toString());
				device.setDeviceIp(deviceinfo.get("Host").toString());
				device.setConnectionStatus(deviceinfo.get("ADBStatus").toString());
				device.setDeviceTcpPort(Integer.parseInt(deviceinfo.get("TcpPort").toString()) );
				device.setDeviceForwardPort(Integer.parseInt(deviceinfo.get("ForwardPort").toString()) );
				device.setApiLevel(deviceinfo.get("APILevel").toString());
				device.setDeviceStatus(Integer.parseInt(deviceinfo.get("UsageStatus").toString()));
				device.setManufacturer(deviceinfo.get("Manufacturer").toString());
				device.setModel(deviceinfo.get("Model").toString());
				device.setResolution(deviceinfo.get("Resolution").toString());
				device.setDeviceIcon(deviceinfo.get("Icon").toString());
				devices.add(device); 
			}
			
		} catch (UnknownHostException e) {
			log.error(e);
			//log.error(e);
		} catch (MongoException e) {
			log.error(e);
			//log.error(e);
		} finally {
			if(mongo!=null){
				mongo.close();
			}
		}
		return devices;
	}
	@Override
	public List<DeviceModel> getAllDevicesInfoFromMango( )  {
		List<DeviceModel> devices= null;
		MongoClient mongo=null;
		try {
			
			mongo = new MongoClient(host, port);
			DB db = mongo.getDB(dbName);
			DBCollection collection = db.getCollection("devices");
			
			BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("ADBStatus", "device");
			DBCursor cursor = collection.find(whereQuery);
			
			DeviceModel device= null;
			DBObject deviceinfo;
			devices = new ArrayList<DeviceModel>();
			while(cursor.hasNext()) {
				deviceinfo=cursor.next();
				device = new DeviceModel();
				device.setDeviceUdid(deviceinfo.get("DeviceUDID").toString());
				device.setDeviceOs(deviceinfo.get("Plateform").toString());
				device.setDeviceOsVersion(deviceinfo.get("Version").toString());
				device.setManufacturer(deviceinfo.get("Manufacturer").toString());
				device.setModel(deviceinfo.get("Model").toString());
				try{
					device.setDeviceName(deviceinfo.get("DeviceName").toString());
				}catch(NullPointerException e){
					
				}
				devices.add(device); 
			}
			
		} catch (UnknownHostException e) {
			log.error(e);
			//log.error(e);
		} catch (MongoException e) {
			log.error(e);
			//log.error(e);
		} finally {
			if(mongo!=null){
				mongo.close();
			}
		}
		return devices;
	}



	@Override
	public boolean updateDeviceConnectionStatus(String deviceId, int statusCode) {
		// TODO Auto-generated method stub
		MongoClient mongo=null;
		try {
			
			mongo = new MongoClient(host, port);
			DB db = mongo.getDB(dbName);
				DBCollection collection = db.getCollection("devices");
			
			BasicDBObject newDocument = new BasicDBObject();
			newDocument.append("$set", new BasicDBObject().append("UsageStatus", statusCode));
			
			BasicDBObject searchQuery = new BasicDBObject().append("DeviceUDID", deviceId);
			collection.update(searchQuery, newDocument);
			
			
		} catch (UnknownHostException e) {
			log.error(e);
		} catch (MongoException e) {
			log.error(e);
		}  finally {
			if(mongo!=null){
				mongo.close();
			}
		}
		return false;
	}



	@Override
	public int checkDeviceConnectionStatus(String deviceId) {
		// TODO Auto-generated method stub
		int status = 0;
		MongoClient mongo=null;
		try {
			
			mongo = new MongoClient(host, port);
			DB db = mongo.getDB(dbName);
			DBCollection collection = db.getCollection("devices");
			
			BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("DeviceUDID", deviceId);
			DBCursor cursor = collection.find(whereQuery);
			
			
			DBObject deviceinfo;
			
			if(cursor.hasNext()) {
				deviceinfo=cursor.next();
				status = (int) deviceinfo.get("UsageStatus");
			}
			
		} catch (UnknownHostException e) {
			log.error(e);
		} catch (MongoException e) {
			log.error(e);
		} finally {
			if(mongo!=null){
				mongo.close();
			}
		}
		return status;
	}



	@Override
	public JSONObject getDeviceByModel(String deviceModel) throws SQLException {
		// TODO Auto-generated method stub
		JSONObject deviceResult = null;
		MongoClient mongo=null;
		try {
			
			mongo = new MongoClient(host, port);
			DB db = mongo.getDB(dbName);
			
			DBCollection collection = db.getCollection("devices");
			
			BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("Model", deviceModel);
			DBCursor cursor = collection.find(whereQuery);
			
			
			DBObject deviceinfo;
		
			if(cursor.hasNext()) {
				deviceinfo=cursor.next();
				String resultstr= deviceinfo.toString().replace("\\\"", "'");
				//log.info(result);
				JSONParser jsonParser = new JSONParser();
				try {
					deviceResult = (JSONObject)jsonParser.parse(resultstr);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		} catch (UnknownHostException e) {
			log.error(e);
			//log.error(e);
		} catch (MongoException e) {
			log.error(e);
			//log.error(e);
		} finally {
			if(mongo!=null){
				mongo.close();
			}
		}
		return deviceResult;
	}
	

}
