package core;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public abstract class BaseMobileTest {
  private static final int COMMAND_TIMEOUT_SEC = 15;
  private static final int BOOT_TIMEOUT_SEC = 120;

  private static final String WIKI_PKG = "org.wikipedia";
  private static final String WIKI_ACTIVITY = "org.wikipedia.DefaultIcon";

  protected AndroidDriver driver;
  protected WebDriverWait wait;

  @BeforeClass
  public void setUp() {
    ensureAppiumIsAvailable();
    waitForEmulatorBoot();

    UiAutomator2Options options = new UiAutomator2Options();
    options.setPlatformName("Android");
    options.setAutomationName("UiAutomator2");
    options.setDeviceName("Android Emulator");

    options.setAppPackage(WIKI_PKG);
    options.setAppActivity(WIKI_ACTIVITY);
    options.setAppWaitActivity("org.wikipedia.*");

    options.setNoReset(true);
    options.setNewCommandTimeout(Duration.ofSeconds(120));
    options.setAdbExecTimeout(Duration.ofSeconds(120));
    options.setUiautomator2ServerInstallTimeout(Duration.ofSeconds(120));
    options.setUiautomator2ServerLaunchTimeout(Duration.ofSeconds(120));
    options.setAppWaitDuration(Duration.ofSeconds(120));
    options.setDisableWindowAnimation(true);
    options.setIgnoreHiddenApiPolicyError(true);

    try {
      driver = new AndroidDriver(new URL(Config.APPIUM_URL), options);
    } catch (MalformedURLException e) {
      throw new RuntimeException("Invalid APPIUM_URL", e);
    }

    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
    wait = new WebDriverWait(driver, Duration.ofSeconds(Config.MOBILE_TIMEOUT_SECONDS));

   
    ensureWikipediaInForegroundOrSkip();
  }

  @AfterClass(alwaysRun = true)
  public void tearDown() {
    if (driver != null) {
      driver.quit();
    }
  }

  
  protected void ensureWikipediaInForeground() {
    ensureWikipediaInForegroundOrSkip();
  }

  protected void dismissOnboardingIfPresent() {
    ensureWikipediaInForegroundOrSkip();

    By[] locators = new By[] {
        By.id("org.wikipedia:id/fragment_onboarding_skip_button"),
        By.id("org.wikipedia:id/onboarding_skip_button"),
        By.id("org.wikipedia:id/fragment_onboarding_forward_button"),
        By.id("org.wikipedia:id/accept_button"),
        By.id("org.wikipedia:id/positiveButton"),
        By.id("android:id/button1"),
        By.id("com.android.permissioncontroller:id/permission_allow_button"),
        By.id("com.android.permissioncontroller:id/permission_allow_foreground_only_button"),
        By.xpath("//*[@text='Continue' or @text='CONTINUE' or @text='Skip' or @text='SKIP'"
            + " or @text='Accept' or @text='AGREE' or @text='No thanks' or @text='NO THANKS'"
            + " or @text='Allow' or contains(@text,'While using')]")
    };

    for (int attempt = 0; attempt < 4; attempt++) {
      boolean clicked = false;
      for (By locator : locators) {
        if (exists(locator, Duration.ofSeconds(2))) {
          try {
            safeClick(locator);
            clicked = true;
            sleepMillis(300);
            break;
          } catch (Exception ignored) {
          }
        }
      }
      if (!clicked) {
        return;
      }
    }
  }

  private void ensureWikipediaInForegroundOrSkip() {
    String adbPath = resolveAdbPath();
    String udid = resolveUdid();

    long deadline = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(20);
    Exception lastError = null;

    while (System.currentTimeMillis() < deadline) {
      if (isWikipediaForeground()) {
        return;
      }

      try {
        driver.activateApp(WIKI_PKG);
      } catch (Exception e) {
        lastError = e;
      }

      if (isWikipediaForeground()) {
        return;
      }

      try {
        driver.terminateApp(WIKI_PKG);
        driver.activateApp(WIKI_PKG);
      } catch (Exception e) {
        lastError = e;
      }

      if (isWikipediaForeground()) {
        return;
      }

      try {
        runCommand(adbPath, "-s", udid, "shell", "monkey", "-p", WIKI_PKG,
            "-c", "android.intent.category.LAUNCHER", "1");
      } catch (Exception e) {
        lastError = e;
      }

      sleepMillis(500);
    }

    String diag = buildForegroundDiagnostics(lastError);
    throw new SkipException("Wikipedia not in foreground for Appium session. " + diag);
  }

  private boolean isWikipediaForeground() {
    try {
      return WIKI_PKG.equals(safeTrim(driver.getCurrentPackage()));
    } catch (Exception e) {
      return false;
    }
  }

  private String buildForegroundDiagnostics(Exception lastError) {
    StringBuilder sb = new StringBuilder();
    try {
      sb.append("currentPackage=").append(safeTrim(driver.getCurrentPackage()));
    } catch (Exception e) {
      sb.append("currentPackage=<error>");
    }
    try {
      sb.append(" | currentActivity=").append(safeTrim(driver.currentActivity()));
    } catch (Exception e) {
      sb.append(" | currentActivity=<error>");
    }
    try {
      String source = driver.getPageSource();
      sb.append(" | pageSource=").append(truncate(source, 1500));
    } catch (Exception e) {
      sb.append(" | pageSource=<error>");
    }
    if (lastError != null && lastError.getMessage() != null) {
      sb.append(" | lastError=").append(truncate(lastError.getMessage(), 300));
    }
    return sb.toString();
  }

  private void waitForEmulatorBoot() {
    long timeoutSec = BOOT_TIMEOUT_SEC;
    long timeoutMs = TimeUnit.SECONDS.toMillis(timeoutSec);
    long start = System.currentTimeMillis();

    String adbPath = resolveAdbPath();
    String udid = resolveUdid();

    System.out.println("[MOBILE] adbPath=" + adbPath);
    System.out.println("[MOBILE] udid=" + udid);
    System.out.println("[MOBILE] bootTimeoutSec=" + timeoutSec);

    CommandResult state = runCommand(adbPath, "-s", udid, "get-state");
    if (!state.isSuccess()) {
      throw new SkipException(buildBootMessage("adb get-state failed", adbPath, udid, timeoutSec, state));
    }

    String stateValue = safeTrim(state.stdout);
    if (!"device".equals(stateValue)) {
      throw new SkipException(buildBootMessage("Device state is '" + stateValue + "'", adbPath, udid, timeoutSec, state));
    }

    CommandResult last = null;
    while (System.currentTimeMillis() - start < timeoutMs) {
      CommandResult bootCompleted =
          runCommand(adbPath, "-s", udid, "shell", "getprop", "sys.boot_completed");
      last = bootCompleted;

      String bootValue = safeTrim(bootCompleted.stdout);
      if (bootCompleted.isSuccess() && "1".equals(bootValue)) {
        return;
      }

      if (bootValue.isEmpty()) {
        CommandResult bootAnim =
            runCommand(adbPath, "-s", udid, "shell", "getprop", "init.svc.bootanim");
        last = bootAnim;

        if (bootAnim.isSuccess() && "stopped".equalsIgnoreCase(safeTrim(bootAnim.stdout))) {
          CommandResult recheck =
              runCommand(adbPath, "-s", udid, "shell", "getprop", "sys.boot_completed");
          last = recheck;
          if (recheck.isSuccess() && "1".equals(safeTrim(recheck.stdout))) {
            return;
          }
        }
      }

      sleepMillis(1000);
    }

    throw new SkipException(buildBootMessage(
        "Emulator boot not confirmed within " + timeoutSec + "s",
        adbPath,
        udid,
        timeoutSec,
        last
    ));
  }

  private String resolveAdbPath() {
    String adb = System.getProperty("adb.path");
    if (adb != null && !adb.trim().isEmpty()) {
      return adb.trim();
    }
    adb = System.getenv("ADB");
    if (adb != null && !adb.trim().isEmpty()) {
      return adb.trim();
    }
    adb = System.getenv("ADB_PATH");
    if (adb != null && !adb.trim().isEmpty()) {
      return adb.trim();
    }

    CommandResult where = runCommand("where.exe", "adb");
    if (where.isSuccess()) {
      String first = firstLine(where.stdout);
      if (!first.isEmpty()) {
        return first;
      }
    }

    return "adb";
  }

  private String resolveUdid() {
    String udid = System.getProperty("android.udid");
    if (udid != null && !udid.trim().isEmpty()) {
      return udid.trim();
    }
    udid = System.getenv("ANDROID_UDID");
    if (udid != null && !udid.trim().isEmpty()) {
      return udid.trim();
    }
    return "emulator-5554";
  }

  protected WebElement waitVisible(By locator) {
    return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
  }

  protected WebElement waitClickable(By locator) {
    return wait.until(ExpectedConditions.elementToBeClickable(locator));
  }

  protected boolean exists(By locator, Duration timeout) {
    try {
      new WebDriverWait(driver, timeout).until(ExpectedConditions.presenceOfElementLocated(locator));
      return true;
    } catch (TimeoutException e) {
      return false;
    }
  }

  protected void safeClick(By locator) {
    try {
      waitClickable(locator).click();
    } catch (Exception e) {
      sleepMillis(300);
      waitClickable(locator).click();
    }
  }

  private void ensureAppiumIsAvailable() {
    String base = Config.APPIUM_URL;
    if (base.endsWith("/")) {
      base = base.substring(0, base.length() - 1);
    }

    System.out.println("[MOBILE] appiumUrl=" + base);

    String[] urls;
    if (base.endsWith("/wd/hub")) {
      urls = new String[] { base + "/status" };
    } else {
      urls = new String[] { base + "/status", base + "/wd/hub/status" };
    }

    long deadline = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(60);
    while (System.currentTimeMillis() < deadline) {
      for (String url : urls) {
        if (isHttpOk(url)) {
          return;
        }
      }
      sleepMillis(1000);
    }

    throw new IllegalStateException(
        "Appium server is not available. Запусти appium на 127.0.0.1:4723. Проверены URL: "
            + urls[0] + (urls.length > 1 ? ", " + urls[1] : "")
    );
  }

  private boolean isHttpOk(String url) {
    try {
      java.net.HttpURLConnection conn = (java.net.HttpURLConnection) new URL(url).openConnection();
      conn.setRequestMethod("GET");
      conn.setConnectTimeout(2000);
      conn.setReadTimeout(2000);
      int code = conn.getResponseCode();
      return code >= 200 && code < 300;
    } catch (IOException e) {
      return false;
    }
  }

  private CommandResult runCommand(String... command) {
    String cmd = commandToString(command);
    ProcessBuilder builder = new ProcessBuilder(command);
    builder.redirectErrorStream(true);
    try {
      Process process = builder.start();
      StringBuilder output = new StringBuilder();
      Thread readerThread = new Thread(() -> readStream(process.getInputStream(), output));
      readerThread.setDaemon(true);
      readerThread.start();

      boolean finished = process.waitFor(COMMAND_TIMEOUT_SEC, TimeUnit.SECONDS);
      if (!finished) {
        process.destroyForcibly();
      }
      readerThread.join(TimeUnit.SECONDS.toMillis(5));

      String stdout = output.toString();
      if (!finished) {
        return CommandResult.timeout(cmd, stdout, "");
      }
      return new CommandResult(cmd, process.exitValue(), stdout, "", null, false);
    } catch (IOException e) {
      return new CommandResult(cmd, -1, "", "", e, false);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      return new CommandResult(cmd, -1, "", "", e, false);
    }
  }

  private void readStream(InputStream stream, StringBuilder output) {
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
      String line;
      while ((line = reader.readLine()) != null) {
        output.append(line).append('\n');
      }
    } catch (IOException ignored) {
    }
  }

  private String commandToString(String... command) {
    StringBuilder sb = new StringBuilder();
    for (String part : command) {
      if (sb.length() > 0) {
        sb.append(' ');
      }
      sb.append(part);
    }
    return sb.toString();
  }

  private String firstLine(String text) {
    if (text == null || text.trim().isEmpty()) {
      return "";
    }
    String[] lines = text.split("\\r?\\n");
    return lines.length == 0 ? "" : lines[0].trim();
  }

  private String safeTrim(String value) {
    return value == null ? "" : value.trim();
  }

  private String buildBootMessage(
      String reason,
      String adbPath,
      String udid,
      long timeoutSec,
      CommandResult last
  ) {
    StringBuilder sb = new StringBuilder();
    sb.append(reason);
    sb.append(" | adbPath=").append(adbPath);
    sb.append(" | udid=").append(udid);
    sb.append(" | timeoutSec=").append(timeoutSec);
    if (last != null) {
      sb.append(" | cmd=").append(last.command);
      sb.append(" | exit=").append(last.exitCode);
      if (last.timedOut) {
        sb.append(" | timedOut=true");
      }
      String out = truncate(safeTrim(last.stdout), 500);
      if (!out.isEmpty()) {
        sb.append(" | stdout=").append(out);
      }
      if (last.exception != null && last.exception.getMessage() != null) {
        sb.append(" | error=").append(last.exception.getMessage());
      }
    }
    return sb.toString();
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
      throw new IllegalStateException("Interrupted while waiting.", e);
    }
  }

  private static final class CommandResult {
    private final String command;
    private final int exitCode;
    private final String stdout;
    private final String stderr;
    private final Exception exception;
    private final boolean timedOut;

    private CommandResult(
        String command,
        int exitCode,
        String stdout,
        String stderr,
        Exception exception,
        boolean timedOut
    ) {
      this.command = command;
      this.exitCode = exitCode;
      this.stdout = stdout;
      this.stderr = stderr;
      this.exception = exception;
      this.timedOut = timedOut;
    }

    private static CommandResult timeout(String command, String stdout, String stderr) {
      return new CommandResult(command, -1, stdout, stderr, null, true);
    }

    private boolean isSuccess() {
      return exception == null && !timedOut && exitCode == 0;
    }
  }
}
