package com.example.automatedloanapprovalapp.ui.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.number.FormattedNumber;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.NumberFormat;
import java.util.Locale;

public class EligibilityCheckActivity extends AppCompatActivity {

    private FirestoreCRUD firestoreCRUD = new FirestoreCRUD();
    private UserManager userManager = new UserManager(EligibilityCheckActivity.this);
    NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eligibility_check);
        TextView loanLimit = findViewById(R.id.loanLimit);



        String uid = userManager.getCurrentUser().getUid();

        Button button =  findViewById(R.id.applyBtn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EligibilityCheckActivity.this, LoanApplicationActivity.class);
                startActivity(intent);
            }
        });


        firestoreCRUD.readDocument("customer_details", uid, new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        PersonalInformation personalInformation = document.toObject(PersonalInformation.class);

                        if (personalInformation != null) {
                            CreditScoreCalculator creditScoreCalculator = new CreditScoreCalculator();
                            int creditScore = creditScoreCalculator.calculateCreditScore(personalInformation);

                            creditScoreCalculator.setCreditScore(creditScore);
                            int loan_limit = creditScoreCalculator.calculateLoanAmount(creditScore);

                            loanLimit.setText("Ugx "+String.valueOf(numberFormat.format(loan_limit)));



                        }
                    }

                }
            }
        });
    }
}