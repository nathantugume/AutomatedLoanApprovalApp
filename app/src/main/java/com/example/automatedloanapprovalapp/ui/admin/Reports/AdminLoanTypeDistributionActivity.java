package com.example.automatedloanapprovalapp.ui.admin.Reports;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.automatedloanapprovalapp.R;
import com.example.automatedloanapprovalapp.ui.admin.AdminDashboardActivity;
import com.example.automatedloanapprovalapp.ui.admin.AdminManageLoanActivity;
import com.example.automatedloanapprovalapp.ui.admin.Reports.AdminLoanTypeDistributionActivity;
import com.example.automatedloanapprovalapp.ui.admin.AdminTransactionActivity;
import com.example.automatedloanapprovalapp.ui.admin.UserActivity;
import com.example.automatedloanapprovalapp.ui.loanofficer.ManageAccountActivity;
import com.example.automatedloanapprovalapp.ui.loanofficer.ManageLoanActivity;
import com.example.automatedloanapprovalapp.ui.loanofficer.OfficerDashboardActivity;
import com.example.automatedloanapprovalapp.ui.loanofficer.OfficerReportActivity;
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
import java.util.HashMap;
import java.util.Map;

public class AdminLoanTypeDistributionActivity extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private ProgressBar progressBar;
    private PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_type_distribution);

        firestore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);
        pieChart = findViewById(R.id.pieChart);

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
        toolbar.setTitle("Admin Reports");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.bottom_manager_home){
                startActivity(new Intent(AdminLoanTypeDistributionActivity.this, AdminDashboardActivity.class));
                return true;
            }
            if (item.getItemId() == R.id.bottom_reports){
                startActivity(new Intent(AdminLoanTypeDistributionActivity.this, AdminLoanTypeDistributionActivity.class));
                return true;
            }
            if (item.getItemId() == R.id.bottom_mng_loan){
                startActivity(new Intent(AdminLoanTypeDistributionActivity.this, AdminManageLoanActivity.class));
                return true;
            }
            if (item.getItemId() == R.id.bottom_transactions){
                startActivity(new Intent(AdminLoanTypeDistributionActivity.this, AdminTransactionActivity.class));
                return true;
            }
            if (item.getItemId() == R.id.bottom_accounts){
                startActivity(new Intent(AdminLoanTypeDistributionActivity.this, UserActivity.class));
                return true;
            }
            return false;
        });

        //fetch data from database and fill the piechart
        fetchLoanTypeDistribution();
    }
    private void fetchLoanTypeDistribution() {
        firestore.collection("transaction")
                .whereNotEqualTo("status", "pending")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Map<String, Integer> loanTypeCounts = new HashMap<>();

                        for (DocumentSnapshot document : task.getResult()) {
                            String loanType = document.getString("loanType").toLowerCase();

                            // Update the count for the corresponding loan type
                            if (loanTypeCounts.containsKey(loanType)) {
                                int currentCount = loanTypeCounts.get(loanType);
                                loanTypeCounts.put(loanType, currentCount + 1);
                            } else {
                                loanTypeCounts.put(loanType, 1);
                            }
                        }

                        // Convert the Map to ArrayList<PieEntry> for chart display
                        ArrayList<PieEntry> entries = new ArrayList<>();
                        for (Map.Entry<String, Integer> entry : loanTypeCounts.entrySet()) {
                            entries.add(new PieEntry(entry.getValue(), entry.getKey()));
                        }

                        displayPieChart(entries);
                    } else {
                        // Handle errors
                        Log.d("Charts", task.getException().getMessage());
                    }
                });
    }

    private void displayPieChart(ArrayList<PieEntry> entries) {
        PieChart pieChart = findViewById(R.id.pieChart); // Replace with your actual PieChart id
        PieDataSet dataSet = new PieDataSet(entries, "Loan Type Distribution");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextSize(14f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter(pieChart));

        pieChart.setData(data);
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);
        pieChart.invalidate();
        pieChart.setCenterText("Loan Type Distribution");
        pieChart.setUsePercentValues(true);
        pieChart.setCenterTextSize(14f);

    }
}