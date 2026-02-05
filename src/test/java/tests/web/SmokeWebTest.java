package tests.web;

import core.BaseWebTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.web.HomePage;

public class SmokeWebTest extends BaseWebTest {

  @Test
  public void checkboxesLinkOpens() {
    HomePage home = new HomePage(driver, wait);
    home.open();

    System.out.println("URL=" + driver.getCurrentUrl());
    System.out.println("Title=" + driver.getTitle());
    try {
      System.out.println("H1=" + driver.findElement(By.tagName("h1")).getText());
    } catch (Exception e) {
      System.out.println("H1=<not found>");
    }
    Assert.assertTrue(home.isLoaded(), "Home page should be loaded");

    home.clickLink("Checkboxes");

    Assert.assertTrue(driver.getCurrentUrl().contains("/checkboxes"), "URL should contain /checkboxes");

    WebElement header = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("h3")));
    Assert.assertEquals(header.getText(), "Checkboxes", "Checkboxes page header should be visible");
  }
}
