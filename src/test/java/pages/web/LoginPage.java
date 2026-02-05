package pages.web;

import core.Config;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {
  private final WebDriver driver;
  private final WebDriverWait wait;

  private final By header = By.tagName("h2");
  private final By usernameField = By.id("username");
  private final By passwordField = By.id("password");
  private final By submitButton = By.cssSelector("button[type='submit']");
  private final By flash = By.id("flash");

  public LoginPage(WebDriver driver, WebDriverWait wait) {
    this.driver = driver;
    this.wait = wait;
  }

  public void open() {
    driver.get(Config.WEB_BASE_URL + "/login");
  }

  public boolean isLoaded() {
    try {
      WebElement h2 = wait.until(ExpectedConditions.visibilityOfElementLocated(header));
      return h2.getText().contains("Login Page");
    } catch (TimeoutException e) {
      return false;
    }
  }

  public void login(String username, String password) {
    WebElement userInput = wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField));
    WebElement passInput = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField));

    userInput.clear();
    passInput.clear();

    userInput.sendKeys(username);
    passInput.sendKeys(password);

    wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();
  }

  public String getFlashMessage() {
    WebElement flashEl = wait.until(ExpectedConditions.visibilityOfElementLocated(flash));
    return flashEl.getText().replace("\n", " ").trim();
  }
}
