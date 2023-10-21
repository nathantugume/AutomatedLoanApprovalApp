package com.example.automatedloanapprovalapp.ui.loanofficer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.automatedloanapprovalapp.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationView;

public class OfficerDashboardActivity extends AppCompatActivity {
    private MaterialToolbar topAppBar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_officer_dashboard);

        topAppBar = findViewById(R.id.topAppBar);
        drawerLayout = findViewById(R.id.drawerLayout);


        CardView manage_loan_card = findViewById(R.id.manage_loan);
        CardView manage_applications = findViewById(R.id.manage_applications);
        CardView monitor_repayment_card = findViewById(R.id.monitor_repayment_card);
        CardView transactionCard = findViewById(R.id.transactions_card);
        CardView manage_account_card = findViewById(R.id.manage_account_card);

        manage_account_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            newActivity(ManageAccountActivity.class);
            }
        });

        transactionCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newActivity(TransactionActivity.class);
            }
        });

        monitor_repayment_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newActivity(MonitorRepaymentActivity.class);
            }
        });

        manage_applications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newActivity(ManageApplicationActivity.class);
            }
        });

        manage_loan_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newActivity(ManageLoanActivity.class);
            }
        });


        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.open();
            }
        });


    }

    public void newActivity(Class<?> destinationActivity) {
        intent = new Intent(OfficerDashboardActivity.this, destinationActivity);
        startActivity(intent);
    }

}
