package testRunner;

import org.testng.annotations.Listeners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@Listeners({listeners.ExtentReportConfigListener.class,listeners.MySuiteListener.class})

@CucumberOptions(features = "src/test/resources/features", glue = "stepDefinitions", monochrome = true, tags = "@RestAPI", plugin = {"pretty", "html:target/cucumber.html", "json:target/cucumber.json", "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"})

public class APITestRunner extends AbstractTestNGCucumberTests {
}