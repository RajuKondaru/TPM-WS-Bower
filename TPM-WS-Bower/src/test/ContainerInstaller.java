package test;
import java.io.File; 
import java.io.FileOutputStream; 
import java.io.InputStream; 
import java.util.logging.Logger; 
import java.util.zip.ZipEntry; 
import java.util.zip.ZipInputStream; 
 
/**
 * Installs the container distribution and configuration before it will be started 
 */ 
public class ContainerInstaller { 
 
    private Logger log = Logger.getLogger(ContainerInstaller.class.getName()); 
 
   
 
    private void unzip(InputStream inputStream, File destination, boolean overwrite) { 
        try { 
            byte[] buf = new byte[1024]; 
            ZipInputStream zipinputstream = null; 
            ZipEntry zipentry; 
            zipinputstream = new ZipInputStream(inputStream); 
 
            zipentry = zipinputstream.getNextEntry(); 
            while (zipentry != null) { 
                int n; 
                FileOutputStream fileoutputstream; 
                File newFile = new File(destination, zipentry.getName()); 
                if (zipentry.isDirectory()) { 
                    newFile.mkdirs(); 
                    zipentry = zipinputstream.getNextEntry(); 
                    continue; 
                } 
 
                if (newFile.exists() && overwrite) { 
                    log.info("Overwriting " + newFile); 
                    newFile.delete(); 
                } 
 
                fileoutputstream = new FileOutputStream(newFile); 
 
                while ((n = zipinputstream.read(buf, 0, 1024)) > -1) { 
                    fileoutputstream.write(buf, 0, n); 
                } 
 
                fileoutputstream.close(); 
                zipinputstream.closeEntry(); 
                zipentry = zipinputstream.getNextEntry(); 
 
            } 
 
            zipinputstream.close(); 
        } catch (Exception e) { 
            throw new IllegalStateException("Can't unzip input stream", e); 
        } 
    } 
}