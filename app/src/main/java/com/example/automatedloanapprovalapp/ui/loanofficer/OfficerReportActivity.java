package com.example.automatedloanapprovalapp.ui.loanofficer;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.automatedloanapprovalapp.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class OfficerReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_officer_report);

        //Top App Bar
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        // BottomAppBar
        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_manager_home) {
                startActivity(new Intent(OfficerReportActivity.this, OfficerDashboardActivity.class));
                return true;
            }
            if (item.getItemId() == R.id.bottom_reports) {
                item.setEnabled(true);
                startActivity(new Intent(OfficerReportActivity.this, OfficerReportActivity.class));
                return true;
            }
            if (item.getItemId() == R.id.bottom_mng_loan) {
                startActivity(new Intent(OfficerReportActivity.this, ManageLoanActivity.class))
                ;
                startActivity(new Intent(OfficerReportActivity.this, ManageLoanActivity.class));
                return true;
            }
            if (item.getItemId() == R.id.bottom_transactions) {
                startActivity(new Intent(OfficerReportActivity.this, TransactionActivity.class));
                return true;
            }
            if (item.getItemId() == R.id.bottom_accounts) {
                startActivity(new Intent(OfficerReportActivity.this, ManageAccountActivity.class));
                return true;
            }

            return false;
        });








    }
}