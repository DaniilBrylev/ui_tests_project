package core;

import java.time.Duration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.WebDriver.Timeouts;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public abstract class BaseWebTest {
  protected WebDriver driver;
  protected WebDriverWait wait;

  @BeforeClass
  public void setUp() {
    driver = DriverFactory.createChromeDriver();
    Options options = driver.manage();
    Timeouts timeouts = options.timeouts();
    timeouts.implicitlyWait(Duration.ofSeconds(0));
    options.window().setSize(new Dimension(1400, 900));

    wait = new WebDriverWait(driver, Duration.ofSeconds(Config.WEB_TIMEOUT_SECONDS));
  }

  @AfterClass(alwaysRun = true)
  public void tearDown() {
    if (driver != null) {
      driver.quit();
    }
  }

  protected void open(String path) {
    driver.get(Config.WEB_BASE_URL + path);
  }
}
