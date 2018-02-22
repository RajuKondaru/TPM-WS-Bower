package com.tpm.mobile.android.app.service;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.tpm.mobile.android.pojo.AppPojo;
import com.tpm.mobile.android.utils.ApkToolUtil;
import com.tpm.mobile.android.utils.CommandPrompt;
import com.tpm.mobile.android.utils.Utils;
import com.tpm.mobile.android.utils.ZipUtility;







public class AppManageService implements IAppManageService {
	private static final Logger log = Logger.getLogger(AppManageService.class);
	private CommandPrompt cmd ;
	private String appPackage;
	private String buildToolPath;
	private String KEY_STORE;
	private String KEY_PASS;
	private String STORE_PASS;
	private String KEY_ALIAS;
	
	private int maxFileSize = 50000 * 1024;
	private int maxMemSize = 4000 * 1024;
	private File file ;
	public AppManageService() throws IOException{
		Properties prop = new Properties();
		prop.load(AppManageService.class.getClassLoader().getResourceAsStream("config.properties"));
		buildToolPath = prop.getProperty("buildToolPath");
		KEY_STORE = prop.getProperty("KEY_STORE");
		KEY_PASS = prop.getProperty("KEY_PASS");
		STORE_PASS = prop.getProperty("STORE_PASS");
		KEY_ALIAS = prop.getProperty("KEY_ALIAS");
		
	}
		
	public boolean appAUTInstallService(String deviceId, AppPojo app) {
		boolean isInstall= false;
		try {
			String autinstallcmd= "adb -s "+deviceId+" install "+app.getDownloadedAppPath();
			log.info(autinstallcmd);
	        Process p = Runtime.getRuntime().exec(autinstallcmd);
	        InputStream is = p.getInputStream();
	        InputStreamReader isr = new InputStreamReader(is);
	        BufferedReader br = new BufferedReader(isr);

	        String line;
	        
	        while ((line = br.readLine()) != null) {
	        	log.info(line);
	           
	        	if(line.contains("Success")){
	        		isInstall= true;
		            	break;
	            }else if(line.contains("Failure")){
	            	isInstall= false;
	            	break;
				}
	        }
	        
	    } catch (IOException e) {
	        log.error(e);
	        e.printStackTrace();
	        
	    }
    	
		return isInstall;
		
	}
	@Override
	public boolean appInstallService(String deviceId, AppPojo app) {
		boolean status=false;
		
		try {
			cmd = new CommandPrompt();
			
			String apkFile= app.getDownloadedAppPath();
			String apkName = app.getFileName();
			appPackage = app.getAppPackageName();
			log.info("App Name >> " + apkName);
			log.info("app Package >> " + appPackage);
			String installCommand = "adb -s "+deviceId+" install "+apkFile; // 0123456789ABCD // // adb -s 0123456789ABCD install D:/RedBus.apk
			log.info("Command >> " + installCommand);
			
			cmd.runCommand(installCommand);
			
			status = true;
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			log.error(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error(e);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e);
		}
		return status;
	}

