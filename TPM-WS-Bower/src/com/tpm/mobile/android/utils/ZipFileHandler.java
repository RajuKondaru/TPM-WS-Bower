package com.tpm.mobile.android.utils;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.log4j.Logger;

public class ZipFileHandler{
	private static final Logger log = Logger.getLogger(ZipFileHandler.class);
	private ZipFile zipFile;

	public ZipFileHandler(String zipFileLocation)	{
	    try{
	        zipFile = new ZipFile(zipFileLocation);
	    }catch (IOException e) {
	    	log.error("Unable to load zip file at location: " + zipFileLocation);
	    }
	}
	
	public byte[] getEntry(String filePath){
	    ZipEntry entry = zipFile.getEntry(filePath);
	    int entrySize = (int)entry.getSize();
	    try{
	        BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));
	        byte[] finalByteArray = new byte[entrySize];
	
	        int bufferSize = 2048;
	        byte[] buffer = new byte[2048];
	        int chunkSize = 0;
	        int bytesRead = 0;
	
	        while(true){
	            //Read chunk to buffer
	            chunkSize = bis.read(buffer, 0, bufferSize); //read() returns the number of bytes read
	            if(chunkSize == -1){
	                //read() returns -1 if the end of the stream has been reached
	                break;
	            }
	            //Write that chunk to the finalByteArray
	            //System.arraycopy(src, srcPos, dest, destPos, length)
	            System.arraycopy(buffer, 0, finalByteArray, bytesRead, chunkSize);
	            bytesRead += chunkSize;
	        }
	        bis.close(); //close BufferedInputStream
	        log.info("Entry size: " + finalByteArray.length);
	        return finalByteArray;
	    }
	    catch (IOException e){
	        log.error("No zip entry found at: " + filePath);
	        return null;
	    }
	}
}