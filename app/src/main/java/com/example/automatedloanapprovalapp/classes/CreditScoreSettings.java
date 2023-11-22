package com.example.automatedloanapprovalapp.classes;

public class CreditScoreSettings {
    public int ageWeight;
    public int monthlyIncomeWeight;
    public int businessRevenueWeight;
    public int dependentsWeight;
    public int addressStabilityWeight;
    public int jobStabilityWeight;
    public int nationalityWeight;
    public int repaymentHistoryWeight;
    public int minAge;
    public int minMonthlyIncome;
    public int minBusinessRevenue;
    public int poorScoreMaxLoan;
    public int fairScoreMaxLoan;
    public int goodScoreMaxLoan;
    public int veryGoodScoreMaxLoan;
    public int excellentScoreMaxLoan;



    // Add constructors, getters, setters, or any other methods as needed


    public CreditScoreSettings() {
    }

    public CreditScoreSettings(int ageWeight, int monthlyIncomeWeight, int businessRevenueWeight, int dependentsWeight, int addressStabilityWeight, int jobStabilityWeight, int nationalityWeight, int repaymentHistoryWeight, int minAge, int minMonthlyIncome, int minBusinessRevenue, int poorScoreMaxLoan, int fairScoreMaxLoan, int goodScoreMaxLoan, int veryGoodScoreMaxLoan, int excellentScoreMaxLoan) {
        this.ageWeight = ageWeight;
        this.monthlyIncomeWeight = monthlyIncomeWeight;
        this.businessRevenueWeight = businessRevenueWeight;
        this.dependentsWeight = dependentsWeight;
        this.addressStabilityWeight = addressStabilityWeight;
        this.jobStabilityWeight = jobStabilityWeight;
        this.nationalityWeight = nationalityWeight;
        this.repaymentHistoryWeight = repaymentHistoryWeight;
        this.minAge = minAge;
        this.minMonthlyIncome = minMonthlyIncome;
        this.minBusinessRevenue = minBusinessRevenue;
        this.poorScoreMaxLoan = poorScoreMaxLoan;
        this.fairScoreMaxLoan = fairScoreMaxLoan;
        this.goodScoreMaxLoan = goodScoreMaxLoan;
        this.veryGoodScoreMaxLoan = veryGoodScoreMaxLoan;
        this.excellentScoreMaxLoan = excellentScoreMaxLoan;
    }

    public int getAgeWeight() {
        return ageWeight;
    }

    public void setAgeWeight(int ageWeight) {
        this.ageWeight = ageWeight;
    }

    public int getMonthlyIncomeWeight() {
        return monthlyIncomeWeight;
    }

    public void setMonthlyIncomeWeight(int monthlyIncomeWeight) {
        this.monthlyIncomeWeight = monthlyIncomeWeight;
    }

    public int getBusinessRevenueWeight() {
        return businessRevenueWeight;
    }

    public void setBusinessRevenueWeight(int businessRevenueWeight) {
        this.businessRevenueWeight = businessRevenueWeight;
    }

    public int getDependentsWeight() {
        return dependentsWeight;
    }

    public void setDependentsWeight(int dependentsWeight) {
        this.dependentsWeight = dependentsWeight;
    }

    public int getAddressStabilityWeight() {
        return addressStabilityWeight;
    }

    public void setAddressStabilityWeight(int addressStabilityWeight) {
        this.addressStabilityWeight = addressStabilityWeight;
    }

    public int getJobStabilityWeight() {
        return jobStabilityWeight;
    }

    public void setJobStabilityWeight(int jobStabilityWeight) {
        this.jobStabilityWeight = jobStabilityWeight;
    }

    public int getNationalityWeight() {
        return nationalityWeight;
    }

    public void setNationalityWeight(int nationalityWeight) {
        this.nationalityWeight = nationalityWeight;
    }

    public int getRepaymentHistoryWeight() {
        return repaymentHistoryWeight;
    }

    public void setRepaymentHistoryWeight(int repaymentHistoryWeight) {
        this.repaymentHistoryWeight = repaymentHistoryWeight;
    }

    public int getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public int getMinMonthlyIncome() {
        return minMonthlyIncome;
    }

    public void setMinMonthlyIncome(int minMonthlyIncome) {
        this.minMonthlyIncome = minMonthlyIncome;
    }

    public int getMinBusinessRevenue() {
        return minBusinessRevenue;
    }

    public void setMinBusinessRevenue(int minBusinessRevenue) {
        this.minBusinessRevenue = minBusinessRevenue;
    }

    public int getPoorScoreMaxLoan() {
        return poorScoreMaxLoan;
    }

    public void setPoorScoreMaxLoan(int poorScoreMaxLoan) {
        this.poorScoreMaxLoan = poorScoreMaxLoan;
    }

    public int getFairScoreMaxLoan() {
        return fairScoreMaxLoan;
    }

    public void setFairScoreMaxLoan(int fairScoreMaxLoan) {
        this.fairScoreMaxLoan = fairScoreMaxLoan;
    }

    public int getGoodScoreMaxLoan() {
        return goodScoreMaxLoan;
    }

    public void setGoodScoreMaxLoan(int goodScoreMaxLoan) {
        this.goodScoreMaxLoan = goodScoreMaxLoan;
    }

    public int getVeryGoodScoreMaxLoan() {
        return veryGoodScoreMaxLoan;
    }

    public void setVeryGoodScoreMaxLoan(int veryGoodScoreMaxLoan) {
        this.veryGoodScoreMaxLoan = veryGoodScoreMaxLoan;
    }

    public int getExcellentScoreMaxLoan() {
        return excellentScoreMaxLoan;
    }

    public void setExcellentScoreMaxLoan(int excellentScoreMaxLoan) {
        this.excellentScoreMaxLoan = excellentScoreMaxLoan;
    }



}

