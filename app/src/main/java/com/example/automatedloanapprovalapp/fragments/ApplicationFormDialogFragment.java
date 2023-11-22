package com.example.automatedloanapprovalapp.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.automatedloanapprovalapp.R;
import com.example.automatedloanapprovalapp.classes.CreditScoreCalculator;
import com.example.automatedloanapprovalapp.classes.FirestoreCRUD;
import com.example.automatedloanapprovalapp.classes.LoanType;
import com.example.automatedloanapprovalapp.classes.Notification;
import com.example.automatedloanapprovalapp.classes.Transaction;
import com.example.automatedloanapprovalapp.classes.UserManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class ApplicationFormDialogFragment extends DialogFragment {

    private LoanType selectedLoanType;

    private TextView loanTypeTextView;
    private TextView interestRateTextView;
    private TextView durationTextView;
    private TextView maxLoanAmountTxt;
    private TextView paybackAmount;
    private double interestRate;
    private double term;

    private UserManager userManager = new UserManager(getContext());
    private FirestoreCRUD firestoreCRUD = new FirestoreCRUD();
    private CreditScoreCalculator creditScoreCalculator = new CreditScoreCalculator();

    private double calculatedPayback;
    private String uid = userManager.getCurrentUser().getUid();

    public ApplicationFormDialogFragment(LoanType selectedLoanType) {
        this.selectedLoanType = selectedLoanType;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_application_form_dialog, null);

        creditScoreCalculator.setFactors();

        loanTypeTextView = view.findViewById(R.id.textViewLoanType);
        interestRateTextView = view.findViewById(R.id.textViewInterestRate);
        durationTextView = view.findViewById(R.id.textViewDuration);
        paybackAmount = view.findViewById(R.id.textViewPaybackAmount);
        maxLoanAmountTxt = view.findViewById(R.id.textViewMaxLoan);
        EditText amountEditText = view.findViewById(R.id.editTextAmount);
        Button applyButton = view.findViewById(R.id.buttonApply);


        // Display loan details
        loanTypeTextView.setText(selectedLoanType.getType());
        interestRateTextView.setText(String.valueOf(selectedLoanType.getInterestRate()));
        durationTextView.setText(selectedLoanType.getDuration() + "months");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        float calculatedCreditAmount = sharedPreferences.getFloat("calculated_credit_amount", 0.0f);
        creditScoreCalculator.setCalculatedCreditAmount((int) calculatedCreditAmount);
        maxLoanAmountTxt.setText(String.valueOf(creditScoreCalculator.getCalculatedCreditAmount()));

        interestRate = selectedLoanType.getInterestRate();
        term = Double.parseDouble(selectedLoanType.getDuration());
        double maxLoanAmount = creditScoreCalculator.getCalculatedCreditAmount();

        amountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                try {
                    String amountStr = charSequence.toString().trim();
                    double amount = Double.parseDouble(amountStr);
                    if (amount <= maxLoanAmount) {
                        calculatedPayback = creditScoreCalculator.calculatePaybackAmount(amount, interestRate, term);

                        paybackAmount.setText(String.valueOf(calculatedPayback));
                    } else {
                        amountEditText.setError("Amount exceeds the loan limit");
                    }
                } catch (Exception e) {
                    Log.d("textWatcherException", e.getMessage());
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        hasLoan(uid, allFullyPaid -> {
            if (allFullyPaid) {
                // All statuses are "fully_paid"
                Log.d("fully_paid", "true");

            } else {
                // Not all statuses are "fully_paid"
                applyButton.setEnabled(false);
                amountEditText.setError("you have a running loan ");
                Snackbar snackbar = Snackbar.make(view, "You have a pending loan request.", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Ok", v -> {
                            // Handle the action when the user clicks "Ok"

                        });

                snackbar.show();
                snackbar.setDuration(Snackbar.LENGTH_INDEFINITE);

            }
        });

        applyButton.setOnClickListener(v -> {


            String amountStr = amountEditText.getText().toString().trim();
            if (!amountStr.isEmpty()) {
                double amount = Double.parseDouble(amountStr);
                selectedLoanType.setAmount(String.valueOf(amount));

                // Show ProgressDialog
                ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Processing...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                // Get the current date and time
                Date currentDate = Calendar.getInstance().getTime();

                // Format the current date as a string in the desired format (e.g., "yyyy-MM-dd HH:mm:ss")
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String formattedDate = dateFormat.format(currentDate);


                Transaction transaction = new Transaction(uid, selectedLoanType.getType(), amount, calculatedPayback, formattedDate, "pending",calculatedPayback);
                firestoreCRUD.createDocument("transaction", transaction, task -> {
                    // Dismiss ProgressDialog
                    progressDialog.dismiss();

                    if (task.isSuccessful()) {

                        Notification notification = new Notification("New loan application added","management",uid,formattedDate);
                        firestoreCRUD.createDocument("notifications", notification, task1 -> {
                            if (task1.isSuccessful()){
                                Log.d("Notification","notification sent successfully");
                            }
                            else {
                                Log.d("Notification", task1.getException().getMessage());
                            }
                        });
                        Toast.makeText(view.getContext(), "Loan Request Submitted Successfully", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(view.getContext(), Objects.requireNonNull(task.getException()).toString(), Toast.LENGTH_SHORT).show();
                    }
                    dismiss();
                });
            } else {
                amountEditText.setText("");
                amountEditText.setError("Enter a valid amount");
            }
        });


        builder.setView(view);
        return builder.create();
    }

    private void hasLoan(String uid, LoanCheckCallback callback) {

        ArrayList<String> statusList = new ArrayList<>();

        firestoreCRUD.queryDocuments("transaction", "userId", uid, task -> {
            if (task.isSuccessful()) {
                Log.d("checkLoan", "task completed successfully");

                for (QueryDocumentSnapshot document : task.getResult()) {
                    if (document.exists()) {
                        String status = document.getString("status");
                        statusList.add(status);
                    } else {
                        statusList.add("fully_paid");
                    }

                }

                boolean allFullyPaid = true;

                for (String status : statusList) {
                    if (status == null || !status.equals("fully_paid")) {
                        allFullyPaid = false;
                        break;
                    }
                }

                if (allFullyPaid) {
                    // All statuses are "fully_paid"
                    Log.d("checkLoan", "All statuses are fully paid");
                    callback.onLoanStatusCheck(true);
                } else {
                    // Not all statuses are "fully_paid"
                    Log.d("checkLoan", "Not all statuses are fully paid");
                    callback.onLoanStatusCheck(false);
                }

            } else {
                Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Define an interface for the callback
    interface LoanCheckCallback {
        void onLoanStatusCheck(boolean allFullyPaid);
    }


}