	@Override
	public boolean appUnInstallService(String deviceId, AppPojo app) {
		// TODO Auto-generated method stub
		boolean status=false;
		String appPackage = app.getAppPackageName();
		cmd = new CommandPrompt();
		try {
			String clearCommand = "adb -s "+deviceId+" shell pm clear "+appPackage; // 0123456789ABCD // // adb shell pm clear in.redbus.android
			log.info(clearCommand);
			List<String> clearResultList = null;
			try {
				clearResultList = cmd.runCommand(clearCommand);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				log.error(e);
			}
			for (String clearResult : clearResultList) {
				log.info(clearResult);
				if(clearResult.contains("Success")){
					
					String uninstallCommand = "adb -s "+deviceId+" uninstall "+appPackage; // 0123456789ABCD // // adb -s TA93300GXV uninstall in.redbus.android
					log.info(uninstallCommand);
					cmd.runCommand(uninstallCommand);
					List<String> uninstallResultList = null;
					try {
						uninstallResultList = cmd.runCommand(clearCommand);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						log.error(e);
					}
					for (String uninstallResult : uninstallResultList) {
						log.info(uninstallResult);
						if(uninstallResult!=null){
							status = true;
						}
					}
				}
			}
			
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			log.error(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error(e);
		}catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e);
		}
		return status;
	}

	@Override
	public AppPojo getAppInfoService( AppPojo app) throws IOException {
		// TODO Auto-generated method stub
		//File apkFile= new File(appLocation);
		cmd = new CommandPrompt();
		
		List<String> resultList = null;
		try {
			resultList = cmd.runCommand(buildToolPath+"/aapt dump badging "+app.getDownloadedAppPath());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			log.error(e);
		}
		String packageName = null;
		String activityName = null;
		String versionName = null;
		String iconPath = null;
		for (String result : resultList) {
			//log.info(result);
			int ind = result.indexOf("package: name=");
            if (ind >= 0) {
            	log.debug(result);
            	String[] temp=result.split("=");
            	String[] temp2=temp[1].split(" ");
            	packageName = temp2[0];
            	packageName=packageName.replace("'","");
            	app.setAppPackageName(packageName);
               
            }
            int ind1 = result.indexOf("versionName=");
            if (ind1 >= 0) {
            	log.debug(result);
            	String[] temp=result.split("=");
            	String[] temp2=temp[3].split(" ");
            	versionName = temp2[0];
            	versionName=versionName.replace("'","");
            	app.setBuildId(versionName);
               
            }
           
            int ind2 = result.indexOf("launchable-activity: name=");
            if (ind2 >= 0) {
            	log.debug(result);
            	String[] temp=result.split("=");
            	String[] temp2=temp[1].split(" ");
            	activityName = temp2[0];
            	activityName=activityName.replace("'","");
            	app.setActivityName(activityName);
               
            } 
            int ind3 = result.indexOf("icon=");
            if (ind3 >= 0) {
            	log.info(result);
            	String iconPathVar=result.substring(result.lastIndexOf("=") + 1);
            	/*String[] temp=result.split(" ");*/
            	try{
            		/*String[] temp2=iconPathVar.split("=");
	            	iconPath = temp2[1];*/
            		if(!iconPathVar.isEmpty()){
            			iconPath=iconPathVar.replace("'","");
            			if(!iconPath.isEmpty()){
            				app.setIconPath(iconPath);
        	            	log.info("icon= "+iconPath);
            			}
            			
            		}
	            	
            	}catch(ArrayIndexOutOfBoundsException e ){
            		log.error(e);
            	}
            	
               
            }
	            
		}
		
		
		
		app.setDefaultIcon("./images/apps/android-red2.png");
		
		if(activityName == null && app.getActivityName()== null){
    		try {
				app=ApkToolUtil.getActivityNameFromFile(app);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	} 
		
		
		log.info("Package Name	>>>>>>>>"+app.getAppPackageName());
		log.info("Activity Name	>>>>>>>>"+app.getActivityName());
		log.info("Icon Path	>>>>>>>>"+app.getIconPath());
		log.info("App Build No	>>>>>>>>"+app.getBuildId());
		return app;
	}

	@Override
	public AppPojo getMultipartDataFromRequest(HttpServletRequest request, AppPojo app) {
		
		// TODO Auto-generated method stub
		log.info("You are trying to upload");

		DiskFileItemFactory factory = new DiskFileItemFactory();
		// maximum size that will be stored in memory
	    factory.setSizeThreshold(maxMemSize);
	    // Location to save data that is larger than maxMemSize.
	    factory.setRepository(new File(FileUtils.getUserDirectory()+"\\AppData\\Local\\Temp\\"));
		ServletFileUpload upload = new ServletFileUpload(factory);
		// maximum file size to be uploaded.
	    upload.setSizeMax( maxFileSize );
		try {
			
			List<FileItem> fields = upload.parseRequest(request);
			//log.info("Number of fields: " + fields.size() );
			Iterator<FileItem> it = fields.iterator();
			FileItem fileItem= null;
			if (!it.hasNext()) {
				log.error("No fields found");
			//	return;
			}else {
				while (it.hasNext()) {
					
					fileItem = it.next();
					boolean isFormField = fileItem.isFormField();
					if (isFormField) {
						log.info("regular form fields \n FIELD NAME: " + fileItem.getFieldName() + 
								" " + fileItem.getString()
								);
						if(fileItem.getFieldName().contains("userId")){
							app.setUserId(fileItem.getString());
							
						}else if (fileItem.getFieldName().contains("projectId")) {
							app.setProjectId(fileItem.getString());
							
						}else if (fileItem.getFieldName().contains("fileType")) {
							app.setFileType(fileItem.getString());
							
						}else if (fileItem.getFieldName().contains("appName")) {
							app.setAppName(fileItem.getString());
							
						}else if (fileItem.getFieldName().contains("appVersion")) {
							app.setVersion(fileItem.getString());
							
						}
						
					} else {
						log.info("file form fields \n FIELD NAME: " + fileItem.getFieldName() +
								"   " + fileItem.getName() 
								);
						
						app.setFileName(fileItem.getName());
					}
				}
			}
			
			
			// Write the file
            if( app.getFileName()!= null){
               file = new File( factory.getRepository()+""+app.getFileName());
               app.setFilePath(file.getAbsolutePath());
               app.setDownloadedAppPath(file.getAbsolutePath());
            }else{
            	log.error("File Name Null");
            }
            fileItem.write( file ) ;
		} catch (FileUploadException e) {
			log.error(e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e);
		}
		return app;
	}
	@Override
	public void deleteAppFromTemp(AppPojo app) {
		// TODO Auto-generated method stub
		File file = new File(app.getDownloadedAppPath());

		if(file.delete()){
			log.info(file.getName() + " is deleted!");
		}else{
			log.error("Delete operation is failed.");
		}
	}

	@Override
	public boolean appLaunchService(String deviceId,  AppPojo app)  {
		boolean status=false;
	
		try {
			
			appPackage = app.getAppPackageName();
			String launchCommand = "adb -s "+deviceId+" shell monkey -p "+appPackage+" -c android.intent.category.LAUNCHER 1";
			//"adb shell am start -W "+appPackage; // 0123456789ABCD // // adb shell am start -w in.redbus.android
			// adb shell monkey -p com.instagram.android -c android.intent.category.LAUNCHER 1
			// for multiple devices
			// adb -s 0123456789ABCD shell monkey -p in.redbus.android -c android.intent.category.LAUNCHER 1
			log.info("Command >> " + launchCommand);
			cmd = new CommandPrompt();
			List<String> rsList = cmd.runCommand(launchCommand);
			if(rsList!=null){
				status= true;
			}
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			log.error(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error(e);
		}
		
		return status;
	}
	
	@Override
	public boolean browserLaunchService(String deviceId, AppPojo app) {
		boolean status=false;
		try {
			cmd = new CommandPrompt();
			String persistCommand = "adb -s "+deviceId+" shell am set-debug-app --persistent com.android.chrome";
			log.info("Command >> " + persistCommand);
			cmd.runCommand(persistCommand);
			
			String launchCommand = "adb -s "+deviceId+" shell am start -a android.intent.action.VIEW -d "+app.getWebUrl();
			//String launchCommand = "adb -s "+deviceId+" shell am start -n com.android.chrome/com.google.android.apps.chrome.Main -d "+app.getWebUrl();
			//adb shell am start -n com.android.chrome/com.google.android.apps.chrome.Main
			// for multiple devices
			// adb -s 0123456789ABCD shell am start -n com.android.chrome/com.google.android.apps.chrome.Main
			log.info("Command >> " + launchCommand);
			cmd.runCommand(launchCommand);
			status= true;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			log.error(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error(e);
		}
		
		return status;
	}
	@Override
	public AppPojo getFileFromRequest(HttpServletRequest request, AppPojo app) throws IOException, ServletException {
		// TODO Auto-generated method stub
		String savePath=FileUtils.getUserDirectory()+"\\AppData\\Local\\Temp\\";
		for (Part part : request.getParts()) {
			
			app.setFileName(Utils.extractFileName(part));
			//app.setFileIS(part.getInputStream());
			app.setDownloadedAppPath(savePath + app.getFileName());
			log.info(app.getDownloadedAppPath());
			part.write(app.getDownloadedAppPath());
		}
		
		return app;
	}

	@Override
	public boolean browserCleanUpService(String deviceId) {
		boolean status=false;
		try {
			cmd = new CommandPrompt();
			String clearCommand = "adb -s "+deviceId+" shell pm clear com.android.chrome";
			log.info("Command >> " + clearCommand);
			cmd.runCommand(clearCommand);
			
			status= true;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			log.error(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error(e);
		}
		
		return status;
	}

	@Override
	public boolean appResignService(AppPojo app) {
		boolean status=false;
		try {
			cmd = new CommandPrompt();
			String clearCommand = "jarsigner -verbose -sigalg MD5withRSA -digestalg SHA1 -keystore "+KEY_STORE+" -storepass "+STORE_PASS+" -keypass "+KEY_PASS+" "+app.getDownloadedAppPath()+" "+KEY_ALIAS;
			log.info("Command >> " + clearCommand);
			List<String> lineList= cmd.runCommand(clearCommand);
			for (Iterator<String> iterator = lineList.iterator(); iterator.hasNext();) {
				String str = (String) iterator.next();
				//System.out.println(str);
				if(str.contains("jarsigner: attempt to rename") && str.contains("failed")){
					System.out.println(str);
					File signedFile=new File(app.getDownloadedAppPath()+".sig");
					if(signedFile.exists()){
						
						new ZipUtility().changeExtenion(signedFile.getAbsolutePath(), "", ".sig");
						status= true;
					}
					
					
				} else if(str.contains("signing:")){
					status= true;
				}
			}
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			log.error(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error(e);
		}
		
		return status;
	}

	@Override
	public boolean appUnsignService(AppPojo app) {
		boolean status=false;
		try {
			ZipUtility zip=new ZipUtility();
			File zipFile = zip.changeExtenion(app.getDownloadedAppPath(), ".zip", ".apk");
			String[] exts={"META-INF/"};
			File temp = null;
		    try {
		    	temp = zip.deleteZipEntry(zipFile,exts);
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   // System.out.println(temp.getAbsolutePath());
		    File signedApk = zip.changeExtenion(temp.getAbsolutePath(), ".apk", ".tmp");
		    if(signedApk!=null && signedApk.exists()){
		    	app.setDownloadedAppPath(signedApk.getAbsolutePath());
		    	temp.delete();
		    	status=true;
		    }
		   
	      
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error(e);
		}
		
		return status;
	}
	

}
