package com.example.automatedloanapprovalapp.classes;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;

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
    private String approvedDate;
    private String repaymentDate;
    private String disbursedDate;
    private String approvedBy;
    private double repaidAmount;

    private FirestoreCRUD firestoreCRUD = new FirestoreCRUD();

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

    public Transaction(String id, String userId, String loanType, double requestedAmount, double paybackAmount, String dateRequested, String status, String userName) {
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

    public String getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(String approvedDate) {
        this.approvedDate = approvedDate;
    }

    public String getRepaymentDate() {
        return repaymentDate;
    }

    public void setRepaymentDate(String repaymentDate) {
        this.repaymentDate = repaymentDate;
    }

    public String getDisbursedDate() {
        return disbursedDate;
    }

    public void setDisbursedDate(String disbursedDate) {
        this.disbursedDate = disbursedDate;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public double getRepaidAmount() {
        return repaidAmount;
    }

    public void setRepaidAmount(double repaidAmount) {
        this.repaidAmount = repaidAmount;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void disburseFunds(Context context, String phone, int amount, String docId, int paybackAmount) {

        MobileMoneyPayoutTask payoutTask = new MobileMoneyPayoutTask(phone, amount);
        payoutTask.executePayout(new MobileMoneyPayoutTask.MobileMoneyPayoutListener() {
            @Override
            public void onPayoutSuccess() {
                // Handle successful payout
                try {
                 //   Toast.makeText(context, "Money has been transferred to your account successfully", Toast.LENGTH_SHORT).show();
                    FirestoreCRUD firestoreCRUD = new FirestoreCRUD();
                    // Get the current date and time
                    Date currentDate = Calendar.getInstance().getTime();

                    // Format the current date as a string in the desired format (e.g., "yyyy-MM-dd HH:mm:ss")
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    String formattedDate = dateFormat.format(currentDate);
                    Map<String, Object> updateData = new HashMap<>();
                    updateData.put("status", "disbursed");
                    updateData.put("disbursedDate", formattedDate);
                    updateData.put("disbursedAmount",paybackAmount);
                    firestoreCRUD.updateDocumentFields("transaction", docId, updateData, task -> Toast.makeText(context, "Account credited successfully !!", Toast.LENGTH_SHORT).show());

                } catch (Exception e) {
                    Log.d("Payout",e.getMessage());

                }   
            }

            @Override
            public void onPayoutFailure() {
                // Handle payout failure

            Log.d("Failed", "payout failed");
            }
        });

    
    }

    public void payback(Context context, int amount, String transactionId, String phoneNumber, int actualRepayment) {

        MobileMoneyDepositTask depositTask = new MobileMoneyDepositTask();
        depositTask.depositMoney(transactionId,amount,phoneNumber,actualRepayment,new MobileMoneyDepositTask.MobileMoneyDepositListener() {
            @Override
            public void onDepositSuccess(String s) {
                // Handle successful deposit
                int balance = actualRepayment - amount;
                String status = "";
                if (balance > 0) {
                    status = "partially_paid";
                } else {
                    status = "fully_paid";
                }

                // Get the current date and time
                Date currentDate = Calendar.getInstance().getTime();
                // Format the current date as a string in the desired format (e.g., "yyyy-MM-dd HH:mm:ss")
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String formattedDate = dateFormat.format(currentDate);
                Map<String, Object> updatedData = new HashMap<>();
                updatedData.put("status", status);
                updatedData.put("paybackAmount", balance);
                updatedData.put("repaidAmount", amount);
                updatedData.put("repaymentDate", formattedDate);

                firestoreCRUD.updateDocumentFields("transaction", transactionId, updatedData, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            updatedData.put("transactionId", transactionId);
                            Toast.makeText(context, "Transaction completed successfully!!", Toast.LENGTH_SHORT).show();
                            firestoreCRUD.createDocument("Repayment", updatedData, new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("RepaymentData", "Data added successfully");
                                    } else {
                                        Log.d("RepaymentData", task.getException().getMessage());
                                    }

                                }
                            });
                        }

                    }
                });
                Log.d("Success", "deposit successful"+s);
            }

            @Override
            public void onDepositFailure() {
                // Handle failed deposit
                Log.d("Fail", "deposit failed");
            }
        });





    }
}
