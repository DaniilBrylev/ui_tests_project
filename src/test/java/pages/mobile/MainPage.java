package pages.mobile;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MainPage {
  private final AndroidDriver driver;
  private final WebDriverWait wait;

  private final By searchContainer = By.id("org.wikipedia:id/search_container");
  private final By searchContainerText = By.id("org.wikipedia:id/search_container_text");

  public MainPage(AndroidDriver driver, WebDriverWait wait) {
    this.driver = driver;
    this.wait = wait;
  }

  public boolean isLoaded() {
    return isVisible(searchContainer) || isVisible(searchContainerText);
  }

  private boolean isVisible(By locator) {
    try {
      return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
    } catch (TimeoutException e) {
      return false;
    }
  }
}
