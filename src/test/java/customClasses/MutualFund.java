package customClasses;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MutualFund {
    private String fundName;
    private List<FundReturnData> fundReturnDataList;

    public MutualFund(String fundName) {
        this.fundName = fundName;
        this.fundReturnDataList = new ArrayList<>();
    }

    public void addOrUpdateFundReturnData(String duration, String trailingReturns, String categoryAverage, String benchmarkReturns) {
        for (int i = 0; i < fundReturnDataList.size(); i++) {
            if (fundReturnDataList.get(i).getDuration().equals(duration)) {
                FundReturnData existingData = fundReturnDataList.get(i);
                existingData.setTrailingReturns(trailingReturns != null ? trailingReturns : existingData.getTrailingReturns());
                existingData.setCategoryAverage(categoryAverage != null ? categoryAverage : existingData.getCategoryAverage());
                existingData.setBenchmarkReturns(benchmarkReturns != null ? benchmarkReturns : existingData.getBenchmarkReturns());
                return;
            }
        }
        FundReturnData newFundReturnData = new FundReturnData(duration, trailingReturns, categoryAverage, benchmarkReturns);
        fundReturnDataList.add(newFundReturnData);
    }
}
