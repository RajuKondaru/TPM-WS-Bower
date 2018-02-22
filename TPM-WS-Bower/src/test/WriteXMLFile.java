package test;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
 
import org.w3c.dom.Attr;
import org.w3c.dom.Node;
 
public class WriteXMLFile {
 
 public static void main(String argv[]) {
 
   try { DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            
            org.w3c.dom.Document doc = docBuilder.newDocument();
            Node rootElement = doc.createElement("Connector");
            doc.appendChild(rootElement);
            Attr attr0 = doc.createAttribute("logo");
            attr0.setValue("TestPace");
            ((org.w3c.dom.Element) rootElement).setAttributeNode(attr0);
     
            // Connector elements
            org.w3c.dom.Element type = doc.createElement("Type");
            rootElement.appendChild(type);
            // set attribute to Connector element
            Attr attr1 = doc.createAttribute("value");
            attr1.setValue("tp");
            type.setAttributeNode(attr1);
            
            org.w3c.dom.Element url = doc.createElement("URL");
            rootElement.appendChild(url);
            Attr attr2 = doc.createAttribute("value");
            attr2.setValue("www.testpace.com");
            url.setAttributeNode(attr2);
            
            org.w3c.dom.Element username = doc.createElement("HTMLUserName");
            rootElement.appendChild(username);
            Attr attr3 = doc.createAttribute("value");
            attr3.setValue("raju");
            username.setAttributeNode(attr3);
            
            org.w3c.dom.Element password = doc.createElement("HTMLPassword");
            rootElement.appendChild(password);
            Attr attr4 = doc.createAttribute("value");
            attr4.setValue("raju1234");
            password.setAttributeNode(attr4);
            
            //transform data into xml
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("C:\\Users\\raju.kondaru\\Newfolder\\test.xml"));
            transformer.transform(source, result);  
} 
catch (ParserConfigurationException pce) {
  pce.printStackTrace();
   } catch (TransformerException tfe) {
  tfe.printStackTrace();
   }
 }
}