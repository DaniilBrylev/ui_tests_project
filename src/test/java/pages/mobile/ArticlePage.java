package pages.mobile;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ArticlePage {
  private final AndroidDriver driver;
  private final WebDriverWait wait;

  private final By titlePrimary = AppiumBy.id("org.wikipedia:id/view_page_title_text");
  private final By titleAlt = AppiumBy.id("org.wikipedia:id/page_title");
  private final By titleUi = AppiumBy.androidUIAutomator(
      "new UiSelector().resourceIdMatches(\".*page_title.*\")");

  public ArticlePage(AndroidDriver driver, WebDriverWait wait) {
    this.driver = driver;
    this.wait = wait;
  }

  public String getTitle() {
    WebElement titleEl = findTitleElement();
    return titleEl == null ? "" : titleEl.getText();
  }

  public boolean titleContains(String expected) {
    if (expected == null || expected.isEmpty()) {
      return false;
    }

    for (By locator : titleLocators()) {
      try {
        return wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, expected));
      } catch (TimeoutException ignored) {
      }
    }

    String title = getTitle();
    return title.toLowerCase().contains(expected.toLowerCase());
  }

  private WebElement findTitleElement() {
    for (By locator : titleLocators()) {
      try {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
      } catch (TimeoutException ignored) {
      }
    }
    return null;
  }

  private By[] titleLocators() {
    return new By[] { titlePrimary, titleAlt, titleUi };
  }
}
