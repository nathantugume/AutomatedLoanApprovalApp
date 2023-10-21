package com.example.automatedloanapprovalapp.ui.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.automatedloanapprovalapp.R;
import com.example.automatedloanapprovalapp.ui.loanofficer.ManageLoanActivity;
import com.example.automatedloanapprovalapp.ui.loanofficer.TransactionActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationView;

public class AdminDashboardActivity extends AppCompatActivity {
    private MaterialToolbar topAppBar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        topAppBar = findViewById(R.id.topAppBar);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);

        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.open();
            }
        });
        CardView userCard = findViewById(R.id.users_card);
        CardView transaction_card = findViewById(R.id.admin_transaction_card);
        CardView loan_management_card = findViewById(R.id.loan_mgt);


        userCard.setOnClickListener(view -> {
            Intent intent = new Intent(AdminDashboardActivity.this, UserActivity.class);
            startActivity(intent);
        });

        transaction_card.setOnClickListener(view -> {
            Intent intent = new Intent(AdminDashboardActivity.this, TransactionActivity.class );
            startActivity(intent);
        });

        loan_management_card.setOnClickListener(view -> {
            Intent intent = new Intent(AdminDashboardActivity.this, ManageLoanActivity.class);
            startActivity(intent);
        });


    }

    }
