package stepDefinitions;

import com.mailgun.model.message.MessageResponse;
import customClasses.FundReturnData;
import customClasses.MutualFund;
import io.cucumber.java.en.Then;
import org.testng.Assert;
import testUtilities.EmailBodyUtil;
import testUtilities.SendEmailUtil;
import testUtilities.TestDataContext;

import java.util.List;

public class SendEmailStepDefinition {

    @Then("Send Email")
    public void sendEmail() {
        List<MutualFund> mutualFundRows = TestDataContext.getMutualFundRows();
        String htmlContent = EmailBodyUtil.generateHtmlContent(mutualFundRows);
        sendMutualFundsReport(htmlContent, compareFundReturn(mutualFundRows));
        TestDataContext.clearData();
    }

    private void sendMutualFundsReport(String htmlContent, boolean fundsHealthCheck) {
        System.out.println("Sending email using MailGun...");
        MessageResponse response = SendEmailUtil.sendMutualFundsReportMailGun(htmlContent, fundsHealthCheck);
        System.out.println(response);
        Assert.assertTrue(response.toString().contains("Thank"));
    }

    public boolean compareFundReturn(List<MutualFund> mutualFundRows) {
        for (MutualFund mutualFund : mutualFundRows) {
            if (!mutualFund.getFundName().equals("Index")) {
                List<FundReturnData> fundReturnDataList = mutualFund.getFundReturnDataList();
                for (FundReturnData fundReturnData : fundReturnDataList) {
                    String duration = fundReturnData.getDuration();
                    if (lessThanYearCheck(duration)) {
                        continue;
                    }
                    double trailingReturnsValue = parsePercentage(fundReturnData.getTrailingReturns());
                    double categoryAverageValue = parsePercentage(fundReturnData.getCategoryAverage());
                    double benchmarkReturnsValue = parsePercentage(fundReturnData.getBenchmarkReturns());
                    if (trailingReturnsValue < categoryAverageValue || trailingReturnsValue < benchmarkReturnsValue) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean lessThanYearCheck(String duration) {
        return duration.equals("1 Month") || duration.equals("3 Months") || duration.equals("6 Months");
    }

    private double parsePercentage(String percentage) {
        return Double.parseDouble(percentage.replace("%", ""));
    }
}
