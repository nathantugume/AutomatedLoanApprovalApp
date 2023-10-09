package com.example.automatedloanapprovalapp.classes;

public class ApplicationStatus {
    private String status;
    private String date;

    public ApplicationStatus(String status, String date) {
        this.status = status;
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public String getDate() {
        return date;
    }
}
