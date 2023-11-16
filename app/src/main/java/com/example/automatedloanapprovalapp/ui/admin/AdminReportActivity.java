package com.example.automatedloanapprovalapp.ui.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.automatedloanapprovalapp.R;
import com.example.automatedloanapprovalapp.fragments.NotificationFragment;
import com.example.automatedloanapprovalapp.ui.admin.Reports.AdminDisbursedLoansReport;
import com.example.automatedloanapprovalapp.ui.admin.Reports.AdminLoanApprovalStatistics;
import com.example.automatedloanapprovalapp.ui.admin.Reports.AdminLoanTypeDistributionActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class AdminReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_report);

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
        toolbar.setTitle("Admin Reports");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.bottom_manager_home){
                startActivity(new Intent(AdminReportActivity.this,AdminDashboardActivity.class));
                return true;
            }
            if (item.getItemId() == R.id.bottom_reports){
                startActivity(new Intent(AdminReportActivity.this, AdminReportActivity.class));
                return true;
            }
            if (item.getItemId() == R.id.bottom_mng_loan){
                startActivity(new Intent(AdminReportActivity.this, AdminManageLoanActivity.class));
                return true;
            }
            if (item.getItemId() == R.id.bottom_transactions){
                startActivity(new Intent(AdminReportActivity.this, AdminTransactionActivity.class));
                return true;
            }
            if (item.getItemId() == R.id.bottom_accounts){
                startActivity(new Intent(AdminReportActivity.this, UserActivity.class));
                return true;
            }
            return false;
        });


        CardView loan_approval_report_card = findViewById(R.id.admin_loan_approval_report_card);
        CardView loan_disbursment_report_card = findViewById(R.id.adminDisbursedLoansReportCard);
        CardView loanTypeDistributionReport = findViewById(R.id.adminLoanTypeDistributionCard);

        loanTypeDistributionReport.setOnClickListener(view ->
                startActivity(new Intent(AdminReportActivity.this, AdminLoanTypeDistributionActivity.class)));


        loan_disbursment_report_card.setOnClickListener(view ->
                startActivity(new Intent(AdminReportActivity.this, AdminDisbursedLoansReport.class)));


        loan_approval_report_card.setOnClickListener(view ->
                startActivity(new Intent(AdminReportActivity.this, AdminLoanApprovalStatistics.class))
                );


    }
}