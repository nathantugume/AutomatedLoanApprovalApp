package com.example.automatedloanapprovalapp.classes;

public class RepaymentTransaction {
    private String customerName;
    private int paidPercentage;
    private int loanAmount;
    private int paidAmount;
    private String status;

    // Constructor, getters, and setters


    public RepaymentTransaction() {
    }

    public RepaymentTransaction(String customerName, int paidPercentage, int loanAmount, int paidAmount, String status) {
        this.customerName = customerName;
        this.paidPercentage = paidPercentage;
        this.loanAmount = loanAmount;
        this.paidAmount = paidAmount;
        this.status = status;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getPaidPercentage() {
        return paidPercentage;
    }

    public void setPaidPercentage(int paidPercentage) {
        this.paidPercentage = paidPercentage;
    }

    public int getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(int loanAmount) {
        this.loanAmount = loanAmount;
    }

    public int getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(int paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

