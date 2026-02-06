package tests.mobile;

import core.BaseMobileTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.mobile.ArticlePage;
import pages.mobile.SearchPage;

public class OpenArticleMobileTest extends BaseMobileTest {

  @Test
  public void openFirstSearchResult() {
    ensureWikipediaInForeground();
    dismissOnboardingIfPresent();

    SearchPage searchPage = new SearchPage(driver, wait);
    searchPage.openSearch();
    searchPage.typeQuery("Java");
    searchPage.waitResultsVisible();
    searchPage.openFirstResult();

    ArticlePage articlePage = new ArticlePage(driver, wait);
    Assert.assertTrue(
        articlePage.titleContains("Java"),
        "Article title should contain Java"
    );
  }
}
