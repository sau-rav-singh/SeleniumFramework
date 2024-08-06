package testUtilities;

import pageObjects.PageObjectManager;

import java.net.MalformedURLException;

public class TestContextSetup {
    public PageObjectManager pageObjectManager;
    public GenericUtils genericUtils;
    public TestBase testBase;

    public TestContextSetup() throws MalformedURLException {
        testBase=new TestBase();
        pageObjectManager = new PageObjectManager(testBase.getDriver());
        genericUtils = new GenericUtils(testBase.getDriver());
    }
}
