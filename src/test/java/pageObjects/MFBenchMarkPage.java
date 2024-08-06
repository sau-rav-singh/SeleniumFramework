package pageObjects;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;
import customClasses.MutualFund;
import org.openqa.selenium.*;
import testUtilities.GenericUtils;
import testUtilities.TestDataContext;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Properties;

public class MFBenchMarkPage {

    public WebDriver driver;
    public IndexAlertPage indexAlertPage;

    public MFBenchMarkPage(WebDriver driver) {
        this.driver = driver;
        indexAlertPage = new IndexAlertPage(driver);
    }

    public void getUrl(String fundType) {
        Properties prop = GenericUtils.getDataFromPropertyFile("global");
        driver.get(prop.getProperty(fundType));
        ExtentCucumberAdapter.getCurrentStep().log(Status.INFO, "WebPage Opened is " + prop.getProperty(fundType));
        ExtentCucumberAdapter.getCurrentStep().log(Status.PASS, MediaEntityBuilder.createScreenCaptureFromBase64String(getBase64Screenshot()).build());
    }

    public String getBase64Screenshot() {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
    }

    public String captureTrailingReturns(String duration) {
        return driver.findElement(returnValueLocator("Trailing", duration)).getText();
    }

    public String captureCategoryAverageReturns(String duration) {
        String categoryAverageText = driver.findElement(returnValueLocator("CategoryAverage", duration)).getText();
        double categoryAverageValue = Double.parseDouble(categoryAverageText.replace("%", "").trim());
        return String.format("%.2f%%", categoryAverageValue);
    }

    public String captureBenchMarkReturns(String duration) {
        indexAlertPage.closeMCPopUp();
        String durationId = getDurationId(duration);
        WebElement spanElement = driver.findElement(By.xpath(String.format("//span[@id='%s']", durationId)));
        String absoluteReturnPercentage = spanElement.getAttribute("textContent");
        String currentValue = indexAlertPage.currentIndexValue();
        return calculateCAGRWithAbsoluteReturn(duration, absoluteReturnPercentage, currentValue);
    }

    public static String calculateCAGRWithAbsoluteReturn(String duration, String absoluteReturnPercentage, String currentValue) {
        if ("1 month".equalsIgnoreCase(duration) || "3 months".equalsIgnoreCase(duration) || "6 months".equalsIgnoreCase(duration)) {
            return absoluteReturnPercentage;
        }
        double parsedAbsoluteReturn = parsePercentage(absoluteReturnPercentage);
        double parsedEndingValue = parseNumber(currentValue);
        double numberOfYears = convertDurationToYears(duration);
        double beginningValue = parsedEndingValue / (1 + parsedAbsoluteReturn);
        double cagr = Math.pow(parsedEndingValue / beginningValue, 1.0 / numberOfYears) - 1;
        return String.format("%.3f%%", cagr * 100);
    }

    private static double parsePercentage(String percentage) {
        try {
            String sanitizedInput = percentage.replaceAll("--%", "0%");
            return NumberFormat.getPercentInstance().parse(sanitizedInput).doubleValue();
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid percentage format: " + percentage);
        }
    }

    private static double parseNumber(String number) {
        try {
            return NumberFormat.getInstance().parse(number).doubleValue();
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid number format: " + number);
        }
    }

    private static double convertDurationToYears(String duration) {
        return switch (duration.toLowerCase()) {
            case "1 month" -> 1.0 / 12; // Assuming an average of 30 days in a month
            case "3 months" -> 3.0 / 12;
            case "6 months" -> 6.0 / 12;
            case "1 year" -> 1.0;
            case "2 year" -> 2.0;
            case "3 years" -> 3.0;
            case "5 years" -> 5.0;
            default -> throw new IllegalArgumentException("Invalid duration: " + duration);
        };
    }

    private String getDurationId(String duration) {
        return switch (duration) {
            case "1 Month" -> "m1";
            case "3 Months" -> "m3";
            case "6 Months" -> "m6";
            case "1 Year" -> "y1";
            case "3 Years" -> "y3";
            case "5 Years" -> "y5";
            default -> throw new IllegalArgumentException("Invalid duration: " + duration);
        };
    }

    public static By returnValueLocator(String returnType, String period) {
        int rowOffset = switch (period) {
            case "3 Months" -> 2;
            case "6 Months" -> 3;
            case "1 Year" -> 4;
            case "3 Years" -> 5;
            case "5 Years" -> 6;
            default -> 1;
        };

        int columnOffset = returnType.equals("Trailing") ? 2 : 3;

        return By.xpath(String.format("//table[@id='trailingReturnTable']/tbody/tr[%d]/td[%d]", rowOffset, columnOffset));
    }

    public void setFundReturnData(String fundType, String duration, String trailingReturns, String categoryAverage, String benchmarkReturns) {
        MutualFund mutualFund = TestDataContext.getMutualFund(fundType);

        if (mutualFund == null) {
            mutualFund = new MutualFund(fundType);
        }
        mutualFund.addOrUpdateFundReturnData(duration, trailingReturns, categoryAverage, benchmarkReturns);

        TestDataContext.addMutualFund(mutualFund);
    }
}
