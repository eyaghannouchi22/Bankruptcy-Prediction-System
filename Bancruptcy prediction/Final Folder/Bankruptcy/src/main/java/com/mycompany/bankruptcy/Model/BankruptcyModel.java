package com.mycompany.bankruptcy.Model;

import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.classifiers.Evaluation;
import weka.filters.Filter;
import weka.core.Attribute;
import weka.core.Instances;
import weka.filters.unsupervised.attribute.Normalize;
import weka.attributeSelection.*;
import com.mycompany.bankruptcy.data.FinancialData;

import java.util.ArrayList;
import java.util.List;
import weka.core.DenseInstance;
import weka.core.Instance;

public class BankruptcyModel {

    private Classifier model;

    public BankruptcyModel() {
        // Use J48 with tuned parameters for better performance
        J48 j48 = new J48();
        j48.setConfidenceFactor(0.25f); // Adjust confidence factor
        j48.setMinNumObj(2); // Minimum number of objects per leaf node
        j48.setUseLaplace(true); // Enable Laplace smoothing to improve probability estimation
        this.model = j48;
    }

    // Preprocess the dataset
    public Instances preprocessData(List<FinancialData> dataList) throws Exception {
        ArrayList<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("ROA_C"));
        attributes.add(new Attribute("ROA_A"));
        attributes.add(new Attribute("ROA_B"));
        attributes.add(new Attribute("Operating_Gross_Margin"));
        attributes.add(new Attribute("Operating_Profit_Rate"));
        attributes.add(new Attribute("Debt_Ratio"));
        attributes.add(new Attribute("Net_Worth_to_Assets"));
        attributes.add(new Attribute("Liabilities_to_Equity"));
        attributes.add(new Attribute("Cash_Flow_to_Sales"));
        attributes.add(new Attribute("Net_Income_to_Total_Assets"));
        attributes.add(new Attribute("Gross_Profit_to_Sales"));
        attributes.add(new Attribute("Equity_to_Liability"));

        // Define the target class (Bankrupt? Yes/No)
        ArrayList<String> classValues = new ArrayList<>();
        classValues.add("NotBankrupt");
        classValues.add("Bankrupt");
        attributes.add(new Attribute("Class", classValues));

        // Create dataset
        Instances data = new Instances("BankruptcyDataset", attributes, dataList.size());
        data.setClassIndex(attributes.size() - 1);

        // Populate dataset
        for (FinancialData fd : dataList) {
            double[] values = new double[attributes.size()];
            values[0] = fd.getRoaC();
            values[1] = fd.getRoaA();
            values[2] = fd.getRoaB();
            values[3] = fd.getOperatingMargin();
            values[4] = fd.getProfitRate();
            values[5] = fd.getDebtRatio();
            values[6] = fd.getNetWorthToAssets();
            values[7] = fd.getLiabilitiesToEquity();
            values[8] = fd.getCashFlowToSales();
            values[9] = fd.getNetIncomeToTotalAssets();
            values[10] = fd.getGrossProfitToSales();
            values[11] = fd.getEquityToLiability();
            values[12] = fd.isBankrupt() ? 1.0 : 0.0;

            data.add(new DenseInstance(1.0, values));
        }

