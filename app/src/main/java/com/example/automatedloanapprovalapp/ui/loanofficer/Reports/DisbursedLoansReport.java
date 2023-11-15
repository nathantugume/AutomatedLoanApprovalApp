package com.example.automatedloanapprovalapp.ui.loanofficer.Reports;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.automatedloanapprovalapp.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DisbursedLoansReport extends AppCompatActivity {

    private BarChart barChart;

    private FirebaseFirestore firestore;



    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disbursed_loans_report);

        barChart = findViewById(R.id.barChart);

        firestore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);


        fetchDataAndDisplayCharts();
    }




    private void fetchDataAndDisplayCharts() {
        // Fetch data from Firestore based on selectedYear and selectedMonth
        // Here, you should query your Firestore database for the relevant data.
        // For simplicity, I'm using a dummy list of disbursed amounts.

        firestore.collection("transaction")
                .whereNotEqualTo("status", "pending")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);
                        Map<Integer, Float> monthlyTotals = new HashMap<>();

                        for (DocumentSnapshot document : task.getResult()) {
                            double disbursedAmount = document.getDouble("requestedAmount");
                            String dateString = document.getString("approvedDate");
                            int monthNumber = getMonthNumberFromString(dateString);

                            // Update the total for the corresponding month
                            if (monthlyTotals.containsKey(monthNumber)) {
                                float currentTotal = monthlyTotals.get(monthNumber);
                                monthlyTotals.put(monthNumber, currentTotal + (float) disbursedAmount);
                            } else {
                                monthlyTotals.put(monthNumber, (float) disbursedAmount);
                            }
                        }

                        // Convert the Map to ArrayList<BarEntry> for chart display
                        ArrayList<BarEntry> entries = new ArrayList<>();
                        for (Map.Entry<Integer, Float> entry : monthlyTotals.entrySet()) {
                            entries.add(new BarEntry(entry.getKey(), entry.getValue()));
                        }

                        displayBarChart(entries);
                    } else {
                        // Handle errors
                        Log.d("Charts", task.getException().getMessage());
                    }
                });


    }

    private int getMonthNumberFromString(String dateString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = sdf.parse(dateString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar.get(Calendar.MONTH) + 1; // Calendar.MONTH is zero-based, so add 1
        } catch (ParseException e) {
            e.printStackTrace();
            return 0; // Handle parse exception
        }
    }

    private void displayBarChart(ArrayList<BarEntry> entries) {
        BarDataSet dataSet = new BarDataSet(entries, "Disbursed Amounts");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(12f);

        BarData data = new BarData(dataSet);
        data.setBarWidth(0.9f);

        barChart.setData(data);
        barChart.getDescription().setEnabled(false);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getXAxis().setValueFormatter(new MonthValueFormatter());
        barChart.getAxisRight().setEnabled(false);


        XAxis xAxis = barChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setLabelRotationAngle(10);  // Rotate labels for better readability

        barChart.invalidate();
    }

    private class MonthValueFormatter extends ValueFormatter {
        @Override
        public String getAxisLabel(float value, AxisBase axis) {
            // Display the month abbreviation as the label on the X-axis
            String[] months = new String[]{"", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
            int intValue = (int) value;
            if (intValue >= 1 && intValue <= 12) {
                return months[intValue];
            } else {
                return "";
            }
        }
    }
}