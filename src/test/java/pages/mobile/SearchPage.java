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

  private final By searchContainer = AppiumBy.id("org.wikipedia:id/search_container");
  private final By searchContainerText = AppiumBy.id("org.wikipedia:id/search_container_text");
  private final By searchMenu = AppiumBy.id("org.wikipedia:id/menu_search");
  private final By searchInput = AppiumBy.id("org.wikipedia:id/search_src_text");
  private final By clearBtn = AppiumBy.id("org.wikipedia:id/search_close_btn");
  private final By resultsList = AppiumBy.id("org.wikipedia:id/search_results_list");
  private final By resultTitle = AppiumBy.id("org.wikipedia:id/page_list_item_title");

  private final By searchAccessibility = AppiumBy.accessibilityId("Search Wikipedia");
  private final By uiSearchDesc = AppiumBy.androidUIAutomator(
      "new UiSelector().descriptionContains(\"Search\")");
  private final By uiSearchText = AppiumBy.androidUIAutomator(
      "new UiSelector().textContains(\"Search\")");
  private final By recyclerView = AppiumBy.className("androidx.recyclerview.widget.RecyclerView");
  private final By editText = AppiumBy.className("android.widget.EditText");
  private final By textView = AppiumBy.className("android.widget.TextView");

  public SearchPage(AndroidDriver driver, WebDriverWait wait) {
    this.driver = driver;
    this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    this.wait.pollingEvery(Duration.ofMillis(500));
  }

  public void openSearch() {
    dismissOnboardingIfPresent();

    By[] locators = new By[] {
        searchAccessibility,
        searchMenu,
        searchContainerText,
        searchContainer,
        uiSearchDesc,
        uiSearchText
    };

    WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(2));
    shortWait.pollingEvery(Duration.ofMillis(250));

    for (By locator : locators) {
      try {
        shortWait.until(ExpectedConditions.elementToBeClickable(locator)).click();
        if (waitForSearchInput(Duration.ofSeconds(2)) != null) {
          return;
        }
      } catch (TimeoutException ignored) {
      }
    }

    for (By locator : locators) {
      try {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
        waitForSearchInput();
        return;
      } catch (TimeoutException ignored) {
      }
    }

    logSearchDiagnostics();
    throw new TimeoutException("Search container not found");
  }

  public void typeQuery(String text) {
    WebElement input = waitForSearchInput();
    if (isClickable(clearBtn, Duration.ofSeconds(2))) {
      try {
        driver.findElement(clearBtn).click();
      } catch (Exception ignored) {
      }
    }
    try {
      input.clear();
    } catch (Exception ignored) {
    }
    input.sendKeys(text);
  }

  public boolean waitResultsVisible() {
    try {
      wait.until(driver -> hasAnyResultItem());
      return true;
    } catch (TimeoutException e) {
      return false;
    }
  }

  public void openFirstResult() {
    if (!waitResultsVisible()) {
      throw new TimeoutException("Search results not visible");
    }

    WebElement list = waitResultsContainer();

    List<WebElement> titles = list.findElements(resultTitle);
    for (WebElement title : titles) {
      if (hasText(title)) {
        title.click();
        return;
      }
    }

    List<WebElement> textViews = list.findElements(textView);
    for (WebElement tv : textViews) {
      if (hasText(tv)) {
        tv.click();
        return;
      }
    }

    List<WebElement> clickables = list.findElements(By.xpath(".//*[@clickable='true']"));
    for (WebElement clickable : clickables) {
      List<WebElement> childTexts = clickable.findElements(textView);
      for (WebElement tv : childTexts) {
        if (hasText(tv)) {
          clickable.click();
          return;
        }
      }
    }

    throw new IllegalStateException("No search results to open");
  }

  private boolean hasAnyResultItem() {
    WebElement list = findResultsContainer();
    if (list == null) {
      return false;
    }

    List<WebElement> titles = list.findElements(resultTitle);
    for (WebElement title : titles) {
      if (hasText(title)) {
        return true;
      }
    }

    List<WebElement> textViews = list.findElements(textView);
    for (WebElement tv : textViews) {
      if (hasText(tv)) {
        return true;
      }
    }

    return false;
  }

  private WebElement waitResultsContainer() {
    try {
      return wait.until(ExpectedConditions.visibilityOfElementLocated(resultsList));
    } catch (TimeoutException e) {
      return wait.until(ExpectedConditions.visibilityOfElementLocated(recyclerView));
    }
  }

  private WebElement findResultsContainer() {
    List<WebElement> list = driver.findElements(resultsList);
    if (!list.isEmpty() && list.get(0).isDisplayed()) {
      return list.get(0);
    }
    List<WebElement> rv = driver.findElements(recyclerView);
    if (!rv.isEmpty() && rv.get(0).isDisplayed()) {
      return rv.get(0);
    }
    return null;
  }

  private WebElement waitForSearchInput() {
    WebElement input = waitForSearchInput(Duration.ofSeconds(30));
    if (input == null) {
      throw new TimeoutException("Search input not found");
    }
    return input;
  }

  private WebElement waitForSearchInput(Duration timeout) {
    By[] inputs = new By[] { searchInput, editText };
    WebDriverWait inputWait = new WebDriverWait(driver, timeout);
    inputWait.pollingEvery(Duration.ofMillis(250));

    for (By locator : inputs) {
      try {
        return inputWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
      } catch (TimeoutException ignored) {
      }
    }
    return null;
  }

  private boolean isClickable(By locator, Duration timeout) {
    try {
      new WebDriverWait(driver, timeout)
          .until(ExpectedConditions.elementToBeClickable(locator));
      return true;
    } catch (TimeoutException e) {
      return false;
    }
  }

  private boolean hasText(WebElement el) {
    if (el == null) {
      return false;
    }
    String text = el.getText();
    return text != null && !text.trim().isEmpty();
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
        By.xpath("//*[@text='Continue' or @text='CONTINUE' or @text='Skip' or @text='SKIP'"
            + " or @text='Accept' or @text='AGREE' or @text='No thanks' or @text='NO THANKS'"
            + " or @text='Allow' or contains(@text,'While using')]")
    };

    WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(2));
    shortWait.pollingEvery(Duration.ofMillis(250));

    for (int attempt = 0; attempt < 4; attempt++) {
      boolean clicked = false;
      for (By locator : buttons) {
        try {
          shortWait.until(ExpectedConditions.elementToBeClickable(locator)).click();
          clicked = true;
          sleepMillis(300);
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
        searchAccessibility,
        uiSearchDesc,
        uiSearchText
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
