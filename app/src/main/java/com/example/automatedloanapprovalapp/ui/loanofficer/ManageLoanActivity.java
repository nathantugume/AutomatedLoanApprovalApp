package com.example.automatedloanapprovalapp.ui.loanofficer;

import android.annotation.SuppressLint;
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
import com.example.automatedloanapprovalapp.classes.FirestoreCRUD;
import com.example.automatedloanapprovalapp.classes.LoanType;
import com.example.automatedloanapprovalapp.classes.LoanTypeManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ManageLoanActivity extends AppCompatActivity implements LoanTypeAdapter.OnEditClickListener,LoanTypeAdapter.OnDeleteClickListener{
    private LoanTypeManager loanTypeManager = new LoanTypeManager();
    private LoanTypeAdapter loanTypeAdapter;
    private RecyclerView recyclerView;
    private String selectedStatus = "Pending";
    private ProgressDialog progressDialog;
    private MaterialToolbar toolbar;
    private LoanTypeAdapter.OnEditClickListener onEditClickListener;
    private FirestoreCRUD firestoreCRUD = new FirestoreCRUD();
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
            @SuppressLint("NotifyDataSetChanged")
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
                        .setPositiveButton("Save", (dialogInterface, i) -> {
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
                                loanTypeAdapter.notifyDataSetChanged();
                                // Close the dialog after successful addition
                                dialogInterface.dismiss();
                            } else {
                                // Display an error message if any field is empty
                                Toast.makeText(ManageLoanActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancel", (dialogInterface, i) -> {
                            dialogInterface.dismiss(); // Dismiss the dialog if canceled
                        });

                // Create and show the dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loanTypeAdapter = new LoanTypeAdapter(new ArrayList<>(), this,this);
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

    @Override
    public void onEditClick(LoanType loanType) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_edit_loan, null);

        // Initialize your views from dialogView, e.g., EditText and Spinner
        EditText loanTypeEditText = dialogView.findViewById(R.id.editTextLoanType);
        EditText interestRateEditText = dialogView.findViewById(R.id.editTextInterestRate);
        EditText durationEditText = dialogView.findViewById(R.id.editTextLoanDuration);
        Spinner loanStatusSpinner = dialogView.findViewById(R.id.spinnerLoanStatus);

        // Set initial values in the dialog's views
        loanTypeEditText.setText(loanType.getType());
        interestRateEditText.setText(String.valueOf(loanType.getInterestRate()));
        durationEditText.setText(loanType.getDuration());

        // Populate and set selected status in the loan status spinner
        String[] loanStatusOptions = {"Pending", "Active", "Discontinued"};
        ArrayAdapter<String> loanStatusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, loanStatusOptions);
        loanStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        loanStatusSpinner.setAdapter(loanStatusAdapter);
        loanStatusSpinner.setSelection(loanStatusAdapter.getPosition(loanType.getStatus()));

        builder.setView(dialogView)
                .setTitle("Edit Loan Details")
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Retrieve updated values from the dialog's views
                        String editedLoanType = loanTypeEditText.getText().toString().trim();
                        double editedInterestRate = Double.parseDouble(interestRateEditText.getText().toString().trim());
                        String editedDuration = durationEditText.getText().toString().trim();
                        String editedStatus = loanStatusSpinner.getSelectedItem().toString();

                        LoanType loanType1 = new LoanType(loanType.getId(),editedLoanType,editedInterestRate,editedDuration,editedStatus);

                        firestoreCRUD.updateDocument("loanTypes", loanType.getId(), loanType1, new OnCompleteListener<Void>() {
                            @SuppressLint("NotifyDataSetChanged")
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(ManageLoanActivity.this, "Loan Type Updated Successfully!!", Toast.LENGTH_SHORT).show();
                                    loanTypeAdapter.notifyDataSetChanged();
                                }
                                else {
                                    Toast.makeText(ManageLoanActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        // Close the dialog after successful update
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss(); // Dismiss the dialog if canceled
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    @Override
    public void onDeleteClick(LoanType loanType) {
        showDeleteConfirmationDialog(loanType);
        
    }

    @SuppressLint("NotifyDataSetChanged")
    private void showDeleteConfirmationDialog(LoanType loanType) {
        Log.d("loanType","id "+loanType.getId());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Deletion")
                .setMessage("Are you sure you want to delete "+loanType.getType()+" ?")
                .setPositiveButton("Delete", (dialogInterface, i) -> firestoreCRUD.deleteDocument("loanTypes", loanType.getId(), task -> {
                    if (task.isSuccessful()){
                        Toast.makeText(ManageLoanActivity.this, loanType.getType()+" deleted Successfully", Toast.LENGTH_SHORT).show();

                         loanTypeAdapter.notifyDataSetChanged();
                    }
                }))
                .setNegativeButton("Cancel", (dialogInterface, i) -> {
                    dialogInterface.dismiss(); // Dismiss the dialog if canceled
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}