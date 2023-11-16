package com.example.automatedloanapprovalapp.ui.admin.Reports;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.automatedloanapprovalapp.R;
import com.example.automatedloanapprovalapp.classes.LoanTransaction;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class AdminProfitAndLossAnalysisActivity extends AppCompatActivity {
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profit_and_loss_analysis);


        firestore = FirebaseFirestore.getInstance();

        fetchDataAndCalculateProfits();
    }

    private void fetchDataAndCalculateProfits() {
        firestore.collection("transaction")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<LoanTransaction> transactions = new ArrayList<>();

                        for (DocumentSnapshot document : task.getResult()) {
                            String approvedDate = document.getString("approvedDate");
                            double requestedAmount = document.getDouble("requestedAmount");
                            double repaidAmount = document.getDouble("repaidAmount");
                            String status = document.getString("status");

                            LoanTransaction transaction = new LoanTransaction(approvedDate, requestedAmount, repaidAmount, status);
                            transactions.add(transaction);
                        }

                        // Calculate profits per month
                        Map<Integer, Double> monthlyProfits = calculateProfitsByMonth(transactions);

                        // Display profits in TextView
                        displayProfits(monthlyProfits);
                    } else {
                        // Handle errors
                        // You can add proper error handling here
                    }
                });
    }

    private Map<Integer, Double> calculateProfitsByMonth(List<LoanTransaction> transactions) {
        Map<Integer, Double> profitsByMonth = new TreeMap<>(); // Use TreeMap for sorting

        for (LoanTransaction transaction : transactions) {
            if ("fully_paid".equals(transaction.getStatus())) {
                try {
                    int monthNumber = transaction.getMonthNumber();
                    double profit = transaction.getRepaidAmount() - transaction.getRequestedAmount();

                    profitsByMonth.merge(monthNumber, profit, Double::sum);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        return profitsByMonth;
    }

    private void displayProfits(Map<Integer, Double> monthlyProfits) {
        TextView profitTextView = findViewById(R.id.profitTextView);

        StringBuilder displayText = new StringBuilder();
        displayText.append("Monthly Profits: ").append(monthlyProfits);

        profitTextView.setText(displayText.toString());
    }
}