package com.example.automatedloanapprovalapp.classes;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.concurrent.CompletableFuture;

public class CreditScoreCalculator {
    // Factors and their respective weights for credit score calculation

    private final FirestoreCRUD firestoreCRUD = new FirestoreCRUD();

    private static int AGE_WEIGHT;
    private static int MONTHLY_INCOME_WEIGHT ;
    private static int BUSINESS_REVENUE_WEIGHT;
    private static int DEPENDENTS_WEIGHT; // Negative weight because more dependents decrease the score
    private static int ADDRESS_STABILITY_WEIGHT;
    private static int JOB_STABILITY_WEIGHT;
    private static int NATIONALITY_WEIGHT;
    private static int REPAYMENT_HISTORY_WEIGHT;

    // Constants for threshold values
    private static int MIN_AGE;
    private static int MIN_MONTHLY_INCOME;
    private static int MIN_BUSINESS_REVENUE;

    private static int POOR_SCORE_MAX_LOAN;
    private static int FAIR_SCORE_MAX_LOAN;
    private static int GOOD_SCORE_MAX_LOAN;
    private static int VERY_GOOD_SCORE_MAX_LOAN;
    private static int EXCELLENT_SCORE_MAX_LOAN;

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

        // Ensure credit score is within a valid range (0 to 150, for example)
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
//
//    public void setFactors(){
//
//        firestoreCRUD.getAllDocuments("creditScoreSettings", task -> {
//
//
//            if (task.isSuccessful()){
//                for (QueryDocumentSnapshot documentSnapshot : task.getResult()  ) {
//
//                    CreditScoreSettings creditScoreSettings = documentSnapshot.toObject(CreditScoreSettings.class);
//
//

//
//                }
//            }else {
//                Log.d("CreditScoreCalculator",task.getException().getMessage());
//            }
//
//        });
//
//    }

    public CompletableFuture<Void> setFactors() {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();

        firestoreCRUD.getAllDocuments("creditScoreSettings", task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                    CreditScoreSettings creditScoreSettings = documentSnapshot.toObject(CreditScoreSettings.class);
                    AGE_WEIGHT = creditScoreSettings.getAgeWeight();
                    MONTHLY_INCOME_WEIGHT = creditScoreSettings.getMonthlyIncomeWeight();
                    BUSINESS_REVENUE_WEIGHT = creditScoreSettings.getBusinessRevenueWeight();
                    DEPENDENTS_WEIGHT = creditScoreSettings.getDependentsWeight(); // Negative weight because more dependents decrease the score
                    ADDRESS_STABILITY_WEIGHT = creditScoreSettings.getAddressStabilityWeight();
                    JOB_STABILITY_WEIGHT = creditScoreSettings.getJobStabilityWeight();
                    NATIONALITY_WEIGHT = creditScoreSettings.getNationalityWeight();
                    REPAYMENT_HISTORY_WEIGHT = creditScoreSettings.getRepaymentHistoryWeight();

                    // Constants for threshold values
                    MIN_AGE = creditScoreSettings.getMinAge();
                    MIN_MONTHLY_INCOME = creditScoreSettings.getMinMonthlyIncome();
                    MIN_BUSINESS_REVENUE = creditScoreSettings.getMinBusinessRevenue();

                    POOR_SCORE_MAX_LOAN = creditScoreSettings.getPoorScoreMaxLoan();
                    FAIR_SCORE_MAX_LOAN = creditScoreSettings.getFairScoreMaxLoan();
                    GOOD_SCORE_MAX_LOAN = creditScoreSettings.getGoodScoreMaxLoan();
                    VERY_GOOD_SCORE_MAX_LOAN = creditScoreSettings.getVeryGoodScoreMaxLoan();
                    EXCELLENT_SCORE_MAX_LOAN = creditScoreSettings.getExcellentScoreMaxLoan();

                }
                completableFuture.complete(null); // Complete the future when factors are loaded
            } else {
                completableFuture.completeExceptionally(task.getException()); // Complete exceptionally if there's an error
            }
        });

        return completableFuture;
    }


}
