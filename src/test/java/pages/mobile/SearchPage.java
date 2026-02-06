package pages.mobile;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import java.time.Duration;
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
    this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    this.wait.pollingEvery(Duration.ofMillis(500));
  }

  public void openSearch() {
    dismissOnboardingIfPresent();

    WebDriverWait quickWait = new WebDriverWait(driver, Duration.ofSeconds(2));
    quickWait.pollingEvery(Duration.ofMillis(250));
    By[] quickLocators = new By[] { searchContainerText, searchContainer };
    for (By locator : quickLocators) {
      try {
        quickWait.until(ExpectedConditions.visibilityOfElementLocated(locator)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput));
        return;
      } catch (TimeoutException ignored) {
      }
    }

    By[] locators = new By[] {
        searchContainerText,
        searchContainer,
        AppiumBy.accessibilityId("Search Wikipedia"),
        By.xpath("//*[contains(@text,'Search') or contains(@text,'Поиск') or contains(@content-desc,'Search')]")
    };

    for (By locator : locators) {
      try {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput));
        return;
      } catch (TimeoutException ignored) {
      }
    }

    logSearchDiagnostics();
    throw new TimeoutException("Search container not found");
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

  private void dismissOnboardingIfPresent() {
    By[] buttons = new By[] {
        By.id("org.wikipedia:id/fragment_onboarding_skip_button"),
        By.id("org.wikipedia:id/onboarding_skip_button"),
        By.id("org.wikipedia:id/accept_button"),
        By.id("org.wikipedia:id/positiveButton"),
        By.id("android:id/button1"),
        By.id("com.android.permissioncontroller:id/permission_allow_button"),
        By.id("com.android.permissioncontroller:id/permission_allow_foreground_only_button"),
        By.id("com.android.permissioncontroller:id/permission_deny_button"),
        By.id("com.android.permissioncontroller:id/permission_deny_and_dont_ask_again_button"),
        By.xpath("//*[@text='Continue' or @text='CONTINUE'"
            + " or @text='Skip' or @text='SKIP'"
            + " or @text='Accept' or @text='AGREE'"
            + " or @text='No thanks' or @text='NO THANKS'"),
        By.xpath("//*[@text='Allow' or @text='Разрешить'"
            + " or contains(@text,'While using') or contains(@text,'во время использования')]")
    };

    WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(2));
    shortWait.pollingEvery(Duration.ofMillis(250));

    for (int attempt = 0; attempt < 4; attempt++) {
      boolean clicked = false;
      for (By locator : buttons) {
        try {
          shortWait.until(ExpectedConditions.elementToBeClickable(locator)).click();
          clicked = true;
          sleepMillis(400);
          if (isSearchContainerVisible()) {
            return;
          }
          break;
        } catch (Exception ignored) {
        }
      }
      if (!clicked) {
        return;
      }
    }
  }

  private boolean isSearchContainerVisible() {
    By[] locators = new By[] {
        searchContainerText,
        searchContainer,
        AppiumBy.accessibilityId("Search Wikipedia"),
        By.xpath("//*[@text='Search' or @text='Поиск' or @content-desc='Search']")
    };

    for (By locator : locators) {
      try {
        if (!driver.findElements(locator).isEmpty()) {
          return true;
        }
      } catch (Exception ignored) {
      }
    }
    return false;
  }

  private void logSearchDiagnostics() {
    try {
      System.out.println("[MOBILE] currentPackage=" + driver.getCurrentPackage());
    } catch (Exception e) {
      System.out.println("[MOBILE] currentPackage=<error>");
    }
    try {
      System.out.println("[MOBILE] currentActivity=" + driver.currentActivity());
    } catch (Exception e) {
      System.out.println("[MOBILE] currentActivity=<error>");
    }
    try {
      String source = driver.getPageSource();
      System.out.println("[MOBILE] pageSource=" + truncate(source, 2000));
    } catch (Exception e) {
      System.out.println("[MOBILE] pageSource=<error>");
    }
  }

  private String truncate(String value, int max) {
    if (value == null) {
      return "";
    }
    if (value.length() <= max) {
      return value;
    }
    return value.substring(0, max);
  }

  private void sleepMillis(long millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }
}
