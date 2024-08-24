package stepDefinitions;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import java.net.URI;
import java.net.URL;

public class GoogleTitleTest {
    public static void main(String[] args) {
        WebDriver driver = null;
        try {
            URL gridUrl = new URI("http://192.168.1.22:4444/wd/hub").toURL();
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("browserName", "MicrosoftEdge");
            driver = new RemoteWebDriver(gridUrl, capabilities);
            driver.get("https://www.google.com");
            String title = driver.getTitle();
            System.out.println("Page title is: " + title);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}

