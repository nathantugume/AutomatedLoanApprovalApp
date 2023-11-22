package com.example.automatedloanapprovalapp.ui.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.number.FormattedNumber;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.automatedloanapprovalapp.R;
import com.example.automatedloanapprovalapp.classes.CreditScoreCalculator;
import com.example.automatedloanapprovalapp.classes.FirestoreCRUD;
import com.example.automatedloanapprovalapp.classes.PersonalInformation;
import com.example.automatedloanapprovalapp.classes.UserManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class EligibilityCheckActivity extends AppCompatActivity {

    private FirestoreCRUD firestoreCRUD = new FirestoreCRUD();
    private UserManager userManager = new UserManager(EligibilityCheckActivity.this);
    NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
    private  CreditScoreCalculator creditScoreCalculator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eligibility_check);
        TextView loanLimit = findViewById(R.id.loanLimit);
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
        BottomNavigationView bottomNavigationView  = findViewById(R.id.bottom_navigationView);

        bottomNavigationView.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.bottom_menu_home){
                Intent intent = new Intent(EligibilityCheckActivity.this, CustomerDashboardActivity.class);
                startActivity(intent);
                return true;
            }
            if (item.getItemId() == R.id.bottom_menu_account){
                Intent intent = new Intent(EligibilityCheckActivity.this, CustomerDetailsActivity.class);
                startActivity(intent);
                return true;
            }
            if (item.getItemId() == R.id.bottom_menu_repay){
                Intent intent = new Intent(EligibilityCheckActivity.this, RepaymentActivity.class);
                startActivity(intent);
                return true;
            }
            if (item.getItemId() == R.id.bottom_menu_status){
                Intent intent = new Intent(EligibilityCheckActivity.this, ApplicationStatusActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });

        String uid = userManager.getCurrentUser().getUid();

        Button button =  findViewById(R.id.applyBtn);

        button.setOnClickListener(view -> {
            Intent intent = new Intent(EligibilityCheckActivity.this, LoanApplicationActivity.class);
            startActivity(intent);
        });
        creditScoreCalculator = new CreditScoreCalculator();

        CompletableFuture<Void> setFactorsFuture = creditScoreCalculator.setFactors();

        setFactorsFuture.thenRun(() -> {
            // Show ProgressDialog
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Processing...");
            progressDialog.setCancelable(false);
            progressDialog.show();


            firestoreCRUD.readDocument("customer_details", uid, task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {


                        PersonalInformation personalInformation = document.toObject(PersonalInformation.class);

                        if (personalInformation != null) {
                            CreditScoreCalculator creditScoreCalculator = new CreditScoreCalculator();
                            creditScoreCalculator.setFactors();
                            int creditScore = creditScoreCalculator.calculateCreditScore(personalInformation);

                            creditScoreCalculator.setCreditScore(creditScore);
                            int loan_limit = creditScoreCalculator.calculateLoanAmount(creditScore);

                            loanLimit.setText("Ugx " + String.valueOf(numberFormat.format(loan_limit)));

                            progressDialog.dismiss();


                        }
                    }

                }
            });

        });
    }
}