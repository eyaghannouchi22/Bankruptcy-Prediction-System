package com.mycompany.bankruptcy.Analysis;

import java.util.List;
import com.mycompany.bankruptcy.data.FinancialData;
import com.mycompany.bankruptcy.data.DataLoader;

public class FinancialAnalyzer {

    // Method to calculate Debt-to-Equity Ratio using `liabilitiesToEquity`
    public double calculateDebtToEquityRatio(FinancialData data) {
        return data.getLiabilitiesToEquity();
    }

    // Method to calculate a simplified Altman Z-Score (example formula)
    public double calculateAltmanZScore(FinancialData data) {
        // Example formula:
        // Z = 1.2 * (Net Worth / Total Assets) + 3.3 * (Cash Flow to Sales)
        return 1.2 * data.getNetWorthToAssets() + 3.3 * data.getCashFlowToSales();
    }

    // Method to analyze all financial data and identify high-risk cases
    public void analyzeAll(List<FinancialData> dataList) {
        System.out.println("Analyzing financial data...");
        for (FinancialData data : dataList) {
            double zScore = calculateAltmanZScore(data);

            // Print results based on Z-Score thresholds
            if (zScore < 1.8) {
                System.out.println("High bankruptcy risk: " + data);
            } else {
                System.out.println("Low bankruptcy risk: " + data);
            }
        }
    }

    // Inner class for advanced analysis
    public class AdvancedFinancialAnalyzer extends FinancialAnalyzer {
        // Method for a more complex Z-Score using `debtRatio`
        public double calculateAdvancedZScore(FinancialData data) {
            // Example: Add `debtRatio` as a factor in the advanced Z-Score
            return 1.2 * data.getNetWorthToAssets() +
                   3.3 * data.getCashFlowToSales() -
                   1.5 * data.getDebtRatio();
        }

        // Override analyzeAll to use the advanced Z-Score formula
        @Override
        public void analyzeAll(List<FinancialData> dataList) {
            System.out.println("Performing advanced analysis...");
            for (FinancialData data : dataList) {
                double advancedZScore = calculateAdvancedZScore(data);

                // Print results based on advanced Z-Score thresholds
                if (advancedZScore < 2.5) {
                    System.out.println("Advanced risk detected: " + data);
                } else {
                    System.out.println("Advanced low-risk case: " + data);
                }
            }
        }
    }
}

