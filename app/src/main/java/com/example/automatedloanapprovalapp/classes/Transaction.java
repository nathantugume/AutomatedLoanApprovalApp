package com.example.automatedloanapprovalapp.classes;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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

    public void disburseFunds(Context context,String phone, int amount, String docId) {
       try {
           Toast.makeText(context, "Money has been transferred to your account successfully", Toast.LENGTH_SHORT).show();
            FirestoreCRUD firestoreCRUD = new FirestoreCRUD();
           // Get the current date and time
           Date currentDate = Calendar.getInstance().getTime();

           // Format the current date as a string in the desired format (e.g., "yyyy-MM-dd HH:mm:ss")
           SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
           String formattedDate = dateFormat.format(currentDate);
           Map<String, Object> updateData = new HashMap<>();
           updateData.put("status", "disbursed");
           updateData.put("disbursedDate",formattedDate);
           firestoreCRUD.updateDocumentFields("transaction", docId, updateData, new OnCompleteListener<Void>() {
               @Override
               public void onComplete(@NonNull Task<Void> task) {
                   Toast.makeText(context, "Account credited successfully !!", Toast.LENGTH_SHORT).show();
               }
           });

       }catch (Exception e){
           Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
       }
    }
}
