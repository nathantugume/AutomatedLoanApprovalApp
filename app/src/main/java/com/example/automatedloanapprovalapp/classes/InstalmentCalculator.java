package com.example.automatedloanapprovalapp.classes;

import android.util.Log;

public class InstalmentCalculator {

    public static double calculateInstallment(double loanAmount, double annualInterestRate, int loanDurationMonths) {
        // Convert annual interest rate to monthly interest rate
        double monthlyInterestRate = annualInterestRate / 12 / 100;

        Log.d("monthly interest_rate",String.valueOf(monthlyInterestRate));

        Log.d("Loan Amount", String.valueOf(loanAmount));
        Log.d("Annual Interest Rate", String.valueOf(annualInterestRate));
        Log.d("Monthly Interest Rate", String.valueOf(monthlyInterestRate));
        Log.d("Loan Duration (Months)", String.valueOf(loanDurationMonths));

        // Calculate monthly payment using the loan amortization formula
        double monthlyPayment = (monthlyInterestRate * loanAmount) / (1 - Math.pow(1 + monthlyInterestRate, -loanDurationMonths));

        return monthlyPayment;

    }
}