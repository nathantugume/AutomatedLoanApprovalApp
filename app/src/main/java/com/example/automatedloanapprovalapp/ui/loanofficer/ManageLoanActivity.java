package com.example.automatedloanapprovalapp.ui.loanofficer;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.automatedloanapprovalapp.R;
import com.example.automatedloanapprovalapp.adapters.LoanTypeAdapter;
import com.example.automatedloanapprovalapp.classes.LoanType;
import com.example.automatedloanapprovalapp.classes.LoanTypeManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ManageLoanActivity extends AppCompatActivity {
    private LoanTypeManager loanTypeManager = new LoanTypeManager();
    private LoanTypeAdapter loanTypeAdapter;
    private RecyclerView recyclerView;
    private String selectedStatus = "Pending";
    private ProgressDialog progressDialog;
    private MaterialToolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_loan);

//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onBackPressed();
//            }
//        });



        FloatingActionButton floatingActionButton = findViewById(R.id.fabAddLoan);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a LayoutInflater to inflate the custom dialog layout
                LayoutInflater inflater = LayoutInflater.from(ManageLoanActivity.this);
                View dialogView = inflater.inflate(R.layout.dialog_add_loan, null);

                // Find the EditText fields and other views in the custom dialog layout
                EditText loanTypeEditText = dialogView.findViewById(R.id.editTextLoanType);
                EditText interestRateEditText = dialogView.findViewById(R.id.editTextInterestRate);
                EditText durationEditText = dialogView.findViewById(R.id.editTextLoanDuration);
                Spinner loanStatusSpinner = dialogView.findViewById(R.id.spinnerLoanStatus);

            // Populate the loan status spinner with options
                String[] loanStatusOptions = {"Pending", "Active","Discontinued"};
                ArrayAdapter<String> loanStatusAdapter = new ArrayAdapter<>(ManageLoanActivity.this, android.R.layout.simple_spinner_item, loanStatusOptions);
                loanStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                loanStatusSpinner.setAdapter(loanStatusAdapter);

                loanStatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        selectedStatus = adapterView.getItemAtPosition(i).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                // Build the AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(ManageLoanActivity.this);
                builder.setView(dialogView)
                        .setTitle("Add New Loan Details")
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Retrieve user input from EditText fields
                                Double interestRate = Double.valueOf(interestRateEditText.getText().toString().trim());
                                String duration = durationEditText.getText().toString().trim();
                                String loanType = loanTypeEditText.getText().toString().trim();
                                // Perform validation and add logic here
                                if (!loanType.isEmpty() && !interestRate.isNaN() && !duration.isEmpty()) {
                                    LoanType loanType1 = new LoanType();
                                    loanType1.setType(loanType);
                                    loanType1.setDuration(duration);
                                    loanType1.setInterestRate(interestRate);
                                    loanType1.setStatus(selectedStatus);
                                    loanTypeManager.addLoanType(loanType1, ManageLoanActivity.this);

                                    // Close the dialog after successful addition
                                    dialogInterface.dismiss();
                                } else {
                                    // Display an error message if any field is empty
                                    Toast.makeText(ManageLoanActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss(); // Dismiss the dialog if canceled
                            }
                        });

                // Create and show the dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loanTypeAdapter = new LoanTypeAdapter(new ArrayList<>());
        recyclerView.setAdapter(loanTypeAdapter);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Loan Types...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        loanTypeManager.getLoanTypesAsList().addOnCompleteListener(new OnCompleteListener<List<LoanType>>() {
            @Override
            public void onComplete(@NonNull Task<List<LoanType>> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    List<LoanType> loanTypes = task.getResult();
                    loanTypeAdapter.setLoanTypes(loanTypes);
                    Log.d("id", String.valueOf(loanTypes.isEmpty()));
                } else {
                    Log.e("ManageLoanActivity", "Error fetching loan types", task.getException());
                    Toast.makeText(ManageLoanActivity.this, Objects.requireNonNull(task.getException()).toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}