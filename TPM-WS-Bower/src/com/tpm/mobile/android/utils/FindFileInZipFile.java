package com.tpm.mobile.android.utils;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
 
public class FindFileInZipFile {
	private static final Logger log = Logger.getLogger(FindFileInZipFile.class);
	static BufferedImage bgTileSprite=null;
    public BufferedImage getImage(String filePath, String searchFileName){
         try{
            //open the source zip file
            ZipFile sourceZipFile = new ZipFile(filePath);
            
            //File we want to search for inside the zip file
            //String searchFileName = "icon.png";
 
                        //get all entries                      
            Enumeration<?> e = sourceZipFile.entries();
            boolean found = false;
           
            log.info("Trying to search " + searchFileName + " in " + sourceZipFile.getName());
           
            while(e.hasMoreElements()){
                ZipEntry entry = (ZipEntry)e.nextElement();
               
                /*
                 * Here, normal compare would not work.
                 *
                 * Because zip might contain directories so the entry name will not
                 * match extactly with the file name we want to search.
                 *
                 * Additionally, there might be more than one file with the same
                 * name in different directories inside the zip archive.
                 *
                 * So approch here is to search using indexOf and not using
                 * equals or equalsIgnoreCase methods.
                 */
               /* if(entry.getName().toLowerCase().indexOf(searchFileName) != -1)
                {
                        found = true;
                        log.info("Found " + entry.getName());
                       
                        
                         * if you want to search only first instance, uncomment the
                         * following break statement.
                         
                         //res/drawable-hdpi-v4/ads_manager_icon.png
                        //break; 
                        String [] data=entry.getName().split("/");
                        // 
                        if( data[2].equalsIgnoreCase("icon.png") || entry.getName().contains("launcher")){
                        	ZipFileHandler zfh = new ZipFileHandler(filePath);
                        	InputStream in = new ByteArrayInputStream(zfh.getEntry(entry.getName()));

                            try
                            {
                                bgTileSprite = ImageIO.read(in);
                                log.info(bgTileSprite);
                            }
                            catch (IOException ex)
                            {
                                log.error("Could not convert zipped image bytearray to a BufferedImage."+ex);
                            }
                         break;
                        }
                        
                        
                }*/
                
                
                
                if(entry.getName().equalsIgnoreCase(searchFileName)){
                	found = true;
                	log.info("Found " + entry.getName());
                    ZipFileHandler zfh = new ZipFileHandler(filePath);
                    
                	InputStream in = new ByteArrayInputStream(zfh.getEntry(entry.getName()));

                    try{
                        bgTileSprite = ImageIO.read(in);
                        log.info(bgTileSprite);
                    }catch (IOException ex){
                    	 log.error("Could not convert zipped image bytearray to a BufferedImage."+ex);
                    }finally {
						if(in!=null){
							in.close();
						}
					}
                    break;
                }
            }
           
            if(found == false){
            	 log.warn("File :" + searchFileName + " Not Found Inside Zip File: " + sourceZipFile.getName());
            }
            //close the zip file
            sourceZipFile.close();
         }catch(IOException ioe){
            	 log.error("Error opening zip file" + ioe);
         }
		return bgTileSprite;
    }
 
        
    public static void main(String args[]){
         try{
            //open the source zip file
            ZipFile sourceZipFile = new ZipFile("E:/pc4.apk");
           
            //File we want to search for inside the zip file
            String searchFileName = "icon.png";
 
                        //get all entries                      
            Enumeration<?> e = sourceZipFile.entries();
            boolean found = false;
           
            log.info("Trying to search " + searchFileName + " in " + sourceZipFile.getName());
           
            while(e.hasMoreElements()){
                ZipEntry entry = (ZipEntry)e.nextElement();
               
                /*
                 * Here, normal compare would not work.
                 *
                 * Because zip might contain directories so the entry name will not
                 * match extactly with the file name we want to search.
                 *
                 * Additionally, there might be more than one file with the same
                 * name in different directories inside the zip archive.
                 *
                 * So approch here is to search using indexOf and not using
                 * equals or equalsIgnoreCase methods.
                 */
                if(entry.getName().toLowerCase().indexOf(searchFileName) != -1){
                    found = true;
                    log.info("Found " + entry.getName());
                   
                    /*
                     * if you want to search only first instance, uncomment the
                     * following break statement.
                     */
                     
                    //break;    
                    if(entry.getName().contains("icon.png")){
                    	ZipFileHandler zfh = new ZipFileHandler("E:/pc4.apk");
                    	InputStream in = new ByteArrayInputStream(zfh.getEntry(entry.getName()));

                        try{
                            bgTileSprite = ImageIO.read(in);
                            log.info(bgTileSprite);
                        }catch (IOException ex){
                            log.error("Could not convert zipped image bytearray to a BufferedImage."+ex);
                        }
                        return;
                    }
                }
            }
            if(found == false){
            	log.warn("File :" + searchFileName + " Not Found Inside Zip File: " + sourceZipFile.getName());
            }
            //close the zip file
            sourceZipFile.close();
   
         }catch(IOException ioe){
            log.error("Error opening zip file" + ioe);
         }
    }
 
}