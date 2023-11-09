package com.example.automatedloanapprovalapp.ui.customer;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.automatedloanapprovalapp.R;
import com.example.automatedloanapprovalapp.adapters.CustomerLoanTypeAdapter;
import com.example.automatedloanapprovalapp.classes.CreditScoreCalculator;
import com.example.automatedloanapprovalapp.classes.FirestoreCRUD;
import com.example.automatedloanapprovalapp.classes.LoanType;
import com.example.automatedloanapprovalapp.classes.LoanTypeManager;
import com.example.automatedloanapprovalapp.classes.PersonalInformation;
import com.example.automatedloanapprovalapp.classes.UserManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LoanApplicationActivity extends AppCompatActivity {

    private LoanTypeManager loanTypeManager = new LoanTypeManager();
    private CustomerLoanTypeAdapter customerLoanTypeAdapter;
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private MaterialToolbar toolbar;
    private UserManager userManager = new UserManager(this);
    private String uid = userManager.getCurrentUser().getUid();
    private FirestoreCRUD firestoreCRUD = new FirestoreCRUD();
    private TextView creditScoreTxt,loanAmountTxt;
    private int loanAmount;
    private int creditScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_application);
        toolbar = findViewById(R.id.topAppBar);

        toolbar.setNavigationOnClickListener(view -> onBackPressed());


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        customerLoanTypeAdapter = new CustomerLoanTypeAdapter(new ArrayList<>(), getSupportFragmentManager());
        recyclerView.setAdapter(customerLoanTypeAdapter);

        BottomNavigationView bottomNavigationView  = findViewById(R.id.bottom_navigationView);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.bottom_menu_home){
                    Intent intent = new Intent(LoanApplicationActivity.this, CustomerDashboardActivity.class);
                    startActivity(intent);
                    return true;
                }
                if (item.getItemId() == R.id.bottom_menu_account){
                    Intent intent = new Intent(LoanApplicationActivity.this, CustomerDetailsActivity.class);
                    startActivity(intent);
                    return true;
                }
                if (item.getItemId() == R.id.bottom_menu_repay){
                    Intent intent = new Intent(LoanApplicationActivity.this, RepaymentActivity.class);
                    startActivity(intent);
                    return true;
                }
                if (item.getItemId() == R.id.bottom_menu_status){
                    Intent intent = new Intent(LoanApplicationActivity.this, ApplicationStatusActivity.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });

        creditScoreTxt = findViewById(R.id.creditScoreTxt);
        loanAmountTxt = findViewById(R.id.loanAmount);
        recyclerView.setAdapter(customerLoanTypeAdapter);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Loan Types...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // fetch all the loan types from the database
        loanTypeManager.getLoanTypesAsList().addOnCompleteListener(task -> {
            progressDialog.dismiss();
            if (task.isSuccessful()) {
                List<LoanType> loanTypes = task.getResult();
                if (!loanTypes.isEmpty()) {
                    customerLoanTypeAdapter.setLoanTypes(loanTypes);
                    Log.d("id", String.valueOf(loanTypes.isEmpty()));
                } else {
                    Toast.makeText(LoanApplicationActivity.this, "Loan Types Empty", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.e("ManageLoanActivity", "Error fetching loan types", task.getException());
                Toast.makeText(LoanApplicationActivity.this, Objects.requireNonNull(task.getException()).toString(), Toast.LENGTH_SHORT).show();
            }
        });

        // calculate credit score and loan limit using personal information
        firestoreCRUD.readDocument("customer_details", uid, task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                    PersonalInformation personalInformation = document.toObject(PersonalInformation.class);

                    if (personalInformation != null) {
                        CreditScoreCalculator creditScoreCalculator = new CreditScoreCalculator();
                        creditScore = creditScoreCalculator.calculateCreditScore(personalInformation);
                        creditScoreTxt.setText(String.valueOf(creditScore));
                        creditScoreCalculator.setCreditScore(creditScore);
                        loanAmount = creditScoreCalculator.calculateLoanAmount(creditScore);

                        loanAmountTxt.setText(String.valueOf(loanAmount));
                        creditScoreCalculator.setCalculatedCreditAmount(loanAmount);

                        // Save the calculated credit amount to SharedPreferences
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LoanApplicationActivity.this);
                        sharedPreferences.edit().putFloat("calculated_credit_amount", (float) loanAmount).apply();
                    }
                }

            }
        });


    }


}