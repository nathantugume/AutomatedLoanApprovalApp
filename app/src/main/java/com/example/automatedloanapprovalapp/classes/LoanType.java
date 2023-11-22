package com.example.automatedloanapprovalapp.classes;

public class LoanType {
    private String amount;
    private String id;
    private String type;
    private double interestRate;
    private String duration;
    private String status;
    public LoanType() {
        // Default constructor required for Firebase
    }

    public LoanType(String id, String type, double interestRate, String duration, String status) {
        this.id = id;
        this.type = type;
        this.interestRate = interestRate;
        this.duration = duration;
        this.status = status;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public String getDuration() {

        return duration;
    }
    public int getDurationAsInt() {
        try {
            // Attempt to parse the duration string to an integer
            return Integer.parseInt(duration);
        } catch (NumberFormatException e) {
            // Handle the case where the parsing fails
            e.printStackTrace(); // Log the exception or handle it as needed
            return 0; // Default value or another appropriate value
        }
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
