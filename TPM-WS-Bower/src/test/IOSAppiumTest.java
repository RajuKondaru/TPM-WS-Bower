package test;
import io.appium.java_client.ios.IOSDriver;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class IOSAppiumTest {
 
	 
	 @SuppressWarnings("rawtypes")
	 private IOSDriver driver;
	 
	 @SuppressWarnings("rawtypes")
	 @BeforeMethod
	 public void setUp() throws MalformedURLException {
		 DesiredCapabilities caps = new DesiredCapabilities();
		 caps.setCapability("platformName", "iOS");
		 caps.setCapability("platformVersion", "7.1"); 
		 caps.setCapability("deviceName", "iPhone Simulator"); 
		// caps.setCapability("bundleid", "com.example.apple-samplecode.UICatalog");
		 caps.setCapability("app", "/Users/gopikannan/Downloads/ios-uicatalog-master/build/Release-iphonesimulator/UICatalog.app"); 
		 driver = new IOSDriver(new URL("http://127.0.0.1:4725/wd/hub"), caps);
	 }
	 
	 @Test
	 public void testiOS() throws InterruptedException, IOException {
		 driver.findElement(By.xpath("//UIAApplicationIAWindow[1]/UIATableView[1]/UIATableCell[1]/UIAStaticText[1]")).click();
		 driver.findElement(By.xpath("//UIAApplicationIAWindow[1]/UIATableView[1]/UIATableCell[1]/UIAStaticText[1]")).click();
		 driver.findElement(By.name("OK")).click();
		 Thread.sleep(2000);
		 File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		 FileUtils.copyFile(scrFile, new File("/Users/gopikannan/Downloads/g2.jpg"));
	 
	 }
	 
	 @AfterMethod
	 public void tearDown() {
		 driver.quit();
	 }
 
}