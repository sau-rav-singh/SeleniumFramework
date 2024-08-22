package testUtilities;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import java.io.FileInputStream;
import java.util.Properties;


public class GenericUtils {
    public WebDriver driver;

    public GenericUtils(WebDriver driver) {
        this.driver = driver;
    }

    public static Properties getDataFromPropertyFile(String fileName) {

        Properties prop;
        prop = new Properties();
        try {
            FileInputStream fis = new FileInputStream(
                    System.getProperty("user.dir") + "/src/test/resources/" + fileName + ".properties");
            prop.load(fis);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return prop;

    }

    public static boolean comparePercentageValues(String value1, String value2) {
        double percentage1 = Double.parseDouble(value1.replace("%", ""));
        double percentage2 = Double.parseDouble(value2.replace("%", ""));
        return percentage1 > percentage2;
    }

    public static void assertion(boolean condition, String message) {
        if (condition) {
            ExtentCucumberAdapter.getCurrentStep().log(Status.PASS, "Assertion for " + message + " is successful");
            Assert.assertTrue(true);
        } else {
            ExtentCucumberAdapter.getCurrentStep().log(Status.FAIL, "Assertion for " + message + " has failed");
            Assert.fail("Assertion for " + message + " has failed");
        }
    }
}
