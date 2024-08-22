package pageObjects;

import org.openqa.selenium.WebDriver;

public class PageObjectManager {
    public WebDriver driver;

    public PageObjectManager(WebDriver driver) {
        this.driver = driver;
    }

    public MFBenchMarkPage getBenchMark_CategoryAveragePage() {
        return new MFBenchMarkPage(driver);
    }

    public IndexAlertPage getIndexAlertPage() {
        return new IndexAlertPage(driver);
    }

}
