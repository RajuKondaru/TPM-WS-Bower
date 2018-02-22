package test;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class InputStreamReaderDemo {

   public static void main(String[] args) throws IOException {
      FileInputStream fis = null;
      InputStreamReader isr = null;
      String s;
      
      try {
      
         // new input stream reader is created 
         fis = new FileInputStream("C:\\Users\\raju.kondaru\\Desktop\\Amazon\\AndroidManifest.xml");
         isr = new InputStreamReader(fis);
         
         // the name of the character encoding returned
         s = isr.getEncoding();
         System.out.print("Character Encoding: "+s);
         
      } catch (Exception e) {
      
         // print error
         System.out.print("The stream is already closed");
      } finally {
         
         // closes the stream and releases resources associated
         if(fis!=null)
            fis.close();
         if(isr!=null)
            isr.close();
      }   
   }
  
}