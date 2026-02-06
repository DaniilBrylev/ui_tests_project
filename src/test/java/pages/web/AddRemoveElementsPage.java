package pages.web;

import core.Config;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AddRemoveElementsPage {
  private final WebDriver driver;
  private final WebDriverWait wait;

  private final By header = By.tagName("h3");
  private final By addButton = By.cssSelector("button[onclick='addElement()']");
  private final By deleteButton = By.cssSelector("button.added-manually");

  public AddRemoveElementsPage(WebDriver driver, WebDriverWait wait) {
    this.driver = driver;
    this.wait = wait;
  }

  public void open() {
    driver.get(Config.WEB_BASE_URL + "/add_remove_elements/");
  }

  public boolean isLoaded() {
    try {
      WebElement h3 = wait.until(ExpectedConditions.visibilityOfElementLocated(header));
      return h3.getText() != null && h3.getText().contains("Add/Remove Elements");
    } catch (TimeoutException e) {
      return false;
    }
  }

  public void clickAddElement() {
    wait.until(ExpectedConditions.elementToBeClickable(addButton)).click();
  }

  public boolean isDeleteButtonVisible() {
    try {
      return wait.until(ExpectedConditions.visibilityOfElementLocated(deleteButton)).isDisplayed();
    } catch (TimeoutException e) {
      return false;
    }
  }

  public void clickDeleteButton() {
    wait.until(ExpectedConditions.elementToBeClickable(deleteButton)).click();
  }

  public boolean isDeleteButtonAbsent() {
    try {
      return wait.until(ExpectedConditions.invisibilityOfElementLocated(deleteButton));
    } catch (TimeoutException e) {
      return false;
    }
  }
}
