package pages.mobile;

import io.appium.java_client.android.AndroidDriver;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SearchPage {
  private final AndroidDriver driver;
  private final WebDriverWait wait;

  private final By searchContainer = By.id("org.wikipedia:id/search_container");
  private final By searchContainerText = By.id("org.wikipedia:id/search_container_text");
  private final By searchInput = By.id("org.wikipedia:id/search_src_text");
  private final By clearBtn = By.id("org.wikipedia:id/search_close_btn");
  private final By resultsList = By.id("org.wikipedia:id/search_results_list");
  private final By resultTitle = By.id("org.wikipedia:id/page_list_item_title");

  public SearchPage(AndroidDriver driver, WebDriverWait wait) {
    this.driver = driver;
    this.wait = wait;
  }

  public void openSearch() {
    try {
      wait.until(ExpectedConditions.elementToBeClickable(searchContainer)).click();
    } catch (TimeoutException e) {
      wait.until(ExpectedConditions.elementToBeClickable(searchContainerText)).click();
    }
  }

  public void typeQuery(String text) {
    WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput));
    try {
      WebElement clear = wait.until(ExpectedConditions.elementToBeClickable(clearBtn));
      clear.click();
    } catch (TimeoutException ignored) {
    }
    input.sendKeys(text);
  }

  public boolean hasResults() {
    try {
      return wait.until(ExpectedConditions.visibilityOfElementLocated(resultsList)).isDisplayed();
    } catch (TimeoutException e) {
      try {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(resultTitle)).isDisplayed();
      } catch (TimeoutException ignored) {
        return false;
      }
    }
  }

  public void openFirstResult() {
    wait.until(ExpectedConditions.visibilityOfElementLocated(resultTitle));
    List<WebElement> results = driver.findElements(resultTitle);
    if (results.isEmpty()) {
      throw new IllegalStateException("No search results to open");
    }
    results.get(0).click();
  }
}
