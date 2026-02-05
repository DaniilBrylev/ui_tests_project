package pages.web;

import core.Config;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DropdownPage {
  private final WebDriver driver;
  private final WebDriverWait wait;

  private final By header = By.tagName("h3");
  private final By dropdown = By.id("dropdown");

  public DropdownPage(WebDriver driver, WebDriverWait wait) {
    this.driver = driver;
    this.wait = wait;
  }

  public void open() {
    driver.get(Config.WEB_BASE_URL + "/dropdown");
  }

  public boolean isLoaded() {
    try {
      WebElement h3 = wait.until(ExpectedConditions.visibilityOfElementLocated(header));
      return "Dropdown List".equals(h3.getText());
    } catch (TimeoutException e) {
      return false;
    }
  }

  public void selectOptionByText(String text) {
    WebElement selectEl = wait.until(ExpectedConditions.elementToBeClickable(dropdown));
    Select select = new Select(selectEl);
    select.selectByVisibleText(text);
  }

  public String getSelectedOptionText() {
    WebElement selectEl = wait.until(ExpectedConditions.visibilityOfElementLocated(dropdown));
    Select select = new Select(selectEl);
    return select.getFirstSelectedOption().getText();
  }
}
