package tests.mobile;

import core.BaseMobileTest;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SmokeMobileTest extends BaseMobileTest {

  @Test
  public void mainPageLoads() {
    dismissOnboardingIfPresent();

    WebDriverWait localWait = new WebDriverWait(driver, Duration.ofSeconds(30));
    localWait.pollingEvery(Duration.ofMillis(500));

    By[] locators = new By[] {
        By.id("org.wikipedia:id/search_container_text"),
        By.id("org.wikipedia:id/search_container"),
        By.id("org.wikipedia:id/search_src_text"),
        By.xpath("//*[contains(@content-desc,'Search')]")
    };

    boolean loaded = false;
    for (By locator : locators) {
      try {
        loaded = localWait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
        if (loaded) {
          break;
        }
      } catch (TimeoutException ignored) {
      }
    }

    if (!loaded) {
      try {
        System.out.println("[MOBILE] currentPackage=" + driver.getCurrentPackage());
        System.out.println("[MOBILE] currentActivity=" + driver.currentActivity());
      } catch (Exception ignored) {
      }
    }

    Assert.assertTrue(loaded, "Main page should be loaded");
  }
}
