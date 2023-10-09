package com.example.automatedloanapprovalapp.ui.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.automatedloanapprovalapp.R;
import com.example.automatedloanapprovalapp.classes.FirestoreCRUD;
import com.example.automatedloanapprovalapp.classes.UserManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ApplicationStatusActivity extends AppCompatActivity {

    private TextView pendingDateTxt,approvedEmailTxt,dateApprovedTxt,disbursedAmountTxt,dateDisbursed,partialBalanceTxt,completeBalanceTxt,completeDateTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_status);

        pendingDateTxt = findViewById(R.id.pendingDate);
        approvedEmailTxt = findViewById(R.id.approved_email);
        dateApprovedTxt = findViewById(R.id.dateApproved);
        disbursedAmountTxt = findViewById(R.id.txt_disbursed_amount);
        dateDisbursed = findViewById(R.id.dateDisbursed);
        partialBalanceTxt = findViewById(R.id.txtPartialBalance);
        completeBalanceTxt = findViewById(R.id.txtCompleteAmount);
        completeDateTxt = findViewById(R.id.dateComplete);


        UserManager userManager = new UserManager(this);
        String uid = userManager.getCurrentUser().getUid();

        FirestoreCRUD firestoreCRUD = new FirestoreCRUD();
        firestoreCRUD.queryDocuments("transaction", "userId", uid, new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot querySnapshot = task.getResult().getDocuments().get(0);
                    String status = querySnapshot.getString("status");
                    String approvedBy = querySnapshot.getString("approvedBy");
                    String dateApproved = querySnapshot.getString("dateRequested");

                }
            }
        });

    }
}