package tests.mobile;

import core.BaseMobileTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.mobile.MainPage;

public class SmokeMobileTest extends BaseMobileTest {

  @Test
  public void mainPageLoads() {
    dismissOnboardingIfPresent();

    MainPage mainPage = new MainPage(driver, wait);
    Assert.assertTrue(mainPage.isLoaded(), "Main page should be loaded");
  }
}
