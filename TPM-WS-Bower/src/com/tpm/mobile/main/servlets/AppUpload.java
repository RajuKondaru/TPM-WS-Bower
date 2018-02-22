package com.tpm.mobile.main.servlets;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import java.text.ParseException;

import com.tpm.mobile.android.app.service.AppManageDao;
import com.tpm.mobile.android.app.service.AppManageService;
import com.tpm.mobile.android.pojo.AppPojo;
import com.tpm.mobile.android.utils.FindFileInZipFile;
import com.tpm.mobile.android.utils.Utils;

/**
 * @author Raju Kondaru Servlet implementation class
 *         AppUpload->This servlet is the implementation of Mobile App upload.
 */
@WebServlet("/AppUpload")
@MultipartConfig(fileSizeThreshold=1024*1024*100,	// 100MB+ 
				 maxFileSize=1024*1024*200,		// 200MB+
				 maxRequestSize=1024*1024*200)	// 200MB+
public class AppUpload extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(AppUpload.class);
	private AppManageService appSer=null;
	private AppManageDao appDao=null;
	static boolean isComplted = false;
	private AppPojo app =null;
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 * @RequestParameters appName, appVersion, apptype
	 * @Description appName -is mobile app name given by user. appVersion-is for mobile app verion given by user
	 *              apptype - is specifed type of mobile app Ex: Native or Hybrid.
	 */
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		/**
		 * Have to get Parameters from request and set into pogo class for store app in MongoDb.
		 * 
		 */
		
		String imgBase64String=null;
		JSONObject jObj = null;
		PrintWriter out= response.getWriter();
		String appName=request.getParameter("appName");
		String appVersion=request.getParameter("appVersion");
		String apptype=request.getParameter("apptype");
		app = new AppPojo();
		app.setAppName(appName);
		app.setVersion(appVersion);
		app.setAppType(apptype);
		//  check request contains multipart content or not. 
		if (ServletFileUpload.isMultipartContent(request)) {
			appSer = new AppManageService();
			// get app file from request.
			app = appSer.getFileFromRequest(request, app);
			// check app type based on file extention.
			if(app.getFileName().contains(".apk")){
				// get app information from app file.
				app=appSer.getAppInfoService( app);
				if(app.getAppPackageName()!=null){
					FindFileInZipFile zipFile = new FindFileInZipFile();
					BufferedImage icon=zipFile.getImage(app.getDownloadedAppPath(), app.getIconPath());
					
					if(icon!=null){
						imgBase64String=Utils.encodeImgTobase64String(icon, "png");
						app.setIcon("data:image/jpeg;base64,"+imgBase64String);
							
					}
					app.setStatus("Active");
					
					HttpSession session=request.getSession(false);
					final JSONObject user=(JSONObject) session.getAttribute("userInfo");
					
					// TODO Auto-generated method stub
					/*boolean isUnsigned = appSer.appUnsignService(app);
					if(isUnsigned){
						boolean isResigned = appSer.appResignService(app);
						if(isResigned){*/
							try {
								appDao = new AppManageDao();
								appDao.storeAppInMangoDB(app, user.get("userId").toString());
								
							} catch (ParseException e1) {
								// TODO Auto-generated catch block
								log.error(e1);
							}
				/* 		}
					}*/
							
							jObj = new JSONObject();
							jObj.put("activityName", app.getActivityName());
							jObj.put("packageName", app.getAppPackageName());
							jObj.put("appName", app.getAppName());
							jObj.put("imgBase64String", imgBase64String);
							jObj.put("version", app.getVersion());
							jObj.put("buildno", app.getBuildId());
							jObj.put("uploadtime", app.getUploadedDate());
							jObj.put("appid", app.getId());
					
					
					
				}
				
				
			}
			
			
			File temp= new File(app.getDownloadedAppPath());
			temp.delete();
			
			response.setContentType("application/json; charset=UTF-8");
			log.info(jObj);
			if(jObj!=null){
				out.print(jObj);
			} else {
				out.print("failed");
			}
			
        }
	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 * @RequestParameters appName, appVersion, apptype
	 * @Description appName -is mobile app name given by user. appVersion-is for mobile app verion given by user
	 *              apptype - is specifed type of mobile app Ex: Native or Hybrid.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
}