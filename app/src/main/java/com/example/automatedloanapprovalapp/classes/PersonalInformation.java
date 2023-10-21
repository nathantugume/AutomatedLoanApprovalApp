package com.example.automatedloanapprovalapp.classes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PersonalInformation {
    private String documentId;
    private String additionalDocuments;
    private String address;
    private String businessLocation;
    private String businessName;
    private double businessRevenue;
    private String businessType;
    private String dateOfBirth;
    private String email;
    private String fullName;
    private String gender;
    private String jobTitle;
    private double monthlyIncome;
    private String nationalID;
    private String nationalIDImage;
    private String nationality;
    private int numberOfDependents;
    private boolean passwordResetRequired;
    private String phoneNumber;
    private int age;


    // Constructors, getters, and setters


    public PersonalInformation() {
    }

    public PersonalInformation(String documentId, String additionalDocuments, String address, String businessLocation,
                               String businessName, double businessRevenue, String businessType, String dateOfBirth,
                               String email, String fullName, String gender, String jobTitle, double monthlyIncome,
                               String nationalID, String nationalIDImage, String nationality, int numberOfDependents,
                               boolean passwordResetRequired, String phoneNumber) {
        this.documentId = documentId;
        this.additionalDocuments = additionalDocuments;
        this.address = address;
        this.businessLocation = businessLocation;
        this.businessName = businessName;
        this.businessRevenue = businessRevenue;
        this.businessType = businessType;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.fullName = fullName;
        this.gender = gender;
        this.jobTitle = jobTitle;
        this.monthlyIncome = monthlyIncome;
        this.nationalID = nationalID;
        this.nationalIDImage = nationalIDImage;
        this.nationality = nationality;
        this.numberOfDependents = numberOfDependents;
        this.passwordResetRequired = passwordResetRequired;
        this.phoneNumber = phoneNumber;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getAdditionalDocuments() {
        return additionalDocuments;
    }

    public void setAdditionalDocuments(String additionalDocuments) {
        this.additionalDocuments = additionalDocuments;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBusinessLocation() {
        return businessLocation;
    }

    public void setBusinessLocation(String businessLocation) {
        this.businessLocation = businessLocation;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public double getBusinessRevenue() {
        return businessRevenue;
    }

    public void setBusinessRevenue(double businessRevenue) {
        this.businessRevenue = businessRevenue;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public String getNationalID() {
        return nationalID;
    }

    public void setNationalID(String nationalID) {
        this.nationalID = nationalID;
    }

    public String getNationalIDImage() {
        return nationalIDImage;
    }

    public void setNationalIDImage(String nationalIDImage) {
        this.nationalIDImage = nationalIDImage;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public int getNumberOfDependents() {
        return numberOfDependents;
    }

    public void setNumberOfDependents(int numberOfDependents) {
        this.numberOfDependents = numberOfDependents;
    }

    public boolean isPasswordResetRequired() {
        return passwordResetRequired;
    }

    public void setPasswordResetRequired(boolean passwordResetRequired) {
        this.passwordResetRequired = passwordResetRequired;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getAge() {
        calculateAge();
        return age;
    }
    public void calculateAge() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date birthDate = sdf.parse(getDateOfBirth());
            Calendar dob = Calendar.getInstance();
            assert birthDate != null;
            dob.setTime(birthDate);
            Calendar now = Calendar.getInstance();
            int age = now.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
            if (now.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
                age--;
            }
            setAge(age);
        } catch (ParseException e) {
            e.printStackTrace();
            // Handle the parse exception according to your application's needs
        }
    }



    public void setAge(int age) {
        this.age = age;
    }
}
