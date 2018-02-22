package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;
import org.json.JSONArray;

public class TestLogStream {
	private static final Logger log = Logger.getLogger(TestLogStream.class);
	public static JSONArray deviceLogs=null;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		deviceLogs = new JSONArray();
		new Thread(new Runnable() {
			public void run() {
				 InputStream is = null;
			        InputStreamReader isr = null;
			        BufferedReader br =null;
			        Process p = null;
				 try {
				        p = Runtime.getRuntime().exec("adb -s 5203c45ae8fea300 shell logcat");
				        is = p.getInputStream();
				        isr = new InputStreamReader(is);
				        br = new BufferedReader(isr);
				        String line;
				        while ((line = br.readLine()) != null) {
				        	if(!line.isEmpty()){
				        		log.info(line);
				                deviceLogs.put(line);
					            
				        	}
				        }
				        
				    } catch (IOException  e) {
				        log.error(e);
				    } finally {
				    	log.info(deviceLogs);
				    	try {
				    		if (br!=null) {
								br.close();
							}
							if (isr!=null) {
								isr.close();
							}
							if (is!=null){
								is.close();
							}
							if (p!=null){
								p.destroy();
							}
				    	} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
			}
		}).start();

	}

}
