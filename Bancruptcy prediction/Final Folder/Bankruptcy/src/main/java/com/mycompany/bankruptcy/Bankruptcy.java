package com.mycompany.bankruptcy;

import weka.core.Instances;
import java.util.List;
import com.mycompany.bankruptcy.data.FinancialData;
import com.mycompany.bankruptcy.data.DataLoader;
import com.mycompany.bankruptcy.Visualization.*;
import com.mycompany.bankruptcy.Model.*;
import com.mycompany.bankruptcy.Analysis.FinancialAnalyzer;

public class Bankruptcy {
    public static void main(String[] args) {
        try {
            // Load financial data
            DataLoader loader = new DataLoader();
            List<FinancialData> dataList = loader.loadData();

            // Check if data is loaded
            if (dataList.isEmpty()) {
                System.out.println("No data loaded. Exiting...");
                return;
            }
            System.out.println("Loaded " + dataList.size() + " rows of financial data.");

            // Create and preprocess the dataset
            BankruptcyModel model = new BankruptcyModel();
            Instances data = model.preprocessData(dataList);

            // Create an instance of FinancialAnalyzer
            FinancialAnalyzer analyzer = new FinancialAnalyzer();

            // Split data into training and testing sets
            int trainSize = (int) Math.round(data.numInstances() * 0.8);
            int testSize = data.numInstances() - trainSize;

            Instances trainingData = new Instances(data, 0, trainSize);
            Instances testingData = new Instances(data, trainSize, testSize);

            System.out.println("Training size: " + trainingData.numInstances());
            System.out.println("Testing size: " + testingData.numInstances());

            // Train the model
            System.out.println("Training the model...");
            model.train(trainingData);

            // Evaluate the model
            System.out.println("Evaluating the model...");
            model.evaluate(testingData);

            // Generate a CSV report
            System.out.println("Generating report...");
            ReportGenerator reportGenerator = new ReportGenerator(model);  // Pass model to the constructor
            reportGenerator.generateReport(dataList, "financial_report.csv");

            // Create ChartGenerator with both model and analyzer
            ChartGenerator chartGenerator = new ChartGenerator(model, analyzer);  // Pass both model and analyzer to the constructor

            // Generate a pie chart for risk distribution
            System.out.println("Generating risk distribution chart...");
            chartGenerator.generateRiskPieChart(dataList, "risk_chart.jpg");

            // Generate additional visualizations
            System.out.println("Generating feature comparison bar chart...");
            chartGenerator.generateFeatureComparisonBarChart(dataList, "feature_comparison_chart.jpg");

            System.out.println("Generating debt ratio distribution histogram...");
            chartGenerator.generateDebtRatioHistogram(dataList, "debt_ratio_histogram.jpg");

            // Generate prediction probability histogram
            System.out.println("Generating prediction probability histogram...");
            chartGenerator.generatePredictionProbabilitiesHistogram(dataList, "prediction_probabilities_histogram.jpg");

            // Generate risk level pie chart based on FinancialAnalyzer's Z-Score
            System.out.println("Generating bankruptcy risk level pie chart (Z-Score)...");
            chartGenerator.generateRiskLevelPieChart(dataList, "risk_level_z_score_chart.jpg");

            System.out.println("All functionalities executed successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
