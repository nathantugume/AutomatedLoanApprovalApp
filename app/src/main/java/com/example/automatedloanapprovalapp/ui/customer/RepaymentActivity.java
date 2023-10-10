package com.example.automatedloanapprovalapp.ui.customer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.automatedloanapprovalapp.R;
import com.example.automatedloanapprovalapp.classes.Transaction;
import com.google.android.material.textfield.TextInputEditText;

public class RepaymentActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repayment);

        TextView loanBalanceTxt = findViewById(R.id.loanBalance);
        TextInputEditText payAmountEdt = findViewById(R.id.payAmount);
        Button payBtn = findViewById(R.id.payBtn);

        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Transaction transaction = new Transaction();
            }
        });
    }
}