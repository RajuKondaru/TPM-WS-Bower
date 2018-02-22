package com.tpm.mobile.android.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.log4j.Logger;

public class FileUtil {
	private static final Logger log = Logger.getLogger(FileUtil.class);
	public String renameFileExtension (String source, String newExtension) {  
		String target;  
		String currentExtension = getFileExtension(source);  
		
		if (currentExtension.equals("")){  
			target = source + "." + newExtension;  
		} else {  
		    target = source.replaceAll("." + currentExtension, newExtension);  
		}  
		new File(source).renameTo(new File(target));
		
		return target;  
	}  
	  
	public String getFileExtension(String f) {  
		String ext = "";  
		int i = f.lastIndexOf('.');  
		if (i > 0 &&  i < f.length() - 1) {  
			ext = f.substring(i + 1).toLowerCase();  
		}  
		return ext;  
	}  
	public void copyFileUsingStream(File source, File dest) throws IOException {
	    InputStream is = null;
	    OutputStream os = null;
	    try {
	        is = new FileInputStream(source);
	        os = new FileOutputStream(dest);
	        byte[] buffer = new byte[1024];
	        int length;
	        while ((length = is.read(buffer)) > 0) {
	            os.write(buffer, 0, length);
	        }
	    } finally {
	        is.close();
	        os.close();
	    }
	}
	public void unzip(String zipFilePath, String destDir) throws IOException {
        File dir = new File(destDir);
        // create output directory if it doesn't exist
	    if(!dir.exists()) dir.mkdirs();
	    FileInputStream fis = null;
	    ZipInputStream zis = null ;
	    FileOutputStream fos = null;
	    //buffer for read and write data to file
	    byte[] buffer = new byte[1024];
	    try {
	        fis = new FileInputStream(zipFilePath);
	        zis = new ZipInputStream(fis);
	        ZipEntry ze = zis.getNextEntry();
	        while(ze != null){
	            String fileName = ze.getName();
	            File newFile = new File(destDir + File.separator + fileName);
	            //log.info("Unzipping to "+newFile.getAbsolutePath());
	            //create directories for sub directories in zip
	            
	            try{
	            	new File(newFile.getParent()).mkdirs();
	            	fos = new FileOutputStream(newFile);
	            	int len;
	            	while ((len = zis.read(buffer)) > 0) {
	     	            fos.write(buffer, 0, len);
	     	        }
	            }catch(Exception e){
	            	e.printStackTrace();
	            }finally {
	            	if(fos!=null){
	            		fos.close();
	            		//System.out.println("fos closed");
	            	}
	            	 
	            	 //close this ZipEntry
	 	            zis.closeEntry();
				}
	           
	           
	           
	            ze = zis.getNextEntry();
	        }
	        //close last ZipEntry
	        zis.closeEntry();
	       
	    } catch (IOException e) {
	        log.error(e);
	    }finally {
	    	if(zis!=null){
	    		 zis.close();
	    		 //System.out.println("zis closed");
	    	}
	    	if(fis!=null){
	    		 fis.close();
	    		 //System.out.println("fis closed");
	    	}
		    
		}
	     
	}
	public String tempFile(String fileName,  String ext) throws IOException{
		  
	    // by calling deleteOnExit the temp file is deleted when the jvm is 
	    // shut down
	    File tempFile2 = File.createTempFile(fileName, ext);
	    tempFile2.deleteOnExit();
	    System.out.format("Canonical filename: %s\n", tempFile2.getCanonicalFile());
	    return tempFile2.getAbsolutePath();
	}
	public File createTempDirectory() throws IOException	{
	    final File temp;

	    temp = File.createTempFile("temp", Long.toString(System.nanoTime()));

	    if(!(temp.delete()))
	    {
	        throw new IOException("Could not delete temp file: " + temp.getAbsolutePath());
	    }

	    if(!(temp.mkdir()))
	    {
	        throw new IOException("Could not create temp directory: " + temp.getAbsolutePath());
	    }

	    return (temp);
	}

}
