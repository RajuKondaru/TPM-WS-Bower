package test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;

public class amazon_dataprovider 
{
	AndroidDriver driver;
	@BeforeTest()
	public void launchactivity() throws MalformedURLException
	{

		//Test pace capabilities
		DesiredCapabilities capabilities = new DesiredCapabilities();

		capabilities.setCapability("api_key","6c6667b6-767e-40fd-8459-ce628de06f91");

		capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION,"5.1.1");

		capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME,"Android");

		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME,"5203c45ae8fea300 ");


		driver = new AndroidDriver(new URL("http://172.16.20.115:4723/wd/hub"),capabilities);
	    driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
	}
@Test()
public void test()
{
	
	driver.findElement(By.id("com.amazon.mShop.android.shopping:id/skip_sign_in_button")).click();
	driver.findElement(By.id("com.amazon.mShop.android.shopping:id/web_home_shop_by_department_label")).click();
	
	System.out.println("Test is passed");
}
@AfterTest()
public void aftertest()
{
	driver.quit();
}
}
