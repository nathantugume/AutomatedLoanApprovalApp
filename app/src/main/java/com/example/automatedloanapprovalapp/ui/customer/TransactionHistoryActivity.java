package com.example.automatedloanapprovalapp.ui.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.automatedloanapprovalapp.R;
import com.example.automatedloanapprovalapp.adapters.RepaymentAdapter;
import com.example.automatedloanapprovalapp.classes.FirestoreCRUD;
import com.example.automatedloanapprovalapp.classes.RepaymentItem;
import com.example.automatedloanapprovalapp.classes.UserManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TransactionHistoryActivity extends AppCompatActivity {
    private UserManager userManager = new UserManager(TransactionHistoryActivity.this);
    private FirestoreCRUD firestoreCRUD = new FirestoreCRUD();
    private String uid = userManager.getCurrentUser().getUid();
    private TextView loanTypeTxt,requestedDateTxt,noContentTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);

        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        BottomNavigationView bottomNavigationView  = findViewById(R.id.bottom_navigationView);

        bottomNavigationView.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.bottom_menu_home){
                Intent intent = new Intent(TransactionHistoryActivity.this, CustomerDashboardActivity.class);
                startActivity(intent);
                return true;
            }
            if (item.getItemId() == R.id.bottom_menu_account){
                Intent intent = new Intent(TransactionHistoryActivity.this, CustomerDetailsActivity.class);
                startActivity(intent);
                return true;
            }
            if (item.getItemId() == R.id.bottom_menu_repay){
                Intent intent = new Intent(TransactionHistoryActivity.this, RepaymentActivity.class);
                startActivity(intent);
                return true;
            }
            if (item.getItemId() == R.id.bottom_menu_status){
                Intent intent = new Intent(TransactionHistoryActivity.this, ApplicationStatusActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });
        final String[][] loanHistory = new String[1][1];
        ArrayList<String> transactionIds = new ArrayList<>(); // ArrayList to store transaction IDs
        ArrayList<String> requestedDates = new ArrayList<>();
        ArrayList<String> loanTypes = new ArrayList<>();
        ArrayList<RepaymentItem> repaymentItems = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        noContentTxt = findViewById(R.id.no_content_found);
        RepaymentAdapter adapter = new RepaymentAdapter(repaymentItems);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loanTypeTxt = findViewById(R.id.loanType);
        requestedDateTxt = findViewById(R.id.requestedDate);
        Spinner historySpinner = findViewById(R.id.spinnerLoanTransaction);
        firestoreCRUD.queryDocuments("transaction", "userId", uid, new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (DocumentSnapshot snapshot : task.getResult()){
                        String transactionId = snapshot.getId();
                        String requestedDate = snapshot.getString("dateRequested");
                        String loanType = snapshot.getString("loanType");
                        transactionIds.add(transactionId); // Add transaction ID to ArrayList
                        requestedDates.add(requestedDate);
                        loanTypes.add(loanType);


                    }
                    // Convert ArrayList to array
                    loanHistory[0] = transactionIds.toArray(new String[0]);

                    // Now you can use loanHistory array to set up your Spinner adapter
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(TransactionHistoryActivity.this,
                            android.R.layout.simple_spinner_item, loanHistory[0]);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // Set the adapter to your Spinner
                    historySpinner.setAdapter(adapter);
                }else {
                    Toast.makeText(TransactionHistoryActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

        historySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedTransId = adapterView.getSelectedItem().toString();

                int idPosition = transactionIds.indexOf(selectedTransId);

                String date = requestedDates.get(idPosition);
                String loan_type = loanTypes.get(idPosition);
                loanTypeTxt.setText("Loan Type: "+loan_type);
                requestedDateTxt.setText("Requested Date: "+date);

                // Show ProgressDialog
                ProgressDialog progressDialog = new ProgressDialog(TransactionHistoryActivity.this);
                progressDialog.setMessage("Processing...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                // Show ProgressDialog
                firestoreCRUD.queryDocuments("Repayment", "transactionId", selectedTransId, task -> {
                    if (task.isSuccessful()) {
                        repaymentItems.clear(); // Clear the list before adding new items

                        for (DocumentSnapshot document : task.getResult()) {
                            RepaymentItem repaymentItem = document.toObject(RepaymentItem.class);
                            if (repaymentItem != null) {
                                Log.d("repaymentItem", document.getString("status"));
                                repaymentItems.add(repaymentItem);
                                progressDialog.dismiss();
                            }else {
                                noContentTxt.setVisibility(View.VISIBLE);
                                progressDialog.dismiss();
                            }
                        }
                        // Sort repaymentItems by date
                        sortRepaymentItemsByDate(repaymentItems);
                        // Notify the adapter that the data set has changed
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(TransactionHistoryActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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