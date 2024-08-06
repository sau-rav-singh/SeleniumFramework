package customClasses;

import lombok.Data;

@Data
public class FundReturnData {
    private String duration;
    private String trailingReturns;
    private String categoryAverage;
    private String benchmarkReturns;
    public FundReturnData(String duration, String trailingReturns, String categoryAverage, String benchmarkReturns) {
        this.duration = duration;
        this.trailingReturns = trailingReturns;
        this.categoryAverage = categoryAverage;
        this.benchmarkReturns = benchmarkReturns;
    }
}
