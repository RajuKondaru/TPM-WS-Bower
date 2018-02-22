package com.tpm.mobile.android.utils;
/*package com.infotree.objspy.util;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.google.gson.Gson;
import com.info.clic.robotium.app.AppUtility;
@WebServlet("/upload")
public class UploadFile extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final String UPLOAD_DIRECTORY = System.getProperty("java.io.tmpdir");
    private AppUtility appUtil= null;
    private List<String> testSuites;
    public static boolean isInstall= false;
    protected void doPost(HttpServletRequest request,
         HttpServletResponse response) throws ServletException, IOException {
    	doGet(request, response);
	    
    }

	*//**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 *//*
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//String deviceid=request.getParameter("deviceid");
		String deviceid="4897bb00";
	    boolean isMultipart = ServletFileUpload.isMultipartContent(request);
	   // System.out.println(isMultipart);
	    File uploadFileTemp = null;
	    // process only if it is multipart content
	    if (isMultipart) {
	        // Create a factory for disk-based file items
	        FileItemFactory factory = new DiskFileItemFactory();
	
	        // Create a new file upload handler
	        ServletFileUpload upload = new ServletFileUpload(factory);
	        try {
	        // Parse the request
	        	List<FileItem> multiparts = upload.parseRequest(request);
	
	            for (FileItem item : multiparts) {
		            if (!item.isFormField()) {
			            String name = new File(item.getName()).getName();
			            uploadFileTemp=new File(UPLOAD_DIRECTORY + File.separator + name);
			            uploadFileTemp.deleteOnExit();
			            item.write(uploadFileTemp);
		            }
	            }
	            appUtil= new AppUtility();
	            
	        	testSuites = appUtil.appInfo(uploadFileTemp.getAbsolutePath());
	        	try {
			        Process p = Runtime.getRuntime().exec("adb -s "+deviceid+" install "+uploadFileTemp.getAbsolutePath());
			        InputStream is = p.getInputStream();
			        InputStreamReader isr = new InputStreamReader(is);
			        BufferedReader br = new BufferedReader(isr);

			        String line;
			        
			        while ((line = br.readLine()) != null) {
			            System.out.println(line);
			           
			        	if(line.contains("Success")){
			        		isInstall= true;
				            	break;
			            }else if(line.contains("Failure")){
			            	isInstall= false;
			            	break;
						}
			        }
			        
			    } catch (IOException e) {
			        System.err.println(e);
			        e.printStackTrace();
			        
			    }
	        	
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
		          e.printStackTrace();
		    }
	    }
	    response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
	   
	    if(isInstall){
	    	String success = new Gson().toJson(testSuites);
	 	    out.write(success);
	    } else {
	    	String failure = new Gson().toJson("Something went wrong, please try again");
	    	out.write(failure);
		}
	   
	}
}*/