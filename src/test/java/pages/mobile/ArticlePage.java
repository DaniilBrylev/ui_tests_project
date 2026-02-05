package pages.mobile;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ArticlePage {
  private final AndroidDriver driver;
  private final WebDriverWait wait;

  private final By titlePrimary = By.id("org.wikipedia:id/view_page_title_text");
  private final By titleAlt = By.id("org.wikipedia:id/page_title");

  public ArticlePage(AndroidDriver driver, WebDriverWait wait) {
    this.driver = driver;
    this.wait = wait;
  }

  public String getTitle() {
    try {
      return wait.until(ExpectedConditions.visibilityOfElementLocated(titlePrimary)).getText();
    } catch (TimeoutException e) {
      return wait.until(ExpectedConditions.visibilityOfElementLocated(titleAlt)).getText();
    }
  }

  public boolean titleContains(String expected) {
    return getTitle().toLowerCase().contains(expected.toLowerCase());
  }
}
