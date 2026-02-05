package core;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public final class DriverFactory {
  private DriverFactory() {
  }

  public static WebDriver createChromeDriver() {
    WebDriverManager.chromedriver().setup();

    ChromeOptions options = new ChromeOptions();
    if ("true".equalsIgnoreCase(System.getProperty("headless"))) {
      options.addArguments("--headless=new");
    }
    options.addArguments("--window-size=1400,900");
    options.addArguments("--no-sandbox");
    options.addArguments("--disable-dev-shm-usage");

    return new ChromeDriver(options);
  }
}
