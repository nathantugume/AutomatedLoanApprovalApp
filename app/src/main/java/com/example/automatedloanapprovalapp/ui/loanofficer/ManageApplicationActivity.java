package com.example.automatedloanapprovalapp.ui.loanofficer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.automatedloanapprovalapp.R;
import com.example.automatedloanapprovalapp.adapters.TransactionAdapter;
import com.example.automatedloanapprovalapp.classes.FirestoreCRUD;
import com.example.automatedloanapprovalapp.classes.MobileMoneyPayoutTask;
import com.example.automatedloanapprovalapp.classes.Transaction;
import com.example.automatedloanapprovalapp.classes.UserManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ManageApplicationActivity extends AppCompatActivity implements TransactionAdapter.OnTransactionClickListener{

    private RecyclerView recyclerView;
    private TransactionAdapter adapter;
    private List<Transaction> transactions;
    private   String userName;
    private FirestoreCRUD firestoreCRUD = new FirestoreCRUD();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_application);

        // Initialize RecyclerView and data
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        transactions = new ArrayList<>(); // Initialize your transactions list here

        // Create and set the adapter
        adapter = new TransactionAdapter(transactions,this);
        recyclerView.setAdapter(adapter);

        firestoreCRUD.getAllDocuments("transaction", new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    transactions.clear(); // Clear the list before adding new data
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Parse the document data and create Transaction objects
                        String documentId = document.getId();
                        String dateRequested = document.getString("dateRequested");
                        String loanType = document.getString("loanType");
                        int paybackAmount = document.getLong("paybackAmount").intValue();
                        int requestedAmount = document.getLong("requestedAmount").intValue();
                        String status = document.getString("status");
                        String userId = document.getString("userId");

                        //get user name

                        firestoreCRUD.readDocument("users", userId, new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                DocumentSnapshot documentSnapshot = task.getResult();

                                userName = documentSnapshot.getString("username");
                                Log.d("userName",userName);
                                Transaction transaction = new Transaction(documentId,userId,loanType,requestedAmount,paybackAmount,dateRequested,status,userName);
                                transactions.add(transaction);
                                adapter.notifyDataSetChanged();
                            }
                        });



                    }
                 // Notify the adapter that data has changed
                } else {
                    // Handle errors
                    Toast.makeText(ManageApplicationActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void onTransactionClick(String transactionId) {
        UserManager userManager = new UserManager(this);
        String uid = userManager.getCurrentUser().getUid();
        // Get the current date and time

        Date currentDate = Calendar.getInstance().getTime();

        // Format the current date as a string in the desired format (e.g., "yyyy-MM-dd HH:mm:ss")
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String formattedDate = dateFormat.format(currentDate);
        Map<String, Object> updateData = new HashMap<>();
        updateData.put("status", "approved");
        updateData.put("approvedBy", uid);
        updateData.put("approvedDate",formattedDate);



        firestoreCRUD.updateDocumentFields("transaction", transactionId,updateData, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    // Call the disburseFunds function after updating the status
//                    disburseFunds(transactionId);
                    firestoreCRUD.readDocument("transaction", transactionId, new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()){
                                DocumentSnapshot documentSnapshot = task.getResult();
                                String customerID = documentSnapshot.getString("userId");
                                int amount = Math.toIntExact(documentSnapshot.getLong("requestedAmount"));

                                Log.d("customerId",customerID+" ,"+String.valueOf(amount));
                                firestoreCRUD.readDocument("customer_details", customerID, new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()){
                                            DocumentSnapshot snapshot = task.getResult();
                                            String phoneNumber = snapshot.getString("phoneNumber");
                                            Log.d("manageApplication","phoneNumber:"+phoneNumber);
                                            Transaction transaction = new Transaction();
                                            transaction.disburseFunds(ManageApplicationActivity.this,phoneNumber,amount,transactionId);
                                        }else
                                        {
                                            Toast.makeText(ManageApplicationActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });


                            }
                        }
                    });


                    Toast.makeText(ManageApplicationActivity.this, "clicked"+transactionId, Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(ManageApplicationActivity.this, "Failed to approve loan, please try again!!", Toast.LENGTH_SHORT).show();
                }
            }




        });
      //  Toast.makeText(this, "clicked "+transactionId, Toast.LENGTH_SHORT).show();
    }
//    private void disburseFunds(String transactionId) {
//        // To execute the task, create an instance of DisburseFundsTask and call execute():
//        MobileMoneyPayoutTask payoutTask = new MobileMoneyPayoutTask();
//        payoutTask.execute();
//        Toast.makeText(this, "clicked"+transactionId, Toast.LENGTH_SHORT).show();
//    }



}
