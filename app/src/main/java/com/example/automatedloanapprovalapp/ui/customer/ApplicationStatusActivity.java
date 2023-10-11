package com.example.automatedloanapprovalapp.ui.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.automatedloanapprovalapp.R;
import com.example.automatedloanapprovalapp.classes.FirestoreCRUD;
import com.example.automatedloanapprovalapp.classes.Transaction;
import com.example.automatedloanapprovalapp.classes.UserManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ApplicationStatusActivity extends AppCompatActivity {

    private TextView pendingDateTxt, approvedEmailTxt, dateApprovedTxt, disbursedAmountTxt,
            dateDisbursed, partialBalanceTxt, completeBalanceTxt, completeDateTxt,lastPaidTxt;

    private CardView approvedCard, disbursedCard, partialPaymentCard,completedBalanceCard;

    private FirestoreCRUD firestoreCRUD = new FirestoreCRUD();
    private int amount;
    private int repayAmount;


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
        approvedCard = findViewById(R.id.approvedCard);
        disbursedCard = findViewById(R.id.disbursedCard);
        partialPaymentCard = findViewById(R.id.partialPaymentCard);
        completedBalanceCard = findViewById(R.id.paymentCompleteCard);
        lastPaidTxt = findViewById(R.id.lastPayDate);


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
                    String dateApproved = querySnapshot.getString("approvedDate");
                    String requestedDate = querySnapshot.getString("dateRequested");
                    String repaymentDate = querySnapshot.getString("repaymentDate");
                    amount = Math.toIntExact(querySnapshot.getLong("requestedAmount"));
                    repayAmount = Math.toIntExact(querySnapshot.getLong("paybackAmount"));
                    String disbursedDate = querySnapshot.getString("disbursedDate");

                    pendingDateTxt.setText(requestedDate);
                    try {
                        assert approvedBy != null;
                        if (!approvedBy.isEmpty()) {
                            firestoreCRUD.readDocument("users", approvedBy, new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot snapshot = task.getResult();
                                        String username = snapshot.getString("username");
                                        try {
                                            approvedCard.setVisibility(View.VISIBLE);
                                            approvedEmailTxt.setText(username);
                                            dateApprovedTxt.setText(dateApproved);
                                        }catch (Exception exception){
                                            Log.d("ApprovedStatus",exception.getMessage());
                                        }



                                    }
                                }
                            });

                        }
                    }catch (Exception e){
                        Log.d("ApprovedStatus",e.getMessage());
                    }

                    try {
                        assert disbursedDate != null;
                        if (!disbursedDate.isEmpty()) {
                            disbursedCard.setVisibility(View.VISIBLE);
                            disbursedAmountTxt.setText(String.valueOf(amount));
                            dateDisbursed.setText(disbursedDate);
                        }
                    } catch (Exception e) {
                        Log.d("disbursedStatus", e.getMessage());
                    }

                    try {
                        assert repaymentDate != null;
                        if (!repaymentDate.isEmpty()){
                            partialPaymentCard.setVisibility(View.VISIBLE);
                            partialBalanceTxt.setText(String.valueOf(repayAmount));
                            lastPaidTxt.setText(repaymentDate);
                        }
                    }catch (Exception e){
                        Log.d("repayment", e.getMessage());
                    }

                    try {
                        {
                            assert status != null;
                            if (status.equals("fully_paid")){
                                completedBalanceCard.setVisibility(View.VISIBLE);
                                completeBalanceTxt.setText(String.valueOf(repayAmount));
                                completeDateTxt.setText(repaymentDate);
                            }
                        }
                    }catch (Exception e){

                        Log.d("RepaymentCard",e.getMessage());
                    }


                }
            }
        });

    }
}