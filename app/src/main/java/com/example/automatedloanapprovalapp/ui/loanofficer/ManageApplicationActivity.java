package com.example.automatedloanapprovalapp.ui.loanofficer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.automatedloanapprovalapp.R;
import com.example.automatedloanapprovalapp.adapters.TransactionAdapter;
import com.example.automatedloanapprovalapp.classes.FirestoreCRUD;
import com.example.automatedloanapprovalapp.classes.Transaction;
import com.example.automatedloanapprovalapp.classes.UserManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

        TextView noApplicationTxt = findViewById(R.id.no_application_textView);

        // Initialize RecyclerView and data
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        transactions = new ArrayList<>(); // Initialize your transactions list here


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        MaterialToolbar materialToolbar = findViewById(R.id.topAppBar);

        materialToolbar.setNavigationOnClickListener(view -> onBackPressed());

        // Create and set the adapter
        adapter = new TransactionAdapter(transactions,this);
        recyclerView.setAdapter(adapter);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    // Scrolling up, hide the bottom navigation
                    hideBottomNavigation();
                } else if (dy < 0) {
                    // Scrolling down, show the bottom navigation
                    showBottomNavigation();
                }
            }

            private void showBottomNavigation() {
                if (bottomNavigationView.getVisibility() != View.VISIBLE) {
                    bottomNavigationView.setVisibility(View.VISIBLE);
                }
            }

            private void hideBottomNavigation() {
                if (bottomNavigationView.getVisibility() == View.VISIBLE) {
                    bottomNavigationView.setVisibility(View.GONE);
                }
            }
        });

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_manager_home){
                Intent intent = new Intent(ManageApplicationActivity.this, OfficerDashboardActivity.class);
                startActivity(intent);
                return true;
            }
            if (item.getItemId() == R.id.bottom_reports){
                startActivity(new Intent(ManageApplicationActivity.this, OfficerReportActivity.class));
                return true;
            }
            if (item.getItemId() == R.id.bottom_mng_loan){
                startActivity(new Intent(ManageApplicationActivity.this, ManageLoanActivity.class));
                return true;
            }
            if (item.getItemId() == R.id.bottom_transactions){
                startActivity(new Intent(ManageApplicationActivity.this, TransactionActivity.class));
                return true;
            }
            if (item.getItemId() == R.id.bottom_accounts){
                startActivity(new Intent(ManageApplicationActivity.this,ManageAccountActivity.class));
                return true;
            }
            return false;
        });


        // Create a ProgressDialog
        ProgressDialog progressDialog = new ProgressDialog(ManageApplicationActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        firestoreCRUD.getAllDocuments("transaction", task -> {
            progressDialog.dismiss(); // Dismiss the progress dialog when the data is loaded

            if (task.isSuccessful()) {
                transactions.clear(); // Clear the list before adding new data
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String status = document.getString("status");
                    if (status.equals("pending")) {
                        String documentId = document.getId();
                        String dateRequested = document.getString("dateRequested");
                        String loanType = document.getString("loanType");
                        int paybackAmount = document.getLong("paybackAmount").intValue();
                        int requestedAmount = document.getLong("requestedAmount").intValue();
                        String userId = document.getString("userId");

                        //get user name
                        firestoreCRUD.readDocument("users", userId, task1 -> {
                            DocumentSnapshot documentSnapshot = task1.getResult();
                            userName = documentSnapshot.getString("username");
                            Transaction transaction = new Transaction(documentId, userId, loanType, requestedAmount, paybackAmount, dateRequested, status, userName);
                            transactions.add(transaction);
                            adapter.notifyDataSetChanged();
                            noApplicationTxt.setVisibility(View.GONE);
                        });

                    } else {
                        noApplicationTxt.setVisibility(View.VISIBLE);
                    }
                }
            } else {
                // Handle errors
                Toast.makeText(ManageApplicationActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
            }
        });




    }




    @Override
    public void onTransactionClick(String transactionId) {
        UserManager userManager = new UserManager(this);
        String uid = userManager.getCurrentUser().getUid();
        // Show progress dialog
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Processing please wait...");
        progressDialog.show();


        // Get the current date and time
        Date currentDate = Calendar.getInstance().getTime();

        // Format the current date as a string in the desired format (e.g., "yyyy-MM-dd HH:mm:ss")
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String formattedDate = dateFormat.format(currentDate);
        Map<String, Object> updateData = new HashMap<>();
        updateData.put("status", "approved");
        updateData.put("approvedBy", uid);
        updateData.put("approvedDate",formattedDate);


        firestoreCRUD.updateDocumentFields("transaction", transactionId,updateData, task -> {
            if (task.isSuccessful()){
                // Call the disburseFunds function after updating the status
//                    disburseFunds(transactionId);
                firestoreCRUD.readDocument("transaction", transactionId, task12 -> {
                    if (task12.isSuccessful()){
                        DocumentSnapshot documentSnapshot = task12.getResult();
                        String customerID = documentSnapshot.getString("userId");
                        int amount = Math.toIntExact(documentSnapshot.getLong("requestedAmount"));
                        int paybackAmount = Math.toIntExact(documentSnapshot.getLong("paybackAmount"));

                        firestoreCRUD.readDocument("customer_details", customerID, task1 -> {
                            if (task1.isSuccessful()){
                                DocumentSnapshot snapshot = task1.getResult();
                                String phoneNumber = snapshot.getString("phoneNumber");
                                                           Transaction transaction = new Transaction();
                                transaction.disburseFunds(ManageApplicationActivity.this,phoneNumber,amount,transactionId,paybackAmount);
                            }else
                            {
                                Toast.makeText(ManageApplicationActivity.this, task1.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });


            }else {
                Toast.makeText(ManageApplicationActivity.this, "Failed to approve loan, please try again!!", Toast.LENGTH_SHORT).show();
            }
        });

    }




}
