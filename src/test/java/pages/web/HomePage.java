package pages.web;

import core.Config;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {
  private final WebDriver driver;
  private final WebDriverWait wait;
  private final By header = By.tagName("h1");

  public HomePage(WebDriver driver, WebDriverWait wait) {
    this.driver = driver;
    this.wait = wait;
  }

  public void open() {
    driver.get(Config.WEB_BASE_URL + "/");
  }

  public boolean isLoaded() {
    try {
      // 1) Проверяем title
      wait.until(driver -> driver.getTitle() != null && driver.getTitle().contains("The Internet"));

      // 2) Проверяем H1 (на главной он "Welcome to the-internet")
      By h1 = By.tagName("h1");
      WebElement h1El = wait.until(ExpectedConditions.visibilityOfElementLocated(h1));
      String h1Text = h1El.getText();
      return h1Text != null && h1Text.toLowerCase().contains("welcome to the-internet");
    } catch (TimeoutException e) {
      return false;
    }
  }

  public void clickLink(String linkText) {
    By link = By.linkText(linkText);
    wait.until(ExpectedConditions.elementToBeClickable(link)).click();
  }
}
