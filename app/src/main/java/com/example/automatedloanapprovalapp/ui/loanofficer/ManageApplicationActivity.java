package com.example.automatedloanapprovalapp.ui.loanofficer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.automatedloanapprovalapp.R;
import com.example.automatedloanapprovalapp.adapters.TransactionAdapter;
import com.example.automatedloanapprovalapp.classes.FirestoreCRUD;
import com.example.automatedloanapprovalapp.classes.Transaction;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

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

                        // Inside the ManageApplicationActivity class

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
        Toast.makeText(this, "clicked "+transactionId, Toast.LENGTH_SHORT).show();
    }
}