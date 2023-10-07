package com.example.automatedloanapprovalapp.classes;

public class Transaction {
    private String userId;
    private String loanType;
    private double requestedAmount;
    private double paybackAmount;
    private String dateRequested;
    private String status;
    private String userName;

    private String id;


    public Transaction() {
        // Default constructor required for Firestore
    }

    public Transaction(String userId, String loanType, double requestedAmount, double paybackAmount,
                       String dateRequested, String status) {
        this.userId = userId;
        this.loanType = loanType;
        this.requestedAmount = requestedAmount;
        this.paybackAmount = paybackAmount;
        this.dateRequested = dateRequested;
        this.status = status;
    }

    public Transaction(String id,String userId, String loanType, double requestedAmount, double paybackAmount, String dateRequested, String status, String userName) {
        this.userId = userId;
        this.loanType = loanType;
        this.requestedAmount = requestedAmount;
        this.paybackAmount = paybackAmount;
        this.dateRequested = dateRequested;
        this.status = status;
        this.userName = userName;
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public double getRequestedAmount() {
        return requestedAmount;
    }

    public void setRequestedAmount(double requestedAmount) {
        this.requestedAmount = requestedAmount;
    }

    public double getPaybackAmount() {
        return paybackAmount;
    }

    public void setPaybackAmount(double paybackAmount) {
        this.paybackAmount = paybackAmount;
    }

    public String getDateRequested() {
        return dateRequested;
    }

    public void setDateRequested(String dateRequested) {
        this.dateRequested = dateRequested;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void disburseFunds() {
        if ("approved".equals(status)) {
            // Implement disbursement logic here
            // For example, you can call an API to transfer funds, update database records, etc.
            // This method will be called when the transaction is approved
            // You can access the transaction details using the attributes of this Transaction object
        } else {
            // Handle error: Attempting to disburse funds for a non-approved transaction
            throw new IllegalStateException("Cannot disburse funds for a non-approved transaction");
        }
    }
}
