package com.example.automatedloanapprovalapp.ui.loanofficer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.automatedloanapprovalapp.R;
import com.example.automatedloanapprovalapp.ui.loanofficer.Reports.DisbursedLoansReport;
import com.example.automatedloanapprovalapp.ui.loanofficer.Reports.LoanApprovalStatistics;
import com.example.automatedloanapprovalapp.ui.loanofficer.Reports.LoanTypeDistributionActivity;
import com.example.automatedloanapprovalapp.ui.loanofficer.Reports.ProfitAndLossAnalysisActivity;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class OfficerReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_officer_report);

        CardView loanApprovalCard = findViewById(R.id.loan_approval_card);
        CardView disbursedLoansCard = findViewById(R.id.disbursedLoansReportCard);
        CardView loanTypeDistribution = findViewById(R.id.loanTypeDistributionCard);
        CardView profitAndLossCard = findViewById(R.id.profit_and_loss_card);

        profitAndLossCard.setOnClickListener(view ->
                startActivity(new Intent(OfficerReportActivity.this, ProfitAndLossAnalysisActivity.class)));

        loanTypeDistribution.setOnClickListener(view ->
                startActivity(new Intent(OfficerReportActivity.this, LoanTypeDistributionActivity.class)));

        disbursedLoansCard.setOnClickListener(view ->
                startActivity(new Intent(OfficerReportActivity.this, DisbursedLoansReport.class)));

        loanApprovalCard.setOnClickListener(view -> {
            startActivity(new Intent(OfficerReportActivity.this, LoanApprovalStatistics.class));
        });



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