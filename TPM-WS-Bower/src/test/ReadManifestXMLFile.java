package test;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

public class ReadManifestXMLFile {

  public static void main(String argv[]) {

    try {

	File fXmlFile = new File("C:\\Users\\raju.kondaru\\Desktop\\Amazon\\Ebay\\AndroidManifest.xml");
	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	Document doc = dBuilder.parse(fXmlFile);

	//optional, but recommended
	//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
	doc.getDocumentElement().normalize();

	System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

	NodeList nList = doc.getElementsByTagName("activity-alias");

	System.out.println("----------------------------");

	for (int temp = 0; temp < nList.getLength(); temp++) {

		Node nNode = nList.item(temp);

		//System.out.println("\nCurrent Element :" + nNode.getNodeName());

		if (nNode.getNodeType() == Node.ELEMENT_NODE) {

			Element eElement = (Element) nNode;
			NodeList cList = eElement.getElementsByTagName("category");
			for (int c = 0; c < cList.getLength(); c++) {
				Node cNode = cList.item(c);
				//System.out.println("\nCurrent Element :" + cNode.getNodeName());

				if (cNode.getNodeType() == Node.ELEMENT_NODE) {
					Element cElement = (Element) cNode;
					//System.out.println("Activity Name : " + cElement.getAttribute("android:name"));
					if( cElement.getAttribute("android:name").equalsIgnoreCase("android.intent.category.LAUNCHER")){
						System.out.println("Activity Name : " + eElement.getAttribute("android:name"));
						System.out.println("Target Activity Name: " + eElement.getAttribute("android:targetActivity"));
						return;
					}
					

					
				}
			}
			
			
			
			
			
		}
	}
    } catch (Exception e) {
	e.printStackTrace();
    }
  }

}