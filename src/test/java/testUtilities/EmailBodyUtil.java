package testUtilities;

import customClasses.FundReturnData;
import customClasses.MutualFund;

import java.util.List;

public class EmailBodyUtil {

    public static String generateHtmlContent(List<MutualFund> rows) {
        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<html><head><style>")
                .append("body { font-family: 'Arial', sans-serif; }")
                .append("h2 { color: #27ae60; }")
                .append("table { border-collapse: collapse; width: 100%; margin-top: 15px; border-radius: 8px; overflow: hidden; }")
                .append("th, td { border: 1px solid #27ae60; padding: 8px; text-align: left; }")
                .append("th { background-color: #27ae60; color: white; }")
                .append("</style></head><body>");

        for (MutualFund row : rows) {
            htmlContent.append("<h2>").append(row.getFundName()).append("</h2>");
            htmlContent.append("<table><tr><th>S No</th><th>Duration</th><th>")
                    .append("Trailing Returns</th><th>Category Average</th><th>Benchmark Returns</th></tr>");

            int index = 1;
            for (FundReturnData fundReturnData : row.getFundReturnDataList()) {
                htmlContent.append("<tr><td>").append(index).append("</td><td>")
                        .append(fundReturnData.getDuration()).append("</td><td>")
                        .append(formatCellValue(fundReturnData.getTrailingReturns())).append("</td><td>")
                        .append(formatCellValue(fundReturnData.getCategoryAverage())).append("</td><td>")
                        .append(formatCellValue(fundReturnData.getBenchmarkReturns())).append("</td></tr>");
                index++;
            }

            htmlContent.append("</table>");
        }

        htmlContent.append("</body></html>");

        return htmlContent.toString();
    }

    private static String formatCellValue(String value) {
        return value != null ? value : "N/A";
    }
}
