package com.mycompany.bankruptcy.Visualization;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import com.mycompany.bankruptcy.Model.BankruptcyModel;
import com.mycompany.bankruptcy.data.FinancialData;
import com.mycompany.bankruptcy.Analysis.FinancialAnalyzer;
import weka.core.Instance;
import weka.core.Instances;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChartGenerator {

    private BankruptcyModel model;
    private FinancialAnalyzer analyzer; // FinancialAnalyzer instance

    public ChartGenerator(BankruptcyModel model, FinancialAnalyzer analyzer) {
        this.model = model;
        this.analyzer = analyzer;
    }

    // 1. Generate a Pie chart showing risk distribution (High Risk vs Low Risk)
    public void generateRiskPieChart(List<FinancialData> dataList, String filePath) throws Exception {
        int highRisk = 0;
        int lowRisk = 0;

        // Get predictions
        List<Boolean> predictions = model.predictAll(dataList);

        // Count risks
        for (boolean isBankrupt : predictions) {
            if (isBankrupt) {
                highRisk++;
            } else {
                lowRisk++;
            }
        }

        // Create dataset
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("High Risk", highRisk);
        dataset.setValue("Low Risk", lowRisk);

        // Create chart
        JFreeChart pieChart = ChartFactory.createPieChart("Risk Distribution", dataset, true, true, false);

        // Save chart
        saveChartToFile(filePath, pieChart);
    }

    // 2. Generate a Bar chart comparing mean values for features between Bankrupt and Not Bankrupt
    public void generateFeatureComparisonBarChart(List<FinancialData> dataList, String filePath) throws Exception {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Separate data into two groups: Bankrupt and Not Bankrupt
        List<FinancialData> bankruptData = new ArrayList<>();
        List<FinancialData> notBankruptData = new ArrayList<>();

        for (FinancialData fd : dataList) {
            if (fd.isBankrupt()) {
                bankruptData.add(fd);
            } else {
                notBankruptData.add(fd);
            }
        }

        // Compare means for each feature
        dataset.addValue(calculateMean(bankruptData, "ROA_C"), "Bankrupt", "ROA_C");
        dataset.addValue(calculateMean(notBankruptData, "ROA_C"), "Not Bankrupt", "ROA_C");

        dataset.addValue(calculateMean(bankruptData, "Operating_Gross_Margin"), "Bankrupt", "Operating_Gross_Margin");
        dataset.addValue(calculateMean(notBankruptData, "Operating_Gross_Margin"), "Not Bankrupt", "Operating_Gross_Margin");

        dataset.addValue(calculateMean(bankruptData, "Debt_Ratio"), "Bankrupt", "Debt_Ratio");
        dataset.addValue(calculateMean(notBankruptData, "Debt_Ratio"), "Not Bankrupt", "Debt_Ratio");

        // More features can be added similarly

        // Create bar chart
        JFreeChart barChart = ChartFactory.createBarChart(
                "Feature Comparison: Bankrupt vs Not Bankrupt", 
                "Feature", 
                "Mean Value", 
                dataset 
        );

        // Save the chart as an image file
        saveChartToFile(filePath, barChart);
    }

    // 3. Generate a histogram to show the distribution of debt ratio
    public void generateDebtRatioHistogram(List<FinancialData> dataList, String filePath) throws Exception {
        HistogramDataset dataset = new HistogramDataset();

        double[] debtRatios = new double[dataList.size()];
        for (int i = 0; i < dataList.size(); i++) {
            debtRatios[i] = dataList.get(i).getDebtRatio();
        }

        dataset.addSeries("Debt Ratio Distribution", debtRatios, 10); // 10 bins

        // Create histogram chart
        JFreeChart histogramChart = ChartFactory.createHistogram(
                "Debt Ratio Distribution", "Debt Ratio", "Frequency", dataset);

        // Save the chart as an image file
        saveChartToFile(filePath, histogramChart);
    }

    // 4. Generate a histogram showing prediction probabilities for bankruptcy classification
    public void generatePredictionProbabilitiesHistogram(List<FinancialData> dataList, String filePath) throws Exception {
        List<Double> probabilities = model.predictAllProbabilities(dataList);

        HistogramDataset dataset = new HistogramDataset();
        double[] probArray = probabilities.stream().mapToDouble(Double::doubleValue).toArray();

        dataset.addSeries("Prediction Probabilities", probArray, 10); // 10 bins for histogram

        JFreeChart histogramChart = ChartFactory.createHistogram(
                "Prediction Probabilities", "Probability", "Frequency", dataset);

        saveChartToFile(filePath, histogramChart);
    }

    // 5. Generate a pie chart showing Bankruptcy Risk Levels (Based on FinancialAnalyzer's Z-Score)
    public void generateRiskLevelPieChart(List<FinancialData> dataList, String filePath) throws Exception {
        int highRisk = 0;
        int lowRisk = 0;

        // Analyze the risk level based on Z-Score using FinancialAnalyzer
        for (FinancialData fd : dataList) {
            double zScore = analyzer.calculateAltmanZScore(fd);
            if (zScore < 1.8) {
                highRisk++;
            } else {
                lowRisk++;
            }
        }

        // Create dataset
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("High Risk", highRisk);
        dataset.setValue("Low Risk", lowRisk);

        // Create chart
        JFreeChart pieChart = ChartFactory.createPieChart("Bankruptcy Risk Levels (Z-Score)", dataset, true, true, false);

        // Save chart
        saveChartToFile(filePath, pieChart);
    }

    // Helper method to calculate the mean of a feature
    private double calculateMean(List<FinancialData> dataList, String feature) {
        double sum = 0.0;
        int count = 0;

        for (FinancialData fd : dataList) {
            switch (feature) {
                case "ROA_C":
                    sum += fd.getRoaC();
                    break;
                case "Operating_Gross_Margin":
                    sum += fd.getOperatingMargin();
                    break;
                case "Debt_Ratio":
                    sum += fd.getDebtRatio();
                    break;
                case "Net_Income_to_Total_Assets":
                    sum += fd.getNetIncomeToTotalAssets();
                    break;
                case "Gross_Profit_to_Sales":
                    sum += fd.getGrossProfitToSales();
                    break;
                case "Equity_to_Liability":
                    sum += fd.getEquityToLiability();
                    break;
            }
            count++;
        }

        return (count > 0) ? sum / count : 0.0;
    }

    // Save chart to file
    private void saveChartToFile(String filePath, JFreeChart chart) throws IOException {
        try {
            ChartUtils.saveChartAsJPEG(new File(filePath), chart, 800, 600);
            System.out.println("Chart saved to: " + filePath);
        } catch (IOException e) {
            System.err.println("Error saving chart: " + e.getMessage());
            throw e;
        }
    }
}
