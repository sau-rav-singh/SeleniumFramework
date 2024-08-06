package stepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import pageObjects.MFBenchMarkPage;
import testUtilities.TestContextSetup;

public class MFBenchMarkStepDefinition {
    String trailingReturns = "N/A";
    String categoryAverage = "N/A";
    String benchmarkReturns = "N/A";
    String fundType;
    TestContextSetup testContextSetup;
    MFBenchMarkPage benchMark_CategoryAveragePage;

    public MFBenchMarkStepDefinition(TestContextSetup testContextSetup) {
        this.testContextSetup = testContextSetup;
        this.benchMark_CategoryAveragePage = testContextSetup.pageObjectManager.getBenchMark_CategoryAveragePage();
    }

    @Given("User is on ET Money {string} Fund Page")
    public void user_is_on_et_money_fund_page(String fundType) {
        this.fundType = fundType;
        benchMark_CategoryAveragePage.getUrl(fundType);
    }

    @Then("Capture Trailing Returns for {string} period for {string}")
    public void captureTrailingReturnsForPeriod(String duration, String fundType) {
        trailingReturns = benchMark_CategoryAveragePage.captureTrailingReturns(duration);
        categoryAverage = benchMark_CategoryAveragePage.captureCategoryAverageReturns(duration);
        benchMark_CategoryAveragePage.setFundReturnData(fundType, duration, trailingReturns, categoryAverage, null);
    }

    @Given("User is on MoneyControl's Benchmark page of {string}")
    public void userIsOnMoneyControlSBenchmarkPageOf(String fundName) {
        benchMark_CategoryAveragePage.getUrl(fundName + "_MC");
    }

    @Then("Capture Absolute Returns for {string} period for {string}")
    public void captureAbsoluteReturnsForPeriod(String duration, String fundType) {
        benchmarkReturns = benchMark_CategoryAveragePage.captureBenchMarkReturns(duration);
        benchMark_CategoryAveragePage.setFundReturnData(fundType, duration, null, null, benchmarkReturns);
    }

    @Then("Close the Browser")
    public void closeTheBrowser() {
        testContextSetup.testBase.quitWebDriver();
    }
}