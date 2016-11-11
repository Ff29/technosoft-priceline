package com.saucelabs.priceline_git.test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.google.common.base.Function;

public class WebDriverFactory {

	private static WebDriver driver = null;
	protected static final String  URL = "https://www.priceline.com/";
	public static final String USERNAME = "username";
	public static final String ACCESS_KEY = "access-key";
    public static final String SLURL = "https://" + USERNAME + ":" + ACCESS_KEY + "@ondemand.saucelabs.com:443/wd/hub";
    
    public static WebDriver getDriver() {
		return driver;
	}

	public void setDriver(int driverNumber) {
		switch(driverNumber){
		case 1:
			setSauceLabs();
			break;
		case 2:
			setChromeLocal();
			break;
		case 3:
			setFireFoxLocal();
			break;
		default:
			setChromeLocal();
			break;
		}
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.get(URL);
	}
	private void setSauceLabs(){
		 try {
			DesiredCapabilities caps = DesiredCapabilities.chrome();
			    caps.setCapability("platform", "Windows XP");
			    caps.setCapability("version", "43.0");
			    driver = new RemoteWebDriver(new URL(SLURL), caps);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	private void setChromeLocal(){
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\Legion\\Documents\\SeleniumLessons\\Tutorials\\Selenium\\jars\\chromedriver.exe");
		driver = new ChromeDriver();
	}
	
	private void setFireFoxLocal(){
		System.setProperty("webdriver.gecko.driver", "C:\\Users\\Legion\\Documents\\SeleniumLessons\\Tutorials\\Selenium\\jars\\geckodriver.exe");
		driver = new FirefoxDriver();
	}
	
	public void goToHomePage(){
		driver.get(URL);
		driver.manage().window().maximize();
	}
	@BeforeClass
	public void intializeWebDriver() {
		setSauceLabs();
	}
	
	@AfterClass
	public void tearDown() {
		if(driver != null) {
			driver.manage().deleteAllCookies();
			driver.quit();
		}
	}
	
	public static void WaitImplicit(int milliseconds){
		driver.manage().timeouts().implicitlyWait(milliseconds, TimeUnit.MILLISECONDS);
	}
	
	public static void WaitUntilVisible(By locater){
		WebDriverWait wait = new WebDriverWait(driver, 10);
		WebElement element = wait.until(
		        ExpectedConditions.visibilityOfElementLocated(locater));
	}
	
	public static WebElement webDriverFluentWait(final By locator) {
		Wait<WebDriver> wait = new FluentWait<WebDriver>(getDriver())
				.withTimeout(15, TimeUnit.SECONDS)
				.pollingEvery(500, TimeUnit.MILLISECONDS)
				.ignoring(NoSuchElementException.class)
				.ignoring(StaleElementReferenceException.class)
				.ignoring(ElementNotFoundException.class)
				.withMessage(
						"Webdriver waited for 15 seconds but still could not find the element therefore Timeout Exception has been thrown");
				
		WebElement element = wait.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(locator);
			}
		});

		return element;
	}
}
