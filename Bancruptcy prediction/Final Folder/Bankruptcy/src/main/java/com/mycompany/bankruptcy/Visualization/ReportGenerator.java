package com.mycompany.bankruptcy.Visualization;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import com.mycompany.bankruptcy.Model.BankruptcyModel;
import com.mycompany.bankruptcy.data.FinancialData;
import com.mycompany.bankruptcy.Analysis.FinancialAnalyzer;

public class ReportGenerator {

    private BankruptcyModel model;
    private FinancialAnalyzer analyzer;  // Instance of FinancialAnalyzer

    public ReportGenerator(BankruptcyModel model) {
        this.model = model;
        this.analyzer = new FinancialAnalyzer();  // Instantiate FinancialAnalyzer
    }

    public void generateReport(List<FinancialData> dataList, String filePath) throws Exception {
        try (FileWriter writer = new FileWriter(filePath)) {
            // Write header with additional columns for financial analysis
            writer.write("ROA(C),ROA(A),ROA(B),Operating Margin,Profit Rate,Debt Ratio,Net Worth to Assets," +
                    "Liabilities to Equity,Equity to Liability,Cash Flow to Sales,Net Income to Total Assets," +
                    "Gross Profit to Sales,Predicted Risk Level,Predicted Probability,Debt-to-Equity Ratio,Z-Score\n");

            // Get predictions probabilities
            List<Double> probabilities = model.predictAllProbabilities(dataList);

            // Write data with additional predictions and probabilities
            for (int i = 0; i < dataList.size(); i++) {
                FinancialData data = dataList.get(i);
                String riskLevel = probabilities.get(i) >= 0.5 ? "High Risk" : "Low Risk";
                double predictedProbability = probabilities.get(i);

                // Financial metrics from FinancialAnalyzer
                double debtToEquity = analyzer.calculateDebtToEquityRatio(data);  // Debt-to-Equity Ratio
                double zScore = analyzer.calculateAltmanZScore(data);  // Z-Score

                // Write data to CSV, including financial analysis results
                writer.write(data.getRoaC() + "," +
                        data.getRoaA() + "," +
                        data.getRoaB() + "," +
                        data.getOperatingMargin() + "," +
                        data.getProfitRate() + "," +
                        data.getDebtRatio() + "," +
                        data.getNetWorthToAssets() + "," +
                        data.getLiabilitiesToEquity() + "," +
                        data.getEquityToLiability() + "," +
                        data.getCashFlowToSales() + "," +
                        data.getNetIncomeToTotalAssets() + "," +
                        data.getGrossProfitToSales() + "," +
                        riskLevel + "," +
                        predictedProbability + "," +
                        debtToEquity + "," +
                        zScore + "\n");
            }

            System.out.println("Report successfully saved to: " + filePath);

        } catch (IOException e) {
            System.err.println("Error writing the report: " + e.getMessage());
        }
    }
}