        // Normalize dataset to bring all features to the same scale
        Normalize normalize = new Normalize();
        normalize.setInputFormat(data);
        return Filter.useFilter(data, normalize);
    }

    // Handle class imbalance using random undersampling
    private Instances handleImbalance(Instances data) throws Exception {
        int bankruptCount = 0;
        int notBankruptCount = 0;
        for (int i = 0; i < data.numInstances(); i++) {
            if (data.instance(i).classValue() == 1.0) {
                bankruptCount++;
            } else {
                notBankruptCount++;
            }
        }

        // Use undersampling to balance the data
        int minCount = Math.min(bankruptCount, notBankruptCount);
        Instances balancedData = new Instances(data);

        // Create the balanced data by randomly removing excess instances
        Instances finalData = new Instances(balancedData, 0);
        for (int i = 0; i < data.numInstances(); i++) {
            Instance instance = data.instance(i);
            if (finalData.size() < minCount) {
                finalData.add(instance);
            }
        }

        return finalData;
    }

    // Train the model with cross-validation
    public void train(Instances trainingData) throws Exception {
        // Handle class imbalance using alternate methods if necessary
        Instances balancedData = handleImbalance(trainingData);

        // Build the classifier
        model.buildClassifier(balancedData);  // Explicitly train the model

        // Perform cross-validation to improve accuracy
        int folds = 10;
        Evaluation evaluation = new Evaluation(balancedData);
        evaluation.crossValidateModel(model, balancedData, folds, new java.util.Random(1));

        System.out.println("Cross-validation results:");
        System.out.println(evaluation.toSummaryString());
        System.out.println("Model trained successfully!");
    }

    // Feature selection using Information Gain
    private Instances selectFeatures(Instances data) throws Exception {
        // Use InfoGain attribute selection
        InfoGainAttributeEval evaluator = new InfoGainAttributeEval();
        Ranker search = new Ranker();
        AttributeSelection selector = new AttributeSelection();
        selector.setEvaluator(evaluator);
        selector.setSearch(search);
        selector.SelectAttributes(data);

        // Get the selected features
        int[] selectedAttributes = selector.selectedAttributes();
        ArrayList<Attribute> selectedList = new ArrayList<>();
        for (int i = 0; i < selectedAttributes.length; i++) {
            selectedList.add(data.attribute(selectedAttributes[i]));
        }

        // Create a new Instances object with the selected features
        Instances selectedData = new Instances(data);
        selectedData.delete();
        for (int i = 0; i < data.numInstances(); i++) {
            Instance instance = data.instance(i);
            selectedData.add(new DenseInstance(1.0, instance.toDoubleArray()));
        }
        return selectedData;
    }

    // Predict probability for a single instance
    public double predictProbability(Instance instance) throws Exception {
        // Ensure the model is trained
        if (model == null) {
            throw new IllegalStateException("Model has not been trained yet.");
        }

        // Get the probability distribution for each class
        double[] distribution = model.distributionForInstance(instance);

        // Return the probability of the "Bankrupt" class (class 1.0)
        return distribution[1]; // Assuming '1.0' is the class for "Bankrupt"
    }

    // Predict and get probabilities for a list of instances
    public List<Double> predictAllProbabilities(List<FinancialData> dataList) throws Exception {
        Instances processedData = preprocessData(dataList);
        Instances selectedData = selectFeatures(processedData); // Select only the important features
        List<Double> probabilities = new ArrayList<>();

        for (int i = 0; i < selectedData.numInstances(); i++) {
            Instance instance = selectedData.instance(i);
            double[] distribution = model.distributionForInstance(instance);
            probabilities.add(distribution[1]); // Assuming the second index is the probability for "Bankrupt"
        }

        return probabilities;
    }

    // Predict for a list of financial data
    public List<Boolean> predictAll(List<FinancialData> dataList) throws Exception {
        Instances processedData = preprocessData(dataList);
        Instances selectedData = selectFeatures(processedData); // Select only the important features
        List<Boolean> predictions = new ArrayList<>();

        for (int i = 0; i < selectedData.numInstances(); i++) {
            predictions.add(predict(selectedData.instance(i)));  // Predict for each instance
        }

        return predictions;
    }

    // Predict a single instance
    public boolean predict(Instance instance) throws Exception {
        // Ensure the model is trained
        if (model == null) {
            throw new IllegalStateException("Model has not been trained yet.");
        }

        double prediction = model.classifyInstance(instance);  // Classify the instance
        return prediction == 1.0;  // Assuming '1.0' is for "Bankrupt"
    }

    // Evaluate the model
    public void evaluate(Instances testingData) throws Exception {
        // Handle class imbalance in the test data as well
        Instances balancedTestingData = handleImbalance(testingData);

        double correct = 0;
        for (int i = 0; i < balancedTestingData.numInstances(); i++) {
            Instance instance = balancedTestingData.instance(i);
            double probability = predictProbability(instance);
            boolean predicted = probability >= 0.5;  // Assume threshold of 0.5 for classifying as Bankrupt
            boolean actual = instance.classValue() == 1.0; // Assuming 1.0 means "Bankrupt"

            if (predicted == actual) {
                correct++;
            }
        }

        double accuracy = (correct / balancedTestingData.numInstances()) * 100;
        System.out.println("Model evaluation complete. Accuracy: " + accuracy + "%");
    }
}
