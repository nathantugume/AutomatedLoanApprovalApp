package com.example.automatedloanapprovalapp.ui.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.automatedloanapprovalapp.R;
import com.example.automatedloanapprovalapp.classes.CreditScoreSettings;
import com.example.automatedloanapprovalapp.classes.FirestoreCRUD;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class AdminSettingsActivity extends AppCompatActivity {

    private TextInputEditText editTextAgeWeight;
    private TextInputEditText editTextMonthlyIncomeWeight;
    private TextInputEditText editTextBusinessRevenueWeight;
    private TextInputEditText editTextDependentsWeight;
    private TextInputEditText editTextAddressStabilityWeight;
    private TextInputEditText editTextJobStabilityWeight;
    private TextInputEditText editTextNationalityWeight;
    private TextInputEditText editTextRepaymentHistoryWeight;
    private TextInputEditText editTextMinAge;
    private TextInputEditText editTextMinMonthlyIncome;
    private TextInputEditText editTextMinBusinessRevenue;
    private TextInputEditText editTextPoorScoreMaxLoan;
    private TextInputEditText editTextFairScoreMaxLoan;
    private TextInputEditText editTextGoodScoreMaxLoan;
    private TextInputEditText editTextVeryGoodScoreMaxLoan;
    private TextInputEditText editTextExcellentScoreMaxLoan;

    private Button applySettingsButton;
    private Button updateSettingsButton;
    private FirestoreCRUD firestoreCRUD;
    CreditScoreSettings creditScoreSettings;

    private String documentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_settings);
        firestoreCRUD = new FirestoreCRUD();

        // Initialize your TextInputEditText and Button
        editTextAgeWeight = findViewById(R.id.editTextAgeWeight);
        editTextMonthlyIncomeWeight = findViewById(R.id.editTextMonthlyIncomeWeight);
        editTextBusinessRevenueWeight = findViewById(R.id.editTextBusinessRevenueWeight);
        editTextDependentsWeight = findViewById(R.id.editTextDependentsWeight);
        editTextAddressStabilityWeight = findViewById(R.id.editTextAddressStabilityWeight);
        editTextJobStabilityWeight = findViewById(R.id.editTextJobStabilityWeight);
        editTextNationalityWeight = findViewById(R.id.editTextNationalityWeight);
        editTextRepaymentHistoryWeight = findViewById(R.id.editTextRepaymentHistoryWeight);
        editTextMinAge = findViewById(R.id.editTextMinAge);
        editTextMinMonthlyIncome = findViewById(R.id.editTextMinMonthlyIncome);
        editTextMinBusinessRevenue = findViewById(R.id.editTextMinBusinessRevenue);
        editTextPoorScoreMaxLoan = findViewById(R.id.editTextPoorScoreMaxLoan);
        editTextFairScoreMaxLoan = findViewById(R.id.editTextFairScoreMaxLoan);
        editTextGoodScoreMaxLoan = findViewById(R.id.editTextGoodScoreMaxLoan);
        editTextVeryGoodScoreMaxLoan = findViewById(R.id.editTextVeryGoodScoreMaxLoan);
        editTextExcellentScoreMaxLoan = findViewById(R.id.editTextExcellentScoreMaxLoan);
        updateSettingsButton = findViewById(R.id.update_settings);

        loadSettings();

        applySettingsButton = findViewById(R.id.apply_settings);
        applySettingsButton.setOnClickListener(v -> saveSettings());
        updateSettingsButton.setOnClickListener(view -> updateSettings());



    }

    private void updateSettings() {
        initializeSettings();
        // Show progress dialog
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait ...");
        progressDialog.show();

        firestoreCRUD.updateDocument("creditScoreSettings", documentId, creditScoreSettings, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(AdminSettingsActivity.this, "Settings updated SuccessFully", Toast.LENGTH_SHORT).show();
                    loadSettings();
                    progressDialog.dismiss();
                }else {
                    Log.d("AdminSettings",task.getException().getMessage());
                    Toast.makeText(AdminSettingsActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void initializeSettings(){
        // Retrieve values from EditText and save them to variables
        int ageWeight = Integer.parseInt(editTextAgeWeight.getText().toString());
        int monthlyIncomeWeight = Integer.parseInt(editTextMonthlyIncomeWeight.getText().toString());
        int businessRevenueWeight = Integer.parseInt(editTextBusinessRevenueWeight.getText().toString());
        int dependentsWeight = Integer.parseInt(editTextDependentsWeight.getText().toString());
        int addressStabilityWeight = Integer.parseInt(editTextAddressStabilityWeight.getText().toString());
        int jobStabilityWeight = Integer.parseInt(editTextJobStabilityWeight.getText().toString());
        int nationalityWeight = Integer.parseInt(editTextNationalityWeight.getText().toString());
        int repaymentHistoryWeight = Integer.parseInt(editTextRepaymentHistoryWeight.getText().toString());
        int minAge = Integer.parseInt(editTextMinAge.getText().toString());
        int minMonthlyIncome = Integer.parseInt(editTextMinMonthlyIncome.getText().toString());
        int minBusinessRevenue = Integer.parseInt(editTextMinBusinessRevenue.getText().toString());
        int poorScoreMaxLoan = Integer.parseInt(editTextPoorScoreMaxLoan.getText().toString());
        int fairScoreMaxLoan = Integer.parseInt(editTextFairScoreMaxLoan.getText().toString());
        int goodScoreMaxLoan = Integer.parseInt(editTextGoodScoreMaxLoan.getText().toString());
        int veryGoodScoreMaxLoan = Integer.parseInt(editTextVeryGoodScoreMaxLoan.getText().toString());
        int excellentScoreMaxLoan = Integer.parseInt(editTextExcellentScoreMaxLoan.getText().toString());


         creditScoreSettings = new CreditScoreSettings(ageWeight,monthlyIncomeWeight,businessRevenueWeight,dependentsWeight,
                addressStabilityWeight,jobStabilityWeight,nationalityWeight,repaymentHistoryWeight,minAge,minMonthlyIncome,minBusinessRevenue
                ,poorScoreMaxLoan,fairScoreMaxLoan,goodScoreMaxLoan,veryGoodScoreMaxLoan,excellentScoreMaxLoan);

    }

    private void saveSettings() {
        initializeSettings();
        // Show progress dialog
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving settings...");
        progressDialog.show();

        firestoreCRUD.createDocument("creditScoreSettings", creditScoreSettings, task -> {
           if (task.isSuccessful()){
               Toast.makeText(AdminSettingsActivity.this, "Settings saved successfully!!", Toast.LENGTH_SHORT).show();
            loadSettings();
            progressDialog.dismiss();
           }else
           {
               Toast.makeText(AdminSettingsActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
           }
        });


    }

    private void loadSettings(){
        // Show progress dialog
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading settings ...");
        progressDialog.show();
        firestoreCRUD.getAllDocuments("creditScoreSettings", task -> {
            for (QueryDocumentSnapshot snapshot: task.getResult()) {
                documentId = snapshot.getId();
                if (task.isSuccessful()){
                    CreditScoreSettings creditScoreSettings = snapshot.toObject(CreditScoreSettings.class);


                    try {
                        editTextAgeWeight.setText(String.valueOf(creditScoreSettings.getAgeWeight()));
                        editTextMonthlyIncomeWeight.setText(String.valueOf(creditScoreSettings.getMonthlyIncomeWeight()));
                        editTextBusinessRevenueWeight.setText(String.valueOf(creditScoreSettings.getBusinessRevenueWeight()));
                        editTextDependentsWeight.setText(String.valueOf(creditScoreSettings.getDependentsWeight()));
                        editTextAddressStabilityWeight.setText(String.valueOf(creditScoreSettings.getAddressStabilityWeight()));
                        editTextJobStabilityWeight.setText(String.valueOf(creditScoreSettings.getJobStabilityWeight()));
                        editTextNationalityWeight.setText(String.valueOf(creditScoreSettings.getNationalityWeight()));
                        editTextRepaymentHistoryWeight.setText(String.valueOf(creditScoreSettings.getRepaymentHistoryWeight()));
                        editTextMinAge.setText(String.valueOf(creditScoreSettings.getMinAge()));
                        editTextMinMonthlyIncome.setText(String.valueOf(creditScoreSettings.getMinMonthlyIncome()));
                        editTextMinBusinessRevenue.setText(String.valueOf(creditScoreSettings.getMinBusinessRevenue()));
                        editTextPoorScoreMaxLoan.setText(String.valueOf(creditScoreSettings.getPoorScoreMaxLoan()));
                        editTextFairScoreMaxLoan.setText(String.valueOf(creditScoreSettings.getFairScoreMaxLoan()));
                        editTextGoodScoreMaxLoan.setText(String.valueOf(creditScoreSettings.getGoodScoreMaxLoan()));
                        editTextVeryGoodScoreMaxLoan.setText(String.valueOf(creditScoreSettings.getVeryGoodScoreMaxLoan()));
                        editTextExcellentScoreMaxLoan.setText(String.valueOf(creditScoreSettings.getExcellentScoreMaxLoan()));

                        applySettingsButton.setVisibility(View.GONE);
                        updateSettingsButton.setVisibility(View.VISIBLE);
                        progressDialog.dismiss();

                    }catch (Exception e){
                        Log.d("AdminSettings",e.getMessage());
                    }


                }


            }
        });

    }
}