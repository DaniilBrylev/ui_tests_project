package tests.web;

import core.BaseWebTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.web.DropdownPage;

public class DropdownWebTest extends BaseWebTest {

  @Test
  public void canSelectOption() {
    DropdownPage page = new DropdownPage(driver, wait);
    page.open();

    Assert.assertTrue(page.isLoaded(), "Dropdown page should be loaded");

    page.selectOptionByText("Option 1");
    Assert.assertEquals(page.getSelectedOptionText(), "Option 1");
  }
}
