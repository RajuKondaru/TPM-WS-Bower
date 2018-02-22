package test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.remote.DesiredCapabilities;


import io.appium.java_client.android.AndroidDriver;
import org.testng.annotations.Test;
import io.appium.java_client.MobileElement;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.CapabilityType;
import org.testng.annotations.AfterMethod;

public class Flipkart_UsingAppiumProxy_testNg2 {
	private static AndroidDriver<MobileElement> driver;
	
	@Test
	public void f() {
		
		driver.findElement(By.xpath("//android.widget.ImageButton[@index='1']")).click();
	    driver.findElement(By.xpath("//android.widget.TextView[@text=' Search for Products, Brands and More']")).click();
        driver.findElement(By.xpath("//android.widget.EditText[@text='Search for Products, Brands..']")).sendKeys("iphone7 ");
        driver.findElement(By.xpath("//android.widget.TextView[@text='iphone 7 in Mobiles']")).click();
        driver.findElement(By.xpath("//android.widget.TextView[@text='Apple iPhone 7 (Black, 32 GB)']")).click();
        driver.findElement(By.xpath("//android.widget.TextView[@text='ADD TO CART']")).click();
        driver.findElement(By.xpath("//android.widget.TextView[@text='GO TO CART']")).click();
        driver.findElement(By.xpath("//android.view.View[@content-desc='CONTINUE']")).click();
        driver.navigate().back();
        driver.findElement(By.xpath("//android.view.View[@content-desc='Remove']")).click();
        driver.findElement(By.xpath("//android.widget.Button[@content-desc='REMOVE']")).click();
        driver.findElement(By.xpath("//android.widget.ImageButton[@content-desc='Back Button']")).click();
	}
	
	@Parameters({"deviceName_","UDID_", "Port_", "appPackage_", "appActivity_" })
	@BeforeTest(alwaysRun=true)
	public void beforeMethod( String deviceName_, String UDID_,String Port_, String appPackage_, String appActivity_ ) throws MalformedURLException, InterruptedException {
		
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(CapabilityType.BROWSER_NAME, "");
		capabilities.setCapability("deviceName", deviceName_);
		capabilities.setCapability("udid", UDID_);
		capabilities.setCapability("appPackage", appPackage_);
		capabilities.setCapability("appActivity", appActivity_);
		
		driver = new AndroidDriver<MobileElement>(new URL("http://172.16.20.16:"+Port_+"/wd/hub"), capabilities);
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		
	}
	
	@AfterMethod
	public void afterMethod() {
		 driver.closeApp();
	
	}
}