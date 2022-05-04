package com.applitools;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.FileLogger;
import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.StdoutLogHandler;
import com.applitools.eyes.selenium.BrowserType;
import com.applitools.eyes.selenium.ClassicRunner;
import com.applitools.eyes.selenium.Configuration;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.fluent.Target;
import com.applitools.eyes.visualgrid.model.DeviceName;
import com.applitools.eyes.visualgrid.services.VisualGridRunner;
import com.codeborne.selenide.WebDriverRunner;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WrapsDriver;

public class EyesWatcher
    extends TestWatcher
{
  public static Eyes eyes;

  private String testName;
  
  private VisualGridRunner runner;

  private static BatchInfo batch;

  private static final String APPLITOOLS_KEY = System.getProperty("APPLITOOLS_API_KEY", System.getenv("APPLITOOLS_API_KEY"));

  private static final String APPLICATION_NAME = System.getProperty("applicationName", "Branch Test");
  
  private static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
  
  private static String localBranchName;

  static {
    localBranchName = System.getProperty("branchName", System.getenv("GIT_BRANCH_NAME"));
    if (localBranchName == null) {
      localBranchName = "default";
    }
    //eyes.setIsDisabled(APPLITOOLS_KEY == null);

    if (APPLITOOLS_KEY != null) {
      String buildNumber = System.getenv("BUILD_NUMBER");
      batch = new BatchInfo(
          (buildNumber != null ? "#" + buildNumber : " " + localBranchName));
      //BatchInfo batchInfo = new BatchInfo(System.getenv("APPLITOOLS_BATCH_ID"));
      // If the test runs via TeamCity, set the batch ID accordingly.
      String batchId = System.getenv("APPLITOOLS_BATCH_ID");
      if (batchId != null) {
        batch.setId(batchId);
      }
      //eyes.setBatch(batch);

      // Aggregates tests under the same batch when tests are run in different processes (e.g. split tests in bamboo).
      //if (buildNumber != null) {
      //  batch.setId(batch.getName());
      //}

      //eyes.setApiKey(APPLITOOLS_KEY);
      //eyes.setBatch(batch);

      //eyes.setBranchName(localBranchName);

      // For local testing or ci runs with master set the branchName and parentBranchNam
      //if ((batchId != null && "master".equalsIgnoreCase(localBranchName)) || batchId == null) {
        //eyes.setBranchName(
        //    localBranchName.equalsIgnoreCase("master") ? "bmurmistro/applitools-junit/master" : localBranchName);
        //eyes.setParentBranchName("default");
      //}
      //eyes.setIgnoreCaret(true);
    }
    //eyes.setLogHandler(new FileLogger("/Users/brandonmurray/dev/applitools/bmurmistro/applitools.log", false, true));
  }

  @Override
  protected void starting(Description description) {
    testName = description.getTestClass().getSimpleName() + "." + description.getMethodName();
  }

  @Override
  protected void finished(Description description) {
    if (eyes != null) {
      try {
        // End visual testing. Validate visual correctness.
        if (eyes.getIsOpen()) {
          eyes.closeAsync();
          runner.getAllTestResults(true);
        }
      }
      finally {
        // Abort test in case of an unexpected error.
        eyes.abortAsync();
        eyes = null;
        runner = null;
      }
    }
    testName = null;
  }

  public void eyesCheck(ICheckSettings settings) {
    eyesCheck(null, settings);
  }

  /**
   * Convenience method for performing the Applitools validation.
   *
   * @param tag or step name of the validation
   */
  public void eyesCheck(String tag, ICheckSettings settings) {
      if (eyes == null) {
        initEyes();
        WebDriver remoteDriver = WebDriverRunner.getAndCheckWebDriver();

        if (remoteDriver instanceof WrapsDriver) {
          remoteDriver = ((WrapsDriver) remoteDriver).getWrappedDriver();
        }

        eyes.open(remoteDriver, APPLICATION_NAME, testName, new RectangleSize(800, 600));
      }
      eyes.check(tag, settings);
  }
  
  private Configuration getConfiguation() {
    Configuration sconf = new Configuration();


    // Set a batch name so all the different browser and mobile combinations are
    // part of the same batch
    sconf.setBatch(batch);

    // Add Chrome browsers with different Viewports
    sconf.addBrowser(800, 600, BrowserType.CHROME);

    // Add Firefox browser with different Viewports
    sconf.addBrowser(800, 600, BrowserType.FIREFOX);
    
    return sconf;
  }
  
  private void initEyes() {
    runner = new VisualGridRunner(10);

    // Initialize the eyes SDK
    eyes = new Eyes(runner);
    eyes.setLogHandler(new StdoutLogHandler(true));
    eyes.setConfiguration(getConfiguation());
    eyes.setApiKey(APPLITOOLS_KEY);
    eyes.setBatch(batch);

    //eyes.setBranchName(localBranchName);

    // For local testing or ci runs with master set the branchName and parentBranchNam
    /*if ((batch.getId() != null && "master".equalsIgnoreCase(localBranchName)) || batch.getId() == null) {
      eyes.setBranchName(
          localBranchName.equalsIgnoreCase("master") ? "bmurmistro/applitools-junit/master" : localBranchName);
      eyes.setParentBranchName("default");
    }*/
    eyes.setIgnoreCaret(true);
  }
}
