package tests.web;

import core.BaseWebTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.web.LoginPage;
import pages.web.SecureAreaPage;

public class LoginWebTest extends BaseWebTest {

  @Test
  public void userCanLogin() {
    LoginPage loginPage = new LoginPage(driver, wait);
    loginPage.open();

    Assert.assertTrue(loginPage.isLoaded(), "Login page should be loaded");

    loginPage.login("tomsmith", "SuperSecretPassword!");

    SecureAreaPage secureAreaPage = new SecureAreaPage(driver, wait);
    Assert.assertTrue(secureAreaPage.isLoaded(), "Secure area should be loaded");
    Assert.assertTrue(
        secureAreaPage.getFlashMessage().contains("You logged into a secure area!"),
        "Flash message should confirm login"
    );
  }
}
