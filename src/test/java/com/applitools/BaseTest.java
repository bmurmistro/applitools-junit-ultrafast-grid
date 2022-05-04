package com.applitools;

import com.applitools.eyes.BatchInfo;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.openqa.selenium.WebDriver;


public class BaseTest
{
  protected static WebDriver driver;

  @Rule
  public EyesWatcher eyesWatcher = new EyesWatcher();

  @BeforeClass
  public static void setUp() {
    //ChromeOptions options = new ChromeOptions();
    //options.addArguments("--headless");
    Configuration.browser = "chrome";
    //Configuration.headless = true;
    driver = WebDriverRunner.getAndCheckWebDriver();
  }

  @AfterClass
  public static void tearDown() {
    driver.quit();
  }

}
