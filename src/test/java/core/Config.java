package core;

public final class Config {
  public static final String WEB_BASE_URL = "https://the-internet.herokuapp.com";
  public static final String APPIUM_URL = "http://127.0.0.1:4723/wd/hub";
  public static final String ANDROID_UDID = System.getenv("ANDROID_UDID");
  public static final int WEB_TIMEOUT_SECONDS = 10;
  public static final int MOBILE_TIMEOUT_SECONDS = 15;

  private Config() {
  }
}
