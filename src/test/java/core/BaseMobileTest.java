package core;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public abstract class BaseMobileTest {
  protected AndroidDriver driver;
  protected WebDriverWait wait;

  @BeforeClass
  public void setUp() {
    UiAutomator2Options options = new UiAutomator2Options();
    options.setPlatformName("Android");
    options.setAutomationName("UiAutomator2");
    options.setDeviceName("Android Emulator");
    options.setAppPackage("org.wikipedia");
    options.setAppActivity("org.wikipedia.main.MainActivity");
    options.setNoReset(true);
    options.setNewCommandTimeout(Duration.ofSeconds(120));

    try {
      driver = new AndroidDriver(new URL(Config.APPIUM_URL), options);
    } catch (MalformedURLException e) {
      throw new RuntimeException("Invalid APPIUM_URL", e);
    }

    wait = new WebDriverWait(driver, Duration.ofSeconds(Config.MOBILE_TIMEOUT_SECONDS));
  }

  @AfterClass(alwaysRun = true)
  public void tearDown() {
    if (driver != null) {
      driver.quit();
    }
  }

  protected void dismissOnboardingIfPresent() {
    WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));
    By[] locators = new By[] {
        By.id("org.wikipedia:id/fragment_onboarding_skip_button"),
        By.id("org.wikipedia:id/onboarding_skip_button"),
        By.id("org.wikipedia:id/fragment_onboarding_forward_button"),
        By.id("org.wikipedia:id/positiveButton")
    };

    for (By locator : locators) {
      try {
        shortWait.until(ExpectedConditions.elementToBeClickable(locator)).click();
        return;
      } catch (TimeoutException ignored) {
      }
    }
  }
}
