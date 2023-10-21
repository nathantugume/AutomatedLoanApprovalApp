package com.example.automatedloanapprovalapp.ui.loanofficer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import com.example.automatedloanapprovalapp.R;
import com.example.automatedloanapprovalapp.adapters.RepaymentTransactionAdapter;
import com.example.automatedloanapprovalapp.classes.FirestoreCRUD;
import com.example.automatedloanapprovalapp.classes.PersonalInformation;
import com.example.automatedloanapprovalapp.classes.RepaymentTransaction;
import com.example.automatedloanapprovalapp.classes.Transaction;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MonitorRepaymentActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RepaymentTransactionAdapter adapter;
    private TextInputEditText searchEditText;

    FirestoreCRUD firestoreCRUD = new FirestoreCRUD();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor_repayment);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create a list of RepaymentTransaction objects (you can fetch this from Firebase)
        List<RepaymentTransaction> transactions = new ArrayList<>();
        // Add your RepaymentTransaction objects to the list

        // Initialize the adapter with the list of transactions
        adapter = new RepaymentTransactionAdapter(transactions);
        recyclerView.setAdapter(adapter);

        // Initialize search EditText
        searchEditText = findViewById(R.id.monitor_search);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Filter the list based on the search query
                String searchText = charSequence.toString().toLowerCase().trim();
                List<RepaymentTransaction> filteredTransactions = new ArrayList<>();

                for (RepaymentTransaction transaction : transactions) {
                    if (transaction.getCustomerName().toLowerCase().contains(searchText)) {
                        filteredTransactions.add(transaction);
                    }
                }

                // Update the adapter with the filtered list
                adapter.updateList(filteredTransactions);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        firestoreCRUD.getAllDocuments("transaction", new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for ( DocumentSnapshot documentSnapshot:  task.getResult()) {

                        Transaction transaction = documentSnapshot.toObject(Transaction.class);

                        firestoreCRUD.readDocument("customer_details", transaction.getUserId(), new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()){
                                    DocumentSnapshot document = task.getResult();
                                    PersonalInformation personalInformation = document.toObject(PersonalInformation.class);

                                    RepaymentTransaction repaymentTransaction = new RepaymentTransaction();
                                    double loanAmount = transaction.getRequestedAmount();
                                    double paidAmount = transaction.getPaybackAmount();
                                    int paidPercentage ;

                                        // Calculate the percentage decrease if both loanAmount and paidAmount are greater than 0
                                        double decreasePercentage = ((loanAmount - paidAmount) / loanAmount) * 100;
                                        paidPercentage = (int) decreasePercentage;

                                    repaymentTransaction.setCustomerName(personalInformation.getFullName());
                                    Log.d("fullName", personalInformation.getFullName());
                                    repaymentTransaction.setLoanAmount((int) transaction.getRequestedAmount());
                                    repaymentTransaction.setPaidAmount((int) transaction.getPaybackAmount());
                                    repaymentTransaction.setStatus(transaction.getStatus());
                                    repaymentTransaction.setPaidPercentage(paidPercentage);

                                    transactions.add(repaymentTransaction);
                                    adapter.notifyDataSetChanged();

                                }
                            }
                        });





                    }
                }


            }
        });
    }
}