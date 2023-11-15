package com.example.automatedloanapprovalapp.ui.loanofficer.Reports;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.automatedloanapprovalapp.R;
import com.example.automatedloanapprovalapp.ui.loanofficer.ManageAccountActivity;
import com.example.automatedloanapprovalapp.ui.loanofficer.ManageLoanActivity;
import com.example.automatedloanapprovalapp.ui.loanofficer.OfficerDashboardActivity;
import com.example.automatedloanapprovalapp.ui.loanofficer.OfficerReportActivity;
import com.example.automatedloanapprovalapp.ui.loanofficer.Reports.LoanApprovalStatistics;
import com.example.automatedloanapprovalapp.ui.loanofficer.TransactionActivity;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class LoanApprovalStatistics extends AppCompatActivity {
    private FirebaseFirestore firestore;
    private ProgressBar progressBar;
    private PieChart pieChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_approval_statistics);

        firestore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);
        pieChart = findViewById(R.id.pieChart);


        fetchDataAndDisplayChart();

        //Top App Bar
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        // BottomAppBar
        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_manager_home) {
                startActivity(new Intent(LoanApprovalStatistics.this, OfficerDashboardActivity.class));
                return true;
            }
            if (item.getItemId() == R.id.bottom_reports) {
                item.setEnabled(true);
                startActivity(new Intent(LoanApprovalStatistics.this, OfficerReportActivity.class));
                return true;
            }
            if (item.getItemId() == R.id.bottom_mng_loan) {
                startActivity(new Intent(LoanApprovalStatistics.this, ManageLoanActivity.class))
                ;
                startActivity(new Intent(LoanApprovalStatistics.this, ManageLoanActivity.class));
                return true;
            }
            if (item.getItemId() == R.id.bottom_transactions) {
                startActivity(new Intent(LoanApprovalStatistics.this, TransactionActivity.class));
                return true;
            }
            if (item.getItemId() == R.id.bottom_accounts) {
                startActivity(new Intent(LoanApprovalStatistics.this, ManageAccountActivity.class));
                return true;
            }

            return false;
        });

    }

    private void fetchDataAndDisplayChart() {
        firestore.collection("transaction")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int approvedCount = 0;
                        int pendingCount = 0;
                        int disbursedCount = 0;

                        for (DocumentSnapshot document : task.getResult()) {
                            String status = document.getString("status");
                            if ("approved".equals(status)) {
                                approvedCount++;
                            } else if ("pending".equals(status)) {
                                pendingCount++;
                            } else if ("disbursed".equals(status)) {
                                disbursedCount++;
                            }
                        }

                        displayPieChart(approvedCount, pendingCount, disbursedCount);
                        progressBar.setVisibility(View.GONE);
                    } else {
                        // Handle errors
                        Log.d("Charts",task.getException().getMessage());
                    }
                });
    }

    private void displayPieChart(int approvedCount, int pendingCount, int disbursedCount) {


        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(approvedCount, "Approved"));
        entries.add(new PieEntry(pendingCount, "Pending"));
        entries.add(new PieEntry(disbursedCount, "Disbursed"));

        PieDataSet dataSet = new PieDataSet(entries, "Loan Approval");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextSize(12f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter(pieChart));

        pieChart.setData(data);
        pieChart.setCenterText("Loan Approval Statistics");
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(14f);
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);
        pieChart.invalidate();
    }
}