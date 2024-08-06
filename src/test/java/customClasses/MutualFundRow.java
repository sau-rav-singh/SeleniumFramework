package customClasses;

import lombok.Getter;

@Getter
public class MutualFundRow {
    private final String fundName;
    private final String returns;

    public MutualFundRow(String fundName, String returns) {
        this.fundName = fundName;
        this.returns = returns;
    }

}

