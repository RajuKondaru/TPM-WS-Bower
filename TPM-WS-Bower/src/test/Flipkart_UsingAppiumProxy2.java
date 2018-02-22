package test;

import java.net.MalformedURLException;

import java.net.URL;

import java.util.concurrent.TimeUnit;

 

import org.openqa.selenium.By;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;

 

public class Flipkart_UsingAppiumProxy2 {
	static AppiumDriver<WebElement> driver;
	static Dimension size;           
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws MalformedURLException {
		DesiredCapabilities capabilities = new DesiredCapabilities();

		capabilities.setCapability("api_key","61534ca2-27a2-4cc5-84ad-5ad3cdcfeffa");

		capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION,"5.1.1");

		capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME,"Android");

		capabilities.setCapability(MobileCapabilityType.UDID,"5203c45ae8fea300");

		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME,"samsung-SM-T280");

		capabilities.setCapability("appPackage","com.flipkart.android.SplashActivity");

		capabilities.setCapability("appActivity","com.flipkart.android");

		

		//driver = new AndroidDriver(new URL("http://predev.appium.testpace.com:4723/wd/hub"),capabilities);
		driver = new AndroidDriver<WebElement>(new URL("http://localhost:8080/TestPaceMobile/ws/hub"),capabilities);
		
		//driver = new AndroidDriver(new URL("http://localhost:4723/wd/hub"),capabilities);
		try {
			 driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			/* driver.findElement(By.name("Open Drawer")).click();
			 driver.findElement(By.name("My Account")).click();
			 driver.findElement(By.id("com.flipkart.android:id/mobileNo")).sendKeys("8328291269");
			 driver.findElement(By.id("com.flipkart.android:id/et_password")).sendKeys("8328291269");
			 driver.findElement(By.id("com.flipkart.android:id/btn_mlogin")).click();  */
			 try {
				 driver.findElement(By.id("com.flipkart.android:id/btn_skip")).click();
			} catch (Exception e) {
				// TODO: handle exception
			}
			 
			
			/* driver.findElement(By.id("com.flipkart.android:id/btn_mlogin")).click();
		        driver.findElement(By.id("com.flipkart.android:id/mobileNo")).clear();
		        Thread.sleep(3000);
		        driver.findElement(By.xpath("//android.widget.Button[@text='None of the above']")).click();
		        driver.findElement(By.id("com.flipkart.android:id/mobileNo")).sendKeys("8328291269");
		      	driver.findElement(By.id("com.flipkart.android:id/et_password")).sendKeys("8328291269");
		        driver.findElement(By.id("com.flipkart.android:id/btn_mlogin")).click();*/
		        
		        driver.findElement(By.xpath("//android.widget.TextView[@text=' Search for Products, Brands and More']")).click();
		        driver.findElement(By.xpath("//android.widget.EditText[@text='Search for Products, Brands..']")).sendKeys("iphone7 ");
		        
		        driver.findElement(By.xpath("//android.widget.TextView[@text='iphone 7 in Mobiles']")).click();
		        
		        driver.findElement(By.xpath("//android.widget.TextView[@text='Apple iPhone 7 (Black, 32 GB)']")).click();
		        driver.findElement(By.xpath("//android.widget.TextView[@text='ADD TO CART']")).click();
		        driver.findElement(By.xpath("//android.widget.TextView[@text='GO TO CART']")).click();
		        driver.findElement(By.xpath("//android.view.View[@content-desc='CONTINUE']")).click();
		        driver.navigate().back();
		        driver.findElement(By.xpath("//android.view.View[@content-desc='Remove']")).click();
		        driver.findElement(By.xpath("//android.view.View[@content-desc='REMOVE']")).click();
		        driver.findElement(By.xpath("//android.view.View[@content-desc='Back Button']")).click();
		        driver.closeApp();

			
		} catch (Exception e) {
			// TODO: handle exception
			
			e.printStackTrace();

			 driver.closeApp();
		}
		 
	}
	
	

}
