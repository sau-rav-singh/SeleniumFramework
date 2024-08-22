package pageObjects;

import com.mailgun.api.v3.MailgunMessagesApi;
import com.mailgun.client.MailgunClient;
import com.mailgun.model.message.Message;
import com.mailgun.model.message.MessageResponse;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import testUtilities.GenericUtils;

import java.time.Duration;
import java.util.Properties;

public class IndexAlertPage {

    public WebDriver driver;
    private final By NOTIFICATIONPOPUP = By.xpath("//button[@id='wzrk-cancel']");
    private final By INDEXVALUE = By.xpath("//span[@id='sp_val']");
    private final By FIFTYTWOWEEKHIGHVALUE = By.xpath("//div[@id='sp_yearlyhigh']");
    private String indexValue;
    private String fiftyTwoWkHighValue;

    private String indexName;

    public IndexAlertPage(WebDriver driver) {
        this.driver = driver;
    }


    public void navigateToIndexPage(String index) {
        indexName = index;
        Properties prop = GenericUtils.getDataFromPropertyFile("global");
        driver.get(prop.getProperty(index));
        closeMCPopUp();
    }

    public void captureCurrentIndexValue() {
        indexValue =currentIndexValue();
        System.out.println("indexValue value is " + indexValue);
    }

    public String currentIndexValue(){
        return driver.findElement(INDEXVALUE).getAttribute("data-numberanimate-value");
    }
    public void closeMCPopUp(){
        clickIfPresent(NOTIFICATIONPOPUP);
    }
    public void capture52WkHighValue() {
        fiftyTwoWkHighValue = driver.findElement(FIFTYTWOWEEKHIGHVALUE).getText();
        System.out.println("fiftyTwoWkHighValue value is " + fiftyTwoWkHighValue);
    }

    public void sendEmailOnThresholdBreach(String threshold) {
        double currentValue = parseCurrencyString(indexValue);
        double weekHigh = parseCurrencyString(fiftyTwoWkHighValue);
        double thresholdPercentage = parsePercentageString(threshold);
        double thresholdValue = weekHigh * (1 - (thresholdPercentage / 100));
        if (currentValue < thresholdValue) {
            System.out.println(sendIndexFallAlert(indexName, threshold));
        } else {
            System.out.println("No alert needed.");
        }

    }

    private static double parseCurrencyString(String currencyStr) {
        return Double.parseDouble(currencyStr.replaceAll(",", ""));
    }

    private static double parsePercentageString(String percentageStr) {
        return Double.parseDouble(percentageStr.replaceAll("%", ""));
    }

    public static MessageResponse sendIndexFallAlert(String indexName, String threshold) {
        Properties prop = GenericUtils.getDataFromPropertyFile("global");
        MailgunMessagesApi mailgunMessagesApi = MailgunClient.config(prop.getProperty("mailgun_api_key"))
                .createApi(MailgunMessagesApi.class);

        Message message = Message.builder()
                .from("Gitlab Runner <USER@YOURDOMAIN.COM>")
                .to("singh.saurav@icloud.com")
                .subject("Index Fall Alert")
                .text(indexName + " is lower than " + threshold + " value")
                .build();
        return mailgunMessagesApi.sendMessage(prop.getProperty("mailgun_domain"), message);
    }

    public static boolean isElementPresent(By locator, WebDriver driver) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            return element != null;
        } catch (org.openqa.selenium.TimeoutException e) {
            return false;
        }
    }

    public void clickIfPresent(By locator) {
        if (isElementPresent(locator, driver)) {
            driver.findElement(locator).click();
        }
    }

}
