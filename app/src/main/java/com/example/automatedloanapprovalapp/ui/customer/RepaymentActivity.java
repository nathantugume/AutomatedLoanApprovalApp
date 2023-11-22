package com.example.automatedloanapprovalapp.ui.customer;

import static com.example.automatedloanapprovalapp.classes.InstalmentCalculator.calculateInstallment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.automatedloanapprovalapp.R;
import com.example.automatedloanapprovalapp.classes.FirestoreCRUD;
import com.example.automatedloanapprovalapp.classes.LoanType;
import com.example.automatedloanapprovalapp.classes.MobileMoneyDepositTask;
import com.example.automatedloanapprovalapp.classes.Transaction;
import com.example.automatedloanapprovalapp.classes.UserManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Objects;

public class RepaymentActivity extends AppCompatActivity {
    private FirestoreCRUD firestoreCRUD = new FirestoreCRUD();
    private UserManager userManager = new UserManager(RepaymentActivity.this);
    private  String uid = userManager.getCurrentUser().getUid();

    private  Transaction transaction;

    private TextView installmentTxt;

    private  ProgressDialog progressDialog;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repayment);
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
        BottomNavigationView bottomNavigationView  = findViewById(R.id.bottom_navigationView);
        installmentTxt = findViewById(R.id.txtInstallmentAmount);

        bottomNavigationView.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.bottom_menu_home){
                Intent intent = new Intent(RepaymentActivity.this, CustomerDashboardActivity.class);
                startActivity(intent);
                return true;
            }
            if (item.getItemId() == R.id.bottom_menu_account){
                Intent intent = new Intent(RepaymentActivity.this, CustomerDetailsActivity.class);
                startActivity(intent);
                return true;
            }
            if (item.getItemId() == R.id.bottom_menu_repay){
                Intent intent = new Intent(RepaymentActivity.this, RepaymentActivity.class);
                startActivity(intent);
                return true;
            }
            if (item.getItemId() == R.id.bottom_menu_status){
                Intent intent = new Intent(RepaymentActivity.this, ApplicationStatusActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });

        TextView loanBalanceTxt = findViewById(R.id.loanBalance);
        TextInputEditText payAmountEdt = findViewById(R.id.payAmount);
        Button payBtn = findViewById(R.id.payBtn);
        final String[] transactionId = new String[1];
        final String[] status = new String[1];
        final int[] paybackAmount = new int[1];
        final String[] phoneNumber = new String[1];
        final String[] loanType = new String[1];

        payAmountEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String inputText = charSequence.toString();
                if (!inputText.isEmpty()) {
                    int inputAmount = Integer.parseInt(inputText);
                    if (inputAmount > paybackAmount[0]) {
                        // Input amount is greater than payback amount, set an error
                        payAmountEdt.setError("Amount cannot be greater than the payback amount");
                    } else {
                        // Input amount is valid, clear any previous errors
                        payAmountEdt.setError(null);
                    }
                } else {
                    // Input is empty, clear any previous errors
                    payAmountEdt.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // Show ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Processing ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        firestoreCRUD.readDocument("customer_details", uid, new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                phoneNumber[0] = documentSnapshot.getString("phoneNumber");
            }
        });
        firestoreCRUD.queryDocuments("transaction", "userId", uid, task -> {
            if (task.isSuccessful()){
                QuerySnapshot snapshot = task.getResult();
                if (snapshot != null) {
                    for (QueryDocumentSnapshot document : snapshot) {
                        transaction = document.toObject(Transaction.class);
                        // Get the document ID
                         transactionId[0] = document.getId();
                         status[0] = document.getString("status");
                         paybackAmount[0] = Math.toIntExact(document.getLong("paybackAmount"));
                         loanType[0] = document.getString("loanType");

                            installmentCalc();

                    }
                    loanBalanceTxt.setText("Ugx "+paybackAmount[0]);
                }else
                {
                    Log.d("amount",transactionId[0]+ Arrays.toString(transactionId));
                    loanBalanceTxt.setText("Ugx 0");
                    Toast.makeText(RepaymentActivity.this, "data is empty", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(RepaymentActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }

        });

        payBtn.setOnClickListener(view -> {
        int amount = Integer.parseInt(Objects.requireNonNull(payAmountEdt.getText()).toString());
          Transaction transaction = new Transaction();
          transaction.payback(RepaymentActivity.this,amount,transactionId[0],phoneNumber[0],paybackAmount[0]);

            // Execute the MobileMoneyDepositTask
            Log.d("button pay","pressed");


        });
    }

    private void installmentCalc() {
        firestoreCRUD.queryDocuments("loanTypes", "type", transaction.getLoanType(), task -> {
            if (task.isSuccessful()){
                for (QueryDocumentSnapshot snapshot: task.getResult() ) {
                    LoanType loanType1 = snapshot.toObject(LoanType.class);
                    Double monthlyInstallment =  calculateInstallment(transaction.getRequestedAmount(),loanType1.getInterestRate(),loanType1.getDurationAsInt());
                    NumberFormat numberFormat = NumberFormat.getNumberInstance(); // You can choose a different format if needed
                    String formattedInstallment = numberFormat.format(Math.ceil(monthlyInstallment));
                    installmentTxt.setText(formattedInstallment);
                    progressDialog.dismiss();

                }
            }
        });
    }
}