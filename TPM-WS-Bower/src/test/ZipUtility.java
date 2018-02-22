package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilePermission;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipUtility {
	public File change = null;
	public String ext =null;
	public  File changeExt(String targetFilePath, String targetExtension, String replaceExt) throws IOException {
		 if(targetFilePath!=null ) {//1
		        String ext =targetFilePath.substring(targetFilePath.lastIndexOf("."));
		        System.out.println(ext);
		        if(ext.equalsIgnoreCase(replaceExt)) {//2
		          File f = new File(targetFilePath);
		          //if the file exists
		          // then change the filename
		          if(f.exists()) {//3
		            targetFilePath=targetFilePath.replace(ext,targetExtension);
		           System.out.println(" "+targetFilePath);
		           change = new File(targetFilePath);
		           f.renameTo(change);
		          }//3
		          //if file does not exists then create a new file
		          else {//3
			          try {
			            f.createNewFile();
			            targetFilePath=targetFilePath.replace(ext,targetExtension);
			            System.out.println(" "+targetFilePath);
			  
			            change = new File(targetFilePath);
			  
			            f.renameTo(change);
			           
			          }//try
			          catch(IOException ioe) {
			        	 ioe.printStackTrace();
			          }//catch
		                    
		          }//3
		         
		  
		      }//2
		  
		    }//1
		 return change;

	}
	
	public File deleteZipEntry(File zipFile, String[] fileName) throws IOException, InterruptedException {
		       // get a temp file
		String zipPath=zipFile.getAbsolutePath();
		String zipDirc =zipPath.substring(0,zipPath.lastIndexOf("\\"));
		System.out.println(zipDirc);
		String prefix =zipFile.getName().substring(0,zipFile.getName().lastIndexOf("."));
		
		File tempFile = new File(zipDirc+"\\"+prefix+".tmp");
		byte[] buf = new byte[1024];

		ZipInputStream zin = new ZipInputStream(new FileInputStream(zipFile));
		ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(tempFile));
		
		ZipEntry entry = zin.getNextEntry();
		while (entry != null) {
			
			
		    String name = entry.getName();
		   
		    boolean toBeDeleted = false;
		    for (String file : fileName) {
		    	if (name.contains(file)) {
		        	System.out.println(name);
		            toBeDeleted = true;
		           // break;
		        }
			}
		    
		        
		    
		    if (!toBeDeleted) {
		        // Add ZIP entry to output stream.
		        zout.putNextEntry(new ZipEntry(name));
		        // Transfer bytes from the ZIP file to the output file
		        int len;
		        while ((len = zin.read(buf)) > 0) {
		            zout.write(buf, 0, len);
		        }
		    }
		    entry = zin.getNextEntry();
		}
		// Close the streams        
		zin.close();
		// Compress the files
		// Complete the ZIP file
		zout.close();
		zipFile.delete();
	
		
		return tempFile;
		
	}
	public File changeExtenion(String targetFilePath, String targetExtension, String replaceExt)  throws IOException {
		File dest = null;
		 if(targetFilePath!=null ) {//1
		        String ext =targetFilePath.substring(targetFilePath.lastIndexOf("."));
		        //System.out.println(ext);
		        if(ext.equalsIgnoreCase(replaceExt)) {//2
		          File f = new File(targetFilePath);
		          //if the file exists
		          // then change the filename
		          if(f.exists()) {//3
		            String destinationFilePath=targetFilePath.replace(ext,targetExtension);
		            FilePermission permission = new FilePermission(targetFilePath, "read");
		            dest = new File(destinationFilePath);
		            rename(f, dest);
		            f.delete();
		          }
		        }
		 }
		 return dest;
	}
	public void rename(File source, File dest) throws IOException {
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
}
