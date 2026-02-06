package pages.mobile;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MainPage {
  private final AndroidDriver driver;
  private final WebDriverWait wait;

  private final By searchContainer = AppiumBy.id("org.wikipedia:id/search_container");
  private final By searchContainerText = AppiumBy.id("org.wikipedia:id/search_container_text");
  private final By searchInput = AppiumBy.id("org.wikipedia:id/search_src_text");
  private final By searchAccessibility = AppiumBy.accessibilityId("Search Wikipedia");
  private final By uiSearchDesc = AppiumBy.androidUIAutomator(
      "new UiSelector().descriptionContains(\"Search\")");

  public MainPage(AndroidDriver driver, WebDriverWait wait) {
    this.driver = driver;
    this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    this.wait.pollingEvery(Duration.ofMillis(500));
  }

  public boolean isLoaded() {
    By[] locators = new By[] {
        searchContainerText,
        searchContainer,
        searchInput,
        searchAccessibility,
        uiSearchDesc
    };

    for (By locator : locators) {
      try {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
      } catch (TimeoutException ignored) {
      }
    }
    return false;
  }
}
