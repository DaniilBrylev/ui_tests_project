package pages.web;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SecureAreaPage {
  private final WebDriver driver;
  private final WebDriverWait wait;

  private final By flash = By.id("flash");

  public SecureAreaPage(WebDriver driver, WebDriverWait wait) {
    this.driver = driver;
    this.wait = wait;
  }

  public boolean isLoaded() {
    try {
      WebElement flashEl = wait.until(ExpectedConditions.visibilityOfElementLocated(flash));
      return driver.getCurrentUrl().contains("/secure") && flashEl.isDisplayed();
    } catch (TimeoutException e) {
      return false;
    }
  }

  public String getFlashMessage() {
    WebElement flashEl = wait.until(ExpectedConditions.visibilityOfElementLocated(flash));
    return flashEl.getText().replace("\n", " ").trim();
  }
}
