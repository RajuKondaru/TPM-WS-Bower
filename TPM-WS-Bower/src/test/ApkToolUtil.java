package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.tpm.mobile.android.pojo.AppPojo;

public class ApkToolUtil {
	
	public static void main(String[] args) throws InterruptedException, IOException {
		// TODO Auto-generated method stub
		
		ArrayList<String> lineList =null;
		Process p = null;
		BufferedReader r = null;
		String adbCommand ="C:\\AndroidSDK\\platform-tools\\apktool.bat d C:\\Users\\raju.kondaru\\Desktop\\Amazon\\Ebay.apk";
		
		String dest="C:\\Users\\raju.kondaru\\Desktop\\Amazon\\";
		try{
			p = Runtime.getRuntime().exec(adbCommand, null, new File(dest));
			r = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line="";
			
			lineList = new ArrayList<String>();
			line=r.readLine();
			while (line!=null ) {
				System.out.println(line);
				lineList.add(line);
				line=r.readLine();
			}
		} 	catch(Exception e){
    		e.printStackTrace();
    	}finally {
    		r.close();
        	p.destroy();
		}
		for (String string : lineList) {
			
		
			if(string.contains("Loading resource table from file")){
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
		
		

	}
	public static AppPojo getActivityNameFromFile(AppPojo app) throws InterruptedException, IOException {
		ArrayList<String> lineList =null;
		Process p = null;
		BufferedReader r = null;
		File fXmlFile = null;
		String appPath=app.getDownloadedAppPath();
		String adbCommand ="C:\\AndroidSDK\\platform-tools\\apktool.bat d "+appPath;
		//System.out.println(adbCommand);
		String appDirectory=appPath.substring(0, appPath.lastIndexOf("\\")+1);
		//System.out.println(appDirectory);
		String tempDirectoryName=app.getFileName().substring(0, app.getFileName().lastIndexOf("."));
		//System.out.println(tempDirectoryName);
		//String dest=appDirectory+tempDirectoryName;
		File tempFile= new File(appDirectory+tempDirectoryName);
		//System.out.println(tempFile.getAbsolutePath());
		try{
			p = Runtime.getRuntime().exec(adbCommand, null, new File(appDirectory));
			r = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line="";
			
			lineList = new ArrayList<String>();
			line=r.readLine();
			while (line!=null ) {
				//System.out.println(line);
				lineList.add(line);
				line=r.readLine();
			}
		
			for (String string : lineList) {
				
			
				if(string.contains("Loading resource table from file")){
				    try {
				    	fXmlFile = new File(appDirectory+tempDirectoryName+"\\AndroidManifest.xml");
						DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
						DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
						Document doc = dBuilder.parse(fXmlFile);
	
						//optional, but recommended
						//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
						doc.getDocumentElement().normalize();
	
						//System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
	
						NodeList nList = doc.getElementsByTagName("activity-alias");
	
						//System.out.println("----------------------------");
	
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
											//System.out.println("Activity Name : " + eElement.getAttribute("android:name"));
											System.out.println("Target Activity Name: " + eElement.getAttribute("android:targetActivity"));
											app.setActivityName(eElement.getAttribute("android:targetActivity"));
											break;
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
		} 	catch(Exception e){
    		e.printStackTrace();
    	}finally {
    		if(r!=null){
    			r.close();
    		}
    		if(p!=null){
    			p.destroy();
    		}
    		if(tempFile!=null && tempFile.isDirectory()){
    			tempFile.delete();
    		}
    		
        	
		
        	
		}
		return app;
	}

}
