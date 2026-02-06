package tests.web;

import core.BaseWebTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.web.AddRemoveElementsPage;

public class AddRemoveElementsWebTest extends BaseWebTest {

  @Test
  public void addAndRemoveElement() {
    AddRemoveElementsPage page = new AddRemoveElementsPage(driver, wait);
    page.open();

    Assert.assertTrue(page.isLoaded(), "Add/Remove Elements page should be loaded");

    page.clickAddElement();
    Assert.assertTrue(page.isDeleteButtonVisible(), "Delete button should appear");

    page.clickDeleteButton();
    Assert.assertTrue(page.isDeleteButtonAbsent(), "Delete button should disappear");
  }
}
