package com.example.automatedloanapprovalapp.ui.customer;

import android.content.Intent;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.automatedloanapprovalapp.R;

public class CustomerDashboardActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard);

        CardView loan_application = findViewById(R.id.loan_application);
        CardView application_status = findViewById(R.id.application_status_card);
        CardView eligibility_checkCard = findViewById(R.id.eligibility_checkCard);
        CardView repaymentCard = findViewById(R.id.loan_repayment_card);
        CardView transactionHistoryCard = findViewById(R.id.transactionHistoryCard);
        CardView customerAccountCard = findViewById(R.id.customerAccountCard);

        customerAccountCard.setOnClickListener(view -> {
            Intent intent = new Intent(CustomerDashboardActivity.this, CustomerDetailsActivity.class);
            startActivity(intent);
        });

        transactionHistoryCard.setOnClickListener(view -> {
            Intent intent = new Intent(CustomerDashboardActivity.this,TransactionHistoryActivity.class);
            startActivity(intent);
        });
        repaymentCard.setOnClickListener(view -> {
            Intent intent = new Intent(CustomerDashboardActivity.this,RepaymentActivity.class);
            startActivity(intent);
        });
        application_status.setOnClickListener(view -> {
            Intent intent = new Intent(CustomerDashboardActivity.this, ApplicationStatusActivity.class);
            startActivity(intent);
        });
        loan_application.setOnClickListener(view -> {
            // Handle loan application button click here
            // For example, start a new activity for loan application form
            Intent intent = new Intent(CustomerDashboardActivity.this, LoanApplicationActivity.class);
            startActivity(intent);
        });
        eligibility_checkCard.setOnClickListener(view -> {            Intent intent = new Intent(CustomerDashboardActivity.this, EligibilityCheckActivity.class);
            startActivity(intent);
        });

    }


}
