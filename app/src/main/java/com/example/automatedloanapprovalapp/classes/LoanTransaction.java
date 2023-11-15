package com.example.automatedloanapprovalapp.classes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoanTransaction {
    private String approvedDate;
    private double requestedAmount;
    private double repaidAmount;
    private String status;

    public LoanTransaction(String approvedDate, double requestedAmount, double repaidAmount, String status) {
        this.approvedDate = approvedDate;
        this.requestedAmount = requestedAmount;
        this.repaidAmount = repaidAmount;
        this.status = status;
    }

    public String getApprovedDate() {
        return approvedDate;
    }

    public double getRequestedAmount() {
        return requestedAmount;
    }

    public double getRepaidAmount() {
        return repaidAmount;
    }

    public String getStatus() {
        return status;
    }

    public int getMonthNumber() throws ParseException {
        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(approvedDate);
        return Integer.parseInt(new SimpleDateFormat("MM").format(date));
    }
}

