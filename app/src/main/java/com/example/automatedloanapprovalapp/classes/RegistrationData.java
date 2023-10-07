package com.example.automatedloanapprovalapp.classes;

import java.util.List;

public class RegistrationData {
    private String fullName;
    private String dateOfBirth;
    private String gender;
    private String email;
    private String nationality;
    private String address;
    private String phoneNumber;

    //employment info
    private String jobTitle;
    private double monthlyIncome;
    private int numberOfDependents;
    private String businessName;
    private String businessType;
    private double businessRevenue;

    private String businessLocation;
    private String nationalID;
    private byte[] nationalIDImage; // This field stores the binary data of the national ID image
    private List<String> additionalDocuments; // You can store URLs or references to additional documents

    private Boolean passwordResetRequired;

    // Constructors

    public RegistrationData() {
        // Default constructor required for Firestore
    }

    public RegistrationData(String fullName, String dateOfBirth, String gender, String nationality,
                            String address, String phoneNumber, String jobTitle,
                            double monthlyIncome, int numberOfDependents, String businessName,
                            String businessType, double businessRevenue, String nationalID,
                            byte[] nationalIDImage, List<String> additionalDocuments,String email
            ,String businessLocation, Boolean passwordResetRequired) {
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.nationality = nationality;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.jobTitle = jobTitle;
        this.monthlyIncome = monthlyIncome;
        this.numberOfDependents = numberOfDependents;
        this.businessName = businessName;
        this.businessType = businessType;
        this.businessRevenue = businessRevenue;
        this.nationalID = nationalID;
        this.nationalIDImage = nationalIDImage;
        this.additionalDocuments = additionalDocuments;
        this.email = email;
        this.businessLocation = businessLocation;
        this.passwordResetRequired = passwordResetRequired;
    }

    // Getter and Setter methods for each field

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    // Add getter and setter methods for other fields...

    public List<String> getAdditionalDocuments() {
        return additionalDocuments;
    }

    public void setAdditionalDocuments(List<String> additionalDocuments) {
        this.additionalDocuments = additionalDocuments;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public double getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(double monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public int getNumberOfDependents() {
        return numberOfDependents;
    }

    public void setNumberOfDependents(int numberOfDependents) {
        this.numberOfDependents = numberOfDependents;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getBusinessLocation() {
        return businessLocation;
    }

    public void setBusinessLocation(String businessLocation) {
        this.businessLocation = businessLocation;
    }

    public double getBusinessRevenue() {
        return businessRevenue;
    }

    public void setBusinessRevenue(double businessRevenue) {
        this.businessRevenue = businessRevenue;
    }

    public String getNationalID() {
        return nationalID;
    }

    public void setNationalID(String nationalID) {
        this.nationalID = nationalID;
    }

    public byte[] getNationalIDImage() {
        return nationalIDImage;
    }

    public void setNationalIDImage(byte[] nationalIDImage) {
        this.nationalIDImage = nationalIDImage;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getPasswordResetRequired() {
        return passwordResetRequired;
    }

    public void setPasswordResetRequired(Boolean passwordResetRequired) {
        this.passwordResetRequired = passwordResetRequired;
    }
}
