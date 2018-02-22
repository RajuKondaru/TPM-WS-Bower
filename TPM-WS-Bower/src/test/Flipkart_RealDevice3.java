package test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

import org.junit.Rule;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testobject.appium.junit.TestObjectAppiumSuite;
import org.testobject.appium.junit.TestObjectAppiumSuiteWatcher;
import org.testobject.rest.api.appium.common.TestObject;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
@TestObject(testLocally = false, testObjectApiKey = "CF106954A26A4ACCA1C6BB6B5D71BB7D", testObjectSuiteId = 7)
@RunWith(TestObjectAppiumSuite.class)
public class Flipkart_RealDevice3 {
	
	private AppiumDriver<WebElement> driver;

    @Rule
    public TestName testName = new TestName();

    @Rule
    public TestObjectAppiumSuiteWatcher resultWatcher = new TestObjectAppiumSuiteWatcher();

	
	
	@BeforeTest
	public void setUp() throws MalformedURLException
	{
		DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("testobject_api_key", resultWatcher.getApiKey());
        capabilities.setCapability("testobject_test_report_id", resultWatcher.getTestReportId());

        driver = new AndroidDriver<>(resultWatcher.getTestObjectOrLocalAppiumEndpointURL(), capabilities);

        resultWatcher.setRemoteWebDriver(driver);

	   
    }
	
	 @Test
	 public void Login() throws InterruptedException, IOException 
	 {	
		 driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		 Thread.sleep(1000);
	 }
		 

	
	@AfterTest
	public void tearDown()
	{
		driver.quit();  
	}
}
