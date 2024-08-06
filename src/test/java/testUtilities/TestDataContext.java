package testUtilities;

import customClasses.MutualFund;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestDataContext {
    private static final Map<String, MutualFund> mutualFunds = new HashMap<>();

    public static void addMutualFund(MutualFund mutualFund) {
        mutualFunds.put(mutualFund.getFundName(), mutualFund);
    }

    public static MutualFund getMutualFund(String fundName) {
        return mutualFunds.get(fundName);
    }

    public static List<MutualFund> getMutualFundRows() {
        return new ArrayList<>(mutualFunds.values());
    }

    public static void clearData() {
        mutualFunds.clear();
    }
}
