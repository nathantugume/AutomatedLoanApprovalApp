package com.example.automatedloanapprovalapp.classes;

public class CreditScoreCalculator {
    // Factors and their respective weights for credit score calculation
    private static final int AGE_WEIGHT = 20;
    private static final int MONTHLY_INCOME_WEIGHT = 25;
    private static final int BUSINESS_REVENUE_WEIGHT = 15;
    private static final int DEPENDENTS_WEIGHT = -10; // Negative weight because more dependents decrease the score
    private static final int ADDRESS_STABILITY_WEIGHT = 10;
    private static final int JOB_STABILITY_WEIGHT = 10;
    private static final int NATIONALITY_WEIGHT = 5;
    private static final int REPAYMENT_HISTORY_WEIGHT = 30;

    // Constants for threshold values
    private static final int MIN_AGE = 18;
    private static final int MIN_MONTHLY_INCOME = 100000;
    private static final int MIN_BUSINESS_REVENUE = 1000000;

    private static final int POOR_SCORE_MAX_LOAN = 60000;
    private static final int FAIR_SCORE_MAX_LOAN = 100000;
    private static final int GOOD_SCORE_MAX_LOAN = 500000;
    private static final int VERY_GOOD_SCORE_MAX_LOAN = 1200000;
    private static final int EXCELLENT_SCORE_MAX_LOAN = 5000000;

    private int calculatedCreditAmount;
    private int creditScore;
    private double paybackAmount;

    // Calculate credit score based on provided data
    public int calculateCreditScore(PersonalInformation personalInfo) {
        int creditScore = 0;

        // Age factor
        if (personalInfo.getAge() >= MIN_AGE) {
            creditScore += AGE_WEIGHT;
        }

        // Monthly income factor
        if (personalInfo.getMonthlyIncome() >= MIN_MONTHLY_INCOME) {
            creditScore += MONTHLY_INCOME_WEIGHT;
        }

        // Business revenue factor
        if (personalInfo.getBusinessRevenue() >= MIN_BUSINESS_REVENUE) {
            creditScore += BUSINESS_REVENUE_WEIGHT;
        }

        // Dependents factor
        creditScore += personalInfo.getNumberOfDependents() * DEPENDENTS_WEIGHT;

        // Address stability factor
        creditScore += ADDRESS_STABILITY_WEIGHT;

        // Job stability factor
        creditScore += JOB_STABILITY_WEIGHT;

        // Nationality factor
        creditScore += NATIONALITY_WEIGHT;

        // Repayment history factor (assuming this information is available)
        creditScore += REPAYMENT_HISTORY_WEIGHT;

        // Ensure credit score is within a valid range (0 to 1000, for example)
        creditScore = Math.max(0, Math.min(150, creditScore));

        return creditScore;
    }

    // Method to calculate loan amount based on credit score
    public int calculateLoanAmount(int creditScore) {
        int loanAmount = 0;

        // Determine loan amount based on credit score range
        if (creditScore >= 0 && creditScore < 40) {
            loanAmount = POOR_SCORE_MAX_LOAN;
        } else if (creditScore < 50) {
            loanAmount = FAIR_SCORE_MAX_LOAN;
        } else if (creditScore < 70) {
            loanAmount = GOOD_SCORE_MAX_LOAN;
        } else if (creditScore < 90) {
            loanAmount = VERY_GOOD_SCORE_MAX_LOAN;
        } else {
            loanAmount = EXCELLENT_SCORE_MAX_LOAN;
        }

        return loanAmount;
    }

    public int getCalculatedCreditAmount() {
        return calculatedCreditAmount;
    }

    public void setCalculatedCreditAmount(int calculatedCreditAmount) {
        this.calculatedCreditAmount = calculatedCreditAmount;
    }

    public int getCreditScore() {
        return creditScore;
    }

    public void setCreditScore(int creditScore) {
        this.creditScore = creditScore;
    }

    // Method to calculate payback amount
    public double calculatePaybackAmount(double loanAmount, double interestRate, double durationInMonths) {
        // Convert interestRate from percentage to decimal
        double interestRateDecimal = interestRate * 0.01;

        // Convert duration from months to years
        double durationInYears = durationInMonths / 12.0;

        // Formula to calculate payback amount

        return loanAmount + (loanAmount * interestRateDecimal * durationInYears);
    }



}
