package com.example.automatedloanapprovalapp.ui.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.automatedloanapprovalapp.R;
import com.example.automatedloanapprovalapp.classes.FirestoreCRUD;
import com.example.automatedloanapprovalapp.classes.Transaction;
import com.example.automatedloanapprovalapp.classes.UserManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
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

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        BottomNavigationView bottomNavigationView  = findViewById(R.id.bottom_navigationView);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.bottom_menu_home){
                    Intent intent = new Intent(ApplicationStatusActivity.this, CustomerDashboardActivity.class);
                    startActivity(intent);
                    return true;
                }
                if (item.getItemId() == R.id.bottom_menu_account){
                    Intent intent = new Intent(ApplicationStatusActivity.this, CustomerDetailsActivity.class);
                    startActivity(intent);
                    return true;
                }
                if (item.getItemId() == R.id.bottom_menu_repay){
                    Intent intent = new Intent(ApplicationStatusActivity.this, RepaymentActivity.class);
                    startActivity(intent);
                    return true;
                }
                if (item.getItemId() == R.id.bottom_menu_status){
                    Intent intent = new Intent(ApplicationStatusActivity.this, ApplicationStatusActivity.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });


        UserManager userManager = new UserManager(this);
        String uid = userManager.getCurrentUser().getUid();

        FirestoreCRUD firestoreCRUD = new FirestoreCRUD();
        firestoreCRUD.queryDocuments("transaction", "userId", uid, task -> {
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
                    if (!approvedBy.isEmpty()) {
                        firestoreCRUD.readDocument("users", approvedBy, task1 -> {
                            if (task1.isSuccessful()) {
                                DocumentSnapshot snapshot = task1.getResult();
                                String username = snapshot.getString("username");
                                try {
                                    approvedCard.setVisibility(View.VISIBLE);
                                    approvedEmailTxt.setText(username);
                                    dateApprovedTxt.setText(dateApproved);
                                }catch (Exception exception){
                                    Log.d("ApprovedStatus",exception.getMessage());
                                }



                            }
                        });

                    }
                }catch (Exception e){
                    Log.d("ApprovedStatus",e.getMessage());
                }

                try {

                    if (!disbursedDate.isEmpty()) {
                        disbursedCard.setVisibility(View.VISIBLE);
                        disbursedAmountTxt.setText(String.valueOf(amount));
                        dateDisbursed.setText(disbursedDate);
                    }
                } catch (Exception e) {
                    Log.d("disbursedStatus", e.getMessage());
                }

                try {

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
        });

    }
}