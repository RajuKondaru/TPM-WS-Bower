package com.tpm.mobile.android.exe.api.robotium.servlet;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import com.google.gson.Gson;
import com.tpm.mobile.android.app.service.AppManageDao;
import com.tpm.mobile.android.exe.api.robotium.utils.AppUtility;
import com.tpm.mobile.android.pojo.AppPojo;
@WebServlet("/uploadaut")
public class UploadAUTApp extends HttpServlet {
	private static final Logger log = Logger.getLogger(UploadAUTApp.class);
    private static final long serialVersionUID = 1L;
    private final String UPLOAD_DIRECTORY = FileUtils.getUserDirectory()+"\\AppData\\Local\\Temp\\";
    private AppUtility appUtil= null;
    private List<String> testSuites;
    private AppPojo app;
    private AppManageDao appDao=null;
  
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
    
		app = new AppPojo();
		appDao = new AppManageDao();
	    boolean isMultipart = ServletFileUpload.isMultipartContent(request);
	   // log.info(isMultipart);
	    File uploadFileTemp = null;
	    // process only if it is multipart content
	    if (isMultipart) {
	        // Create a factory for disk-based file items
	        FileItemFactory factory = new DiskFileItemFactory();
	        HttpSession session=request.getSession(false);
		    JSONObject user=(JSONObject) session.getAttribute("userInfo");
			
	        // Create a new file upload handler
	        ServletFileUpload upload = new ServletFileUpload(factory);
	        try {
	        // Parse the request
	        	List<FileItem> multiparts = upload.parseRequest(request);
	
	            for (FileItem item : multiparts) {
	            	
	            	
	            	if (!item.isFormField()) {
	            		try{
	            			
	            			String app_file = item.getFieldName();
		            		
		            		// log.info("app_file  ::"+app_file);
		            		 if(app_file!=null ){
		            			 if(app_file.equalsIgnoreCase("app")){
				            		  	String name = new File(item.getName()).getName();
				            		    uploadFileTemp=new File(UPLOAD_DIRECTORY+ name);
							            
							            item.write(uploadFileTemp);
							            app.setAppName(name);
							            app.setDownloadedAppPath(uploadFileTemp.getAbsolutePath());
							          
								}
		            		 }
	            		}catch(Exception e){
	            			
	            		}
	                    
		            } else {
		            	String app_id = item.getFieldName();
	            		
	            		 if(app_id!=null ){
	            			 if(app_id.equalsIgnoreCase("appid")){
			            		 String appid= item.getString();
			            		 app.setId(Integer.parseInt(appid));
	            			 }
	            		 }
					}
	            	 
					
	            }
	            
	           	appUtil= new AppUtility();
			    testSuites = appUtil.appInfo(app.getDownloadedAppPath());
			    if(testSuites.size()!=0) {
			    	app.setAppTestSuites(testSuites);
		            app.setAppType("AUT");
		            app = appDao.storeRobotiumAUTAppInMangoDB(app, user.get("userId").toString());
		            out.write( new Gson().toJson(app));
			    } else {
			    	out.write("empty");
				}
	           
        	} catch (InterruptedException e) {
				
				e.printStackTrace();
				log.error(e);
			} catch (UnsupportedOperationException e) {
				log.error(e);
				e.printStackTrace();
				out.write("unsupported");
		    } catch (ParseException e) {
		    	log.error(e);
				e.printStackTrace();
			} catch (FileUploadException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} finally {
		    	try{
	    			FileUtils.deleteDirectory(new File(UPLOAD_DIRECTORY));
	    		}catch(IOException e){
	    			
	    		}
			}
	    }
	    
	   
	}
	  protected void doPost(HttpServletRequest request,
		         HttpServletResponse response) throws ServletException, IOException {
		    	doGet(request, response);
			    
		    }

}