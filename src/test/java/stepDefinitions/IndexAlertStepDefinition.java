package stepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import pageObjects.IndexAlertPage;
import testUtilities.TestContextSetup;

public class IndexAlertStepDefinition {

    TestContextSetup testContextSetup;
    IndexAlertPage indexAlertPage;

    public IndexAlertStepDefinition(TestContextSetup testContextSetup) {
        this.testContextSetup = testContextSetup;
        this.indexAlertPage = testContextSetup.pageObjectManager.getIndexAlertPage();
    }

    @Given("User is on MoneyControl page of {string}")
    public void userIsOnMoneyControlPageOf(String index) {
        indexAlertPage.navigateToIndexPage(index);
    }

    @Then("Capture Current Value and 52Wk High Value")
    public void captureCurrentValueAndWkHighValue() {
        indexAlertPage.captureCurrentIndexValue();
        indexAlertPage.capture52WkHighValue();
    }

    @Then("Send Email alert if Index Value is less then {string} Threshold")
    public void sendEmailAlertIfIndexValueIsLessThenThreshold(String threshold) {

        indexAlertPage.sendEmailOnThresholdBreach(threshold);
    }
}
