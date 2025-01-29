package com.mycompany.bankruptcy.data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataLoader {
    private static final String FILE_PATH = "filtered_data.csv"; // Same level as pom.xml

    // Method to load data from CSV and return a list of FinancialData objects
    public List<FinancialData> loadData() {
        List<FinancialData> dataList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                // Skip the header row
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                // Parse each row
                String[] columns = line.split(",");
                if (columns.length != 14) { // Ensure correct number of columns
                    System.err.println("Invalid row: " + line);
                    continue;
                }

                // Map columns to FinancialData
                try {
                    FinancialData data = parseRow(columns);
                    dataList.add(data);
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing row: " + line);
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + FILE_PATH);
            e.printStackTrace();
        }

        return dataList;
    }

    // Method to parse a single row into a FinancialData object
    private FinancialData parseRow(String[] columns) {
        try {
            // Remove quotes and trim the values before parsing
            int isBankrupt = Integer.parseInt(columns[0].replace("\"", "").trim());
            double roaC = Double.parseDouble(columns[1].replace("\"", "").trim());
            double roaA = Double.parseDouble(columns[2].replace("\"", "").trim());
            double roaB = Double.parseDouble(columns[3].replace("\"", "").trim());
            double operatingMargin = Double.parseDouble(columns[4].replace("\"", "").trim());
            double profitRate = Double.parseDouble(columns[5].replace("\"", "").trim());
            double debtRatio = Double.parseDouble(columns[6].replace("\"", "").trim());
            double netWorthToAssets = Double.parseDouble(columns[7].replace("\"", "").trim());
            double liabilitiesToEquity = Double.parseDouble(columns[8].replace("\"", "").trim());
            double cashFlowToSales = Double.parseDouble(columns[9].replace("\"", "").trim());
            double netIncomeToAssets = Double.parseDouble(columns[10].replace("\"", "").trim());
            double grossProfitToSales = Double.parseDouble(columns[11].replace("\"", "").trim());
            double liabilityToEquity = Double.parseDouble(columns[12].replace("\"", "").trim());
            double equityToLiability = Double.parseDouble(columns[13].replace("\"", "").trim());

            // Create and return the FinancialData object
            return new FinancialData(roaC, roaA, roaB, operatingMargin, profitRate, debtRatio,
                    netWorthToAssets, liabilitiesToEquity, equityToLiability, cashFlowToSales,
                    netIncomeToAssets, grossProfitToSales, isBankrupt == 1);

        } catch (NumberFormatException e) {
            // Log error and rethrow for debugging purposes
            System.err.println("Error parsing row: " + String.join(",", columns));
            throw e;
        }
    }
}
