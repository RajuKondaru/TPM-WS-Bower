package test;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.pdfcrowd.Client;
import com.pdfcrowd.PdfcrowdError;

public class PdfcrowdTest {
    public static void main(String[] args) {
        try 
        {
            FileOutputStream fileStream;     
 
            // create an API client instance
            Client client = new Client("Raju_konda", "632d35a80cdd7f7a220af01dc500fe5d");

            // convert a web page and save the PDF to a file
            fileStream = new FileOutputStream("testpace.pdf");
            client.convertURI("http://testpace.com", fileStream);
            fileStream.close();

         /*   // convert an HTML string and store the PDF into a byte array
            ByteArrayOutputStream memStream  = new ByteArrayOutputStream();
            String html = "<html><body>In-memory HTML.</body></html>";
            client.convertHtml(html, memStream);

            // convert an HTML file
            fileStream = new FileOutputStream("file.pdf");
            client.convertFile("/path/to/local/file.html", fileStream);
            fileStream.close();
*/
            // retrieve the number of credits in your account
            Integer ncredits = client.numTokens();
        }
        catch(PdfcrowdError why) {
            System.err.println(why.getMessage());
        }
        catch(IOException exc) {
            // handle the exception
        }
    }
}