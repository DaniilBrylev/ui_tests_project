package tests.mobile;

import core.BaseMobileTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.mobile.SearchPage;

public class SearchMobileTest extends BaseMobileTest {

  @Test
  public void searchReturnsResults() {
    ensureWikipediaInForeground();
    dismissOnboardingIfPresent();

    SearchPage searchPage = new SearchPage(driver, wait);
    searchPage.openSearch();
    searchPage.typeQuery("Java");

    Assert.assertTrue(searchPage.hasResults(), "Search results should appear");
  }
}
