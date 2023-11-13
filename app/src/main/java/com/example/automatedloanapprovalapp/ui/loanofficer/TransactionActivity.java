package com.example.automatedloanapprovalapp.ui.loanofficer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.automatedloanapprovalapp.R;
import com.example.automatedloanapprovalapp.adapters.RepaymentAdapter;
import com.example.automatedloanapprovalapp.classes.FirestoreCRUD;
import com.example.automatedloanapprovalapp.classes.PersonalInformation;
import com.example.automatedloanapprovalapp.classes.RepaymentItem;
import com.example.automatedloanapprovalapp.classes.Transaction;
import com.example.automatedloanapprovalapp.ui.customer.TransactionHistoryActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class TransactionActivity extends AppCompatActivity {

    private FirestoreCRUD firestoreCRUD = new FirestoreCRUD();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        //TopAppBar
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        //BottomNavigation

        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_manager_home){
                startActivity(new Intent(TransactionActivity.this, OfficerDashboardActivity.class));
                return true;
            }
            if (item.getItemId() == R.id.bottom_reports){
                startActivity(new Intent(TransactionActivity.this, OfficerReportActivity.class));
                return true;
            }
            if (item.getItemId() == R.id.bottom_mng_loan){
                startActivity(new Intent(TransactionActivity.this,ManageLoanActivity.class));
                return true;
            }
            if (item.getItemId() == R.id.bottom_transactions){
                startActivity(new Intent(TransactionActivity.this, TransactionActivity.class));
                return true;
            }
            if (item.getItemId() == R.id.bottom_accounts){
                startActivity(new Intent(TransactionActivity.this, TransactionActivity.class));
                return true;
            }

            return false;
        });

        final String[][] customers = new String[1][1];
        final String[][] ids = new String[1][1];

        ArrayList<String> customerNames = new ArrayList<>();
        ArrayList<String> customerIds = new ArrayList<>();
        ArrayList<String> transactionIds = new ArrayList<>();

        ArrayList<RepaymentItem> repaymentItems = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        RepaymentAdapter adapter = new RepaymentAdapter(repaymentItems);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        TextInputLayout customerInput = findViewById(R.id.customerNameInputLayout);
        TextInputLayout idsInputLayout = findViewById(R.id.transactionIdsTxt);
        TextView noContent = findViewById(R.id.no_content);
        AutoCompleteTextView autoCompleteTid = findViewById(R.id.autocomplete_text_tid);

        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.autocomplete_text);

        firestoreCRUD.getAllDocuments("customer_details", task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot documentSnapshot : task.getResult()) {
                    PersonalInformation information = documentSnapshot.toObject(PersonalInformation.class);
                    String docId = documentSnapshot.getId();

                    customerNames.add(information.getFullName());
                    customerIds.add(docId);

                }
                customers[0] = customerNames.toArray(new String[0]);

                ArrayAdapter<String> adapter12 = new ArrayAdapter<>(TransactionActivity.this, R.layout.menu_item, customers[0]);
                autoCompleteTextView.setAdapter(adapter12);


            }
        });


        autoCompleteTextView.setOnItemClickListener((adapterView, view, i, l) -> {
            String selectedName = adapterView.getItemAtPosition(i).toString();

            int pos = customerNames.indexOf(selectedName);
            String uid = customerIds.get(pos);
            idsInputLayout.getEditText().setText("");
            transactionIds.clear();
            repaymentItems.clear();
            adapter.notifyDataSetChanged();
            firestoreCRUD.queryDocuments("transaction", "userId", uid, new OnCompleteListener<QuerySnapshot>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {

                        for (DocumentSnapshot snapshot : task.getResult()) {
                            Transaction transaction = snapshot.toObject(Transaction.class);
                            if (transaction != null) {
                                if (transaction.getUserId().equals(uid)) {
                                    transactionIds.add(snapshot.getId());
                                }
                            } else {
                                noContent.setVisibility(View.VISIBLE);
                                idsInputLayout.getEditText().setText("no transaction found!!");
                            }

                        }
                        ids[0] = transactionIds.toArray(new String[0]);

                        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(TransactionActivity.this, R.layout.menu_item, ids[0]);
                        autoCompleteTid.setAdapter(adapter1);

                    } else {
                        Log.d("transactionIDs", task.getException().getMessage());
                    }
                }
            });
        });

        autoCompleteTid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String transactionId = adapterView.getItemAtPosition(i).toString();

                firestoreCRUD.queryDocuments("Repayment", "transactionId", transactionId, new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            repaymentItems.clear(); // Clear the list before adding new items

                            for (DocumentSnapshot document : task.getResult()) {
                                RepaymentItem repaymentItem = document.toObject(RepaymentItem.class);
                                if (repaymentItem != null) {
                                    Log.d("repaymentItem", document.getString("status"));
                                    repaymentItems.add(repaymentItem);
                                }
                            }
                            if (!repaymentItems.isEmpty()) {
                                // Sort repaymentItems by date
                                sortRepaymentItemsByDate(repaymentItems);
                                noContent.setVisibility(View.GONE);
                                // Notify the adapter that the data set has changed
                                adapter.notifyDataSetChanged();
                            } else {
                                noContent.setVisibility(View.VISIBLE);

                            }

                        } else {
                            Toast.makeText(TransactionActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }

    // Inside your TransactionHistoryActivity class
    private void sortRepaymentItemsByDate(ArrayList<RepaymentItem> repaymentItems) {
        Collections.sort(repaymentItems, new Comparator<RepaymentItem>() {
            @Override
            public int compare(RepaymentItem item1, RepaymentItem item2) {
                // Compare items based on repaymentDate (assuming repaymentDate is a String)
                return item1.getRepaymentDate().compareTo(item2.getRepaymentDate());
            }
        });
    }
}