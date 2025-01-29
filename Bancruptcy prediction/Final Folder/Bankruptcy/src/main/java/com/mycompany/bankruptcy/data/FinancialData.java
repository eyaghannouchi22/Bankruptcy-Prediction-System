package com.mycompany.bankruptcy.data;

public class FinancialData {
    private double roaC;            // ROA(C) before interest and depreciation
    private double roaA;            // ROA(A) before interest and after tax
    private double roaB;            // ROA(B) before interest and depreciation after tax
    private double operatingMargin; // Operating Gross Margin
    private double profitRate;      // Operating Profit Rate
    private double debtRatio;       // Debt ratio %
    private double netWorthToAssets;// Net worth/Assets
    private double liabilitiesToEquity; // Liability to Equity
    private double equityToLiability;   // Equity to Liability
    private double cashFlowToSales; // Cash Flow to Sales
    private boolean isBankrupt;     // True if bankrupt, false otherwise
    private double netIncomeToAssets; // Missing field
    private double grossProfitToSales; // Missing field

    // Default constructor
    public FinancialData() {}

    // Updated constructor with 14 arguments
    public FinancialData(double roaC, double roaA, double roaB, double operatingMargin,
                         double profitRate, double debtRatio, double netWorthToAssets,
                         double liabilitiesToEquity, double equityToLiability,
                         double cashFlowToSales, double netIncomeToAssets, double grossProfitToSales,
                         boolean isBankrupt) {
        this.roaC = roaC;
        this.roaA = roaA;
        this.roaB = roaB;
        this.operatingMargin = operatingMargin;
        this.profitRate = profitRate;
        this.debtRatio = debtRatio;
        this.netWorthToAssets = netWorthToAssets;
        this.liabilitiesToEquity = liabilitiesToEquity;
        this.equityToLiability = equityToLiability;
        this.cashFlowToSales = cashFlowToSales;
        this.netIncomeToAssets = netIncomeToAssets;  // Added field
        this.grossProfitToSales = grossProfitToSales;  // Added field
        this.isBankrupt = isBankrupt;
    }

    // Getters and setters
    public double getRoaC() {
        return roaC;
    }

    public void setRoaC(double roaC) {
        this.roaC = roaC;
    }

    public double getRoaA() {
        return roaA;
    }

    public void setRoaA(double roaA) {
        this.roaA = roaA;
    }

    public double getRoaB() {
        return roaB;
    }

    public void setRoaB(double roaB) {
        this.roaB = roaB;
    }

    public double getOperatingMargin() {
        return operatingMargin;
    }

    public void setOperatingMargin(double operatingMargin) {
        this.operatingMargin = operatingMargin;
    }

    public double getProfitRate() {
        return profitRate;
    }

    public void setProfitRate(double profitRate) {
        this.profitRate = profitRate;
    }

    public double getDebtRatio() {
        return debtRatio;
    }

    public void setDebtRatio(double debtRatio) {
        this.debtRatio = debtRatio;
    }

    public double getNetWorthToAssets() {
        return netWorthToAssets;
    }

    public void setNetWorthToAssets(double netWorthToAssets) {
        this.netWorthToAssets = netWorthToAssets;
    }

    public double getLiabilitiesToEquity() {
        return liabilitiesToEquity;
    }

    public void setLiabilitiesToEquity(double liabilitiesToEquity) {
        this.liabilitiesToEquity = liabilitiesToEquity;
    }

    public double getEquityToLiability() {
        return equityToLiability;
    }

    public void setEquityToLiability(double equityToLiability) {
        this.equityToLiability = equityToLiability;
    }

    public double getCashFlowToSales() {
        return cashFlowToSales;
    }

    public void setCashFlowToSales(double cashFlowToSales) {
        this.cashFlowToSales = cashFlowToSales;
    }

    public boolean isBankrupt() {
        return isBankrupt;
    }

    public void setBankrupt(boolean bankrupt) {
        isBankrupt = bankrupt;
    }

    // Missing getters
    public double getNetIncomeToTotalAssets() {
        return netIncomeToAssets;
    }

    public double getGrossProfitToSales() {
        return grossProfitToSales;
    }

    @Override
    public String toString() {
        return "FinancialData{" +
                "roaC=" + roaC +
                ", roaA=" + roaA +
                ", roaB=" + roaB +
                ", operatingMargin=" + operatingMargin +
                ", profitRate=" + profitRate +
                ", debtRatio=" + debtRatio +
                ", netWorthToAssets=" + netWorthToAssets +
                ", liabilitiesToEquity=" + liabilitiesToEquity +
                ", equityToLiability=" + equityToLiability +
                ", cashFlowToSales=" + cashFlowToSales +
                ", isBankrupt=" + isBankrupt +
                ", netIncomeToAssets=" + netIncomeToAssets +  // Added field to toString
                ", grossProfitToSales=" + grossProfitToSales +  // Added field to toString
                '}';
    }
}
