package com.tpm.mobile.android.utils;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.tpm.mobile.android.pojo.AppPojo;



public class S3Objects {
	private static final Logger log = Logger.getLogger(S3Objects.class);
	private static Properties props;
	private S3ObjectInputStream objectContent;
	private static String s3_BucketName;
	private static String s3_AccessKey;
	private static String s3_SecretKey;
	private String downloadedAppPath;
	private S3Object s3object ;
	static{
		 try {
	        	props = new Properties();
		        props.load(S3Objects.class.getClassLoader().getResourceAsStream("aws.properties"));
		        s3_AccessKey = props.getProperty("accessKey");
		        s3_SecretKey = props.getProperty("secretKey");
		        s3_BucketName = props.getProperty("bucketname");
				log.info("s3_AccessKey  >>> "+s3_AccessKey);
				log.info("s3_SecretKey  >>> "+s3_SecretKey);
				log.info("s3_BucketName  >>> "+s3_BucketName);
		    } catch (IOException e) {
	            log.warn(e);
	        }
	}
     public String downloadApp(AppPojo app) throws IOException{
    	try{
    		log.debug(s3_AccessKey+"<<<<<<<<<<<<<>>>>>>>>>>" + s3_SecretKey);
    		GetObjectRequest request = new GetObjectRequest(s3_BucketName , app.getFilePath());
    		AmazonS3 s3Client = new AmazonS3Client( new BasicAWSCredentials(s3_AccessKey, s3_SecretKey) );
    		
    		
    		log.debug(s3_BucketName +"<<<<<<<<<<<<<>>>>>>>>>>" + app.getFilePath());
	        s3object = s3Client.getObject(request);
	       
            log.info("Content-Type: " + s3object.getObjectMetadata().getContentType());
            objectContent = s3object.getObjectContent();
            downloadedAppPath= FileUtils.getUserDirectory()+"/AppData/Local/Temp/"+app.getFileName()+".apk";
            FileUtils.copyInputStreamToFile(objectContent, new File(downloadedAppPath));
            log.info("App Path >> " + downloadedAppPath);
           
            app.setDownloadedAppPath(downloadedAppPath);
            String url = getSingnedURLKey(s3Client, s3_BucketName, app.getFilePath());
			log.info("Received response:"+url);
        }catch(AmazonS3Exception s3){
            log.warn("Received error response:"+s3.getMessage());
        }catch (Exception e) {
			// TODO: handle exception
        	e.printStackTrace();
        	
		}finally {
			if (objectContent != null) {
				objectContent.close();
				
			}
			objectContent.close();
			if (s3object != null) {
				s3object.close();
			}
		}
       
       return downloadedAppPath;
    }
    //  <MaxKeys>1000</MaxKeys>
    private static String getSingnedURLKey(AmazonS3 s3Client, String s3_BucketName, String folderPath_fileName) {
        String folderPath = folderPath_fileName.substring(0,folderPath_fileName.lastIndexOf("/"));      
        ObjectListing folderPath_Objects = s3Client.listObjects(s3_BucketName, folderPath);

        List<S3ObjectSummary> listObjects = folderPath_Objects.getObjectSummaries();
        for(S3ObjectSummary object : listObjects){
            if(object.getKey().equalsIgnoreCase(folderPath_fileName)){
                return getSignedURLforS3File(s3Client, s3_BucketName, folderPath_fileName);
            }
        }
        return "The specified key does not exist.";
    }

    //  providing pre-signed URL to access an object w/o any AWS security credentials.
   //   Pre-Signed URL = s3_BucketName.s3.amazonaws.com/folderPath_fileName?AWSAccessKeyId=XX&Expires=XX&Signature=XX
    public static String getSignedURLforS3File(AmazonS3 s3Client, String s3_BucketName, String folderPath_fileName){
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(s3_BucketName, folderPath_fileName, HttpMethod.GET);
        request.setExpiration( new Date(System.currentTimeMillis() + 1000 * 60 * 15) ); // Default 15 min

        String url = s3Client.generatePresignedUrl( request ).toString();
        log.info("Pre-Signed URL = " + url);
        return url;
    }


    /*public void uploadApp(String folderPath_fileName, File appFile) 
            throws AmazonServiceException, AmazonClientException, InterruptedException{
    	
    	
    	TransferManager tm = new TransferManager(new AmazonS3Client( new BasicAWSCredentials(s3_AccessKey, s3_SecretKey) ));
        PutObjectRequest putObjectRequest = 
                new PutObjectRequest(s3_BucketName, folderPath_fileName, appFile);
        Upload myUpload = tm.upload( putObjectRequest );
        myUpload.waitForCompletion();//block the current thread and wait for your transfer to complete.
        tm.shutdownNow();            //to release the resources once the transfer is complete.
    }*/
    
    
    
    /*
    
    public static void main(String[] args) throws IOException, AmazonServiceException, AmazonClientException, InterruptedException {
    	AppPojo app=new AppPojo();
    	app.setFilePath("uploads/4/1/manualmobileconfig/RedBus.apk");
    	app.setFileName("RedBus");
        S3Objects s3o= new S3Objects();
        s3o.downloadApp(app );
        
    }*/

}
