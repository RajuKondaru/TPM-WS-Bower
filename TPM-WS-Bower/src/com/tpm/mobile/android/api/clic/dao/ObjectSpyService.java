package com.tpm.mobile.android.api.clic.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import org.apache.log4j.Logger;

import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;
import com.tpm.mobile.android.pojo.AppPojo;
import com.tpm.mobile.android.utils.SqlDBConnector;

public class ObjectSpyService implements IObjectSpyService, IManualAppDao {
	
	private static final Logger log= Logger.getLogger(ObjectSpyService.class);
	private Connection con;
	private PreparedStatement statement;
	private ResultSet result;
	private String query;
	private String pageName ;
	private int status;
	//private String jndiName;
	//private DataBase db;

	public ObjectSpyService() throws IOException{
		/*Properties jndiProperties = new Properties();
		jndiProperties.load(ObjectSpyService.class.getClassLoader().getResourceAsStream("config.properties"));
		jndiName = jndiProperties.getProperty("jndiName");
		db= new DataBase(jndiName);*/
		
	}
	@Override
	public String verifyUser(String userid) {
		
		int count = 0;
		try {
			con = SqlDBConnector.getConnection();
			// select * from user where id=7
			query = "select * from user where id=?";
			//log.info(query);
			statement = con.prepareStatement(query);
			statement.setInt(1, Integer.parseInt(userid));
			result = statement.executeQuery();
			while (result.next()) {
				count = result.getInt(1);
			}
		}catch (SQLException e) {
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
		if (count != 0)
			return "authorized";
		else
			return "unauthorized";
	}

	@Override
	public String getPageName(String projid, String pagename) {
		String  isExists = null;
		String name = null;
		pageName = pagename.trim();
		
		try {
			con = SqlDBConnector.getConnection();
			query = "select pagename_objtemp from objectrepository_temp "
					+ "where projectid_objtemp=? "
					+ "and pagename_objtemp=?";
			//log.info(query);
			statement = con.prepareStatement(query);
			statement.setInt(1, Integer.parseInt(projid));
			statement.setString(2, pageName);
			result = statement.executeQuery();
			while (result.next()) {
				name = result.getString("pagename_objtemp");
			}
			if (name != null && name.equalsIgnoreCase(pageName))
				isExists="exists";
			else
				isExists="notexists";
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		return isExists;
	}
	
	@Override
	public void updateObjRepositoryTemp(String[] objectsList, String uid, String projectid) {
		String title = "";
		String type = "";
		String target = "";
		Random r = new Random();
		int id = r.nextInt(10000000);
		pageName = objectsList[0];
		
		try {
			con = SqlDBConnector.getConnection();
			query="insert into objectrepository_temp "
					+ "(id_objtemp, pagename_objtemp,type_objtemp,label_objtemp,"
					+ "identifier_objtemp,addedby_objtemp,modifiedby_objtemp,projectid_objtemp, capture_from)"
					+ "values(?,?,?,?,?,?,?,?,?)";
			//log.info(query);
			statement = con.prepareStatement(query);
			for (int i = 1; i < objectsList.length; i++) {
				String objs[] = objectsList[i].split(":,");
				title = objs[0];
				type = objs[1];
				target = objs[2].replaceAll("'", "\\\\\'");
				statement.setString(1, String.valueOf(id));
				statement.setString(2, pageName);
				statement.setString(3, type);
				statement.setString(4, title);
				statement.setString(5, target);
				statement.setString(6, uid);
				statement.setString(7, "0");
				statement.setString(8, projectid);
				statement.setString(9, "mobile");
				statement.executeUpdate();
			}
		}catch (SQLException e) {
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
		
	}

	@Override
	public Integer deviceStatus(String deviceId) {
		try {
			con = SqlDBConnector.getConnection();
			//select * from parallel_appium_node where device_id=TA64301YVY
			query = "select * from parallel_appium_node where device_id=?";
			//log.info(query);
			statement = con.prepareStatement(query);
			statement.setString(1, deviceId);
			result = statement.executeQuery();
			while (result.next()) {
				status = result.getInt("status");
			}
			
		}catch (SQLException e) {
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
		
		return status;
	}

	@Override
	public void updateDeviceStatus(String deviceId, Integer deviceStatus) {
		// TODO Auto-generated method stub
		try {
			con = SqlDBConnector.getConnection();
			// UPDATE parallel_appium_node SET status=1 WHERE TA64301YVY
			query = "UPDATE parallel_appium_node SET status=? where device_id=?";
			//log.info(query);
			statement = con.prepareStatement(query);
			statement.setInt(1, deviceStatus);
			statement.setString(2, deviceId);
			statement.executeUpdate();
			if(deviceStatus==0){
				log.info(deviceId+" Device is Released to user");
			}else {
				log.info(deviceId+" Device is allocated to user");
			}
			
		}catch (SQLException e) {
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
		
		
	}
	@Override
	public AppPojo getAppConfiguration(String configId) {
		// TODO Auto-generated method stub
		String fileName = null;
		String filePath = null;
		String appPackage = null;
		AppPojo app= null;
		try {
			con = SqlDBConnector.getConnection();
			// SELECT * FROM manual_mobile_uploads WHERE id=?
			query = "SELECT * FROM manual_mobile_uploads WHERE id=?";
			//log.info(query);
			statement = con.prepareStatement(query);
			statement.setString(1, configId);
			result = statement.executeQuery();
			while (result.next()) {
				fileName = result.getString("fileName");
				filePath = result.getString("filePath");
				appPackage = result.getString("package");
			}
			app= new AppPojo();
			app.setFileName(fileName);
			app.setFilePath(filePath);
			if(appPackage!=null)
			app.setAppPackageName(appPackage);
			
		}catch (SQLException e) {
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
		return app;
		
	}
	@Override
	public void UpdateAppConfigurations(String configId ,String appPackage) {
		// TODO Auto-generated method stub
		try {
			con = SqlDBConnector.getConnection();
			// UPDATE manual_mobile_uploads SET package =? WHERE id=?
			query = " UPDATE manual_mobile_uploads SET package =? where id=?";
			//log.info(query);
			statement = con.prepareStatement(query);
			statement.setString(1, appPackage);
			statement.setString(2, configId);
			statement.executeUpdate();
			
		}catch (SQLException e) {
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
	}
	@Override
	public void insertMobileObjTemp(String pageName, String type, String xpath, String userid) {
		
		// TODO Auto-generated method stub
				try {
					con = SqlDBConnector.getConnection();
					// UPDATE manual_mobile_uploads SET package =? WHERE id=?
					query = "INSERT INTO mobileobj_temp ( pageName, type, xpath, userid) VALUES (?,?,?,?)";
					//log.info(query);
					statement = con.prepareStatement(query);
					statement.setString(1, pageName);
					statement.setString(2, type);
					statement.setString(3, xpath);
					statement.setString(4, userid);
					statement.executeUpdate();
					
				}catch (SQLException e) {
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
	}
	@Override
	public JSONObject getMobileObjTemp(String pageName, String userid) {
		JSONObject obj = null;
		
		try {
			con = SqlDBConnector.getConnection();
			//select * from parallel_appium_node where device_id=TA64301YVY
			query = "select * from mobileobj_temp where pageName=? AND userid=?";
			//log.info(query);
			statement = con.prepareStatement(query);
			statement.setString(1, pageName);
			statement.setString(2, userid);
			result = statement.executeQuery();
			obj = new JSONObject();
			if (result.next()) {
				String type = result.getString("type");
				String xpath = result.getString("xpath");
			      try {
					obj.put("type", type);
					 obj.put("xpath",xpath);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			     
				
			}
			
		}catch (SQLException e) {
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
		
		return obj;
	}
	@Override
	public void deleteMobileObjTemp(String pageName) {
		// TODO Auto-generated method stub
		try {
			con = SqlDBConnector.getConnection();
			//select * from parallel_appium_node where device_id=TA64301YVY
			query = "DELETE FROM mobileobj_temp WHERE pageName=?";
			//log.info(query);
			statement = con.prepareStatement(query);
			statement.setString(1, pageName);
			statement.executeUpdate();
		}catch (SQLException e) {
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
	}
	@Override
	public void updateMobileObjTemp(String pageName, String type, String xpath) {
		// TODO Auto-generated method stub
		try {
			con = SqlDBConnector.getConnection();
			// UPDATE manual_mobile_uploads SET package =? WHERE id=?
			query = "UPDATE mobileobj_temp SET type=?,xpath=? WHERE pageName=?";
			//log.info(query);
			statement = con.prepareStatement(query);
			statement.setString(1, type);
			statement.setString(2, xpath);
			statement.setString(3, pageName);
			
			statement.executeUpdate();
			
		}catch (SQLException e) {
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
		
	}
	@Override
	public String getSessionMobileObjTemp(String pageName, String userid) {
		// TODO Auto-generated method stub
		String pagename = null;
		try {
			con = SqlDBConnector.getConnection();
			// UPDATE manual_mobile_uploads SET package =? WHERE id=?
			query = "SELECT pageName FROM mobileobj_temp WHERE pageName=? AND userid=?";
			//log.info(query);
			statement = con.prepareStatement(query);
			statement.setString(1, pageName);
			statement.setString(2, userid);
			result = statement.executeQuery();
			 
			if (result.next()) {
				pagename = result.getString("pageName");
			}
			
		}catch (SQLException e) {
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
		return pagename;
	}
	@Override
	public void addPageName(String userid, String pagename) {
		// TODO Auto-generated method stub
		try {
			con = SqlDBConnector.getConnection();
			// UPDATE manual_mobile_uploads SET package =? WHERE id=?
			query = "INSERT INTO mobileobj_temp ( userid, pageName, type, xpath  ) VALUES (?,?,?,?)";
			//log.info(query);
			statement = con.prepareStatement(query);
			statement.setString(1, userid);
			statement.setString(2, pagename);
			statement.setString(3, "");
			statement.setString(4, "");
			statement.executeUpdate();
			
		}catch (SQLException e) {
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
	}

}
