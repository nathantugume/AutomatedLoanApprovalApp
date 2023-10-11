package com.example.automatedloanapprovalapp.classes;

public class RepaymentItem {
    private String documentId;
    private int paybackAmount;
    private int repaidAmount;
    private String repaymentDate;
    private String status;
    private String transactionId;

    public RepaymentItem() {
    }

    public RepaymentItem(String documentId, int paybackAmount, int repaidAmount, String repaymentDate, String status, String transactionId) {
        this.documentId = documentId;
        this.paybackAmount = paybackAmount;
        this.repaidAmount = repaidAmount;
        this.repaymentDate = repaymentDate;
        this.status = status;
        this.transactionId = transactionId;
    }

    // Getters for each field
    public String getDocumentId() {
        return documentId;
    }

    public int getPaybackAmount() {
        return paybackAmount;
    }

    public int getRepaidAmount() {
        return repaidAmount;
    }

    public String getRepaymentDate() {
        return repaymentDate;
    }

    public String getStatus() {
        return status;
    }

    public String getTransactionId() {
        return transactionId;
    }
}
