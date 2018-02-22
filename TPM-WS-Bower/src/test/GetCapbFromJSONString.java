package test;

import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class GetCapbFromJSONString {
	public static void main(String[] args) throws ParseException {
		String capJsonSrt="--> POST /wd/hub/session {\"desiredCapabilities\":{\"platformVersion\":\"5.1.1\",\"platformName\":\"Android\",\"deviceName\":\"samsung-SM-T280\",\"appActivity\":\"com.flipkart.android.SplashActivity\",\"api_key\":\"a238c551-eb4b-497a-a75d-b8d4a20b9f26\",\"udid\":\"5203c45ae8fea300\",\"appPackage\":\"com.flipkart.android\"}}";
		capJsonSrt=capJsonSrt.substring(capJsonSrt.indexOf("{"));
		JSONParser parser = new JSONParser(); 
		JSONObject json = (JSONObject) parser.parse(capJsonSrt);
		if(json.containsKey("desiredCapabilities")){
			JSONObject jsonDc=(JSONObject) json.get("desiredCapabilities");
			if(jsonDc.containsKey("platformVersion")){
				System.out.println("platformVersion :: "+ jsonDc.get("platformVersion"));
			}
			if(jsonDc.containsKey("platformName")){
				System.out.println("platformName :: "+ jsonDc.get("platformName"));
			}
			if(jsonDc.containsKey("deviceName")){
				System.out.println("deviceName :: "+ jsonDc.get("deviceName"));
			}
			if(jsonDc.containsKey("appActivity")){
				System.out.println("appActivity :: "+ jsonDc.get("appActivity"));
			}
			if(jsonDc.containsKey("api_key")){
				System.out.println("api_key :: "+ jsonDc.get("api_key"));
			}
			if(jsonDc.containsKey("udid")){
				System.out.println("udid :: "+ jsonDc.get("udid"));
			}
			if(jsonDc.containsKey("appPackage")){
				System.out.println("appPackage :: "+ jsonDc.get("appPackage"));
			}
			
		}
		
		
		}
}
