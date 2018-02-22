package test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;

public class Flipkart_RealDevice {
	AndroidDriver<?> driver;
	Dimension size;	
	@SuppressWarnings("rawtypes")
	@BeforeTest
	public void setUp() throws MalformedURLException
	{
	  
		
		 DesiredCapabilities capabilities = new DesiredCapabilities();

		capabilities.setCapability("api_key","");
		
		capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION,"5.1");
		
		capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME,"Android");
		
		capabilities.setCapability(MobileCapabilityType.UDID,"TA64301YVY");
		
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME,"motorola-XT1052");
		
		capabilities.setCapability("appActivity","com.flipkart.android.SplashActivity");
		
		capabilities.setCapability("appPackage","com.flipkart.android");

		driver = new AndroidDriver(new URL("http://appium.testpace.com:2881/wd/hub"),capabilities);
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
    }
	
	 @Test(priority=0)
	 public void Login() throws InterruptedException, IOException 
	 {	 
		
			 driver.findElement(By.xpath("//android.widget.ImageButton[contains(@resource-id,'btn_skip')]")).click();
		 	Thread.sleep(2000);
	    	driver.findElement(By.xpath("//android.widget.ImageButton[@content-desc='Open Drawer']")).click();
	    	Thread.sleep(2000);
	    	driver.findElement(By.xpath(" //android.widget.TextView[@text='My Account']")).click();
	    	Thread.sleep(2000);
	    	System.out.println("Account Login");
	    	driver.findElement(By.xpath("//android.widget.ImageButton[contains(@resource-id,'clbt')]")).click();
	    	Thread.sleep(2000);
	    	driver.findElement(By.xpath("//android.widget.EditText[contains(@resource-id,'mobileNo')]")).sendKeys("8328291269");
			Thread.sleep(2000);
			driver.findElement(By.xpath("//android.widget.EditText[contains(@resource-id,'et_password')]")).clear();
			Thread.sleep(2000);
			driver.findElement(By.xpath("//android.widget.EditText[contains(@resource-id,'et_password')]")).sendKeys("8328291269");
			Thread.sleep(2000);
			driver.findElement(By.xpath("//android.widget.Button[contains(@resource-id,'btn_mlogin')]")).click();			
			Thread.sleep(2000);
			/*driver.findElement(By.xpath("//android.widget.ImageButton[contains(@resource-id,'btn_skip')]")).click();
			Thread.sleep(2000);
	    	driver.findElement(By.xpath("//android.widget.TextView[@text='Home']")).click();
	    	*/
		 }
		 

	
	@AfterTest
	public void tearDown()
	{
		driver.quit();  
	}
}
