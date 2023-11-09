package com.example.automatedloanapprovalapp.ui.customer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.automatedloanapprovalapp.R;
import com.example.automatedloanapprovalapp.classes.FirestoreCRUD;
import com.example.automatedloanapprovalapp.classes.UserManager;
import com.example.automatedloanapprovalapp.fragments.NotificationFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class CustomerDashboardActivity extends AppCompatActivity {

    private NotificationFragment notificationFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard);


        NavigationView navigationView = findViewById(R.id.navigationView);
        View headerView = navigationView.getHeaderView(0);

        TextView customerNAME = headerView.findViewById(R.id.customerNameNavigation);

        FirestoreCRUD firestoreCRUD = new FirestoreCRUD();
        UserManager userManager = new UserManager(this);

        String uid = userManager.getCurrentUser().getUid();

        firestoreCRUD.readDocument("users", uid, task -> {
            String userName = task.getResult().getString("username");

            assert userName != null;
            customerNAME.setText(userName.toUpperCase());
        });

        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.checkEligibilityNav){
                Intent intent = new Intent(CustomerDashboardActivity.this,EligibilityCheckActivity.class);
                startActivity(intent);
                return true;
            }
            if (item.getItemId() == R.id.navApplicationStatus){
                Intent intent = new Intent(CustomerDashboardActivity.this,ApplicationStatusActivity.class);
                startActivity(intent);
                return true;
            }

            if (item.getItemId() == R.id.navSignOut){
                userManager.signOutUser();
                return true;
            }

            return false;
        });

        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        toolbar.setNavigationOnClickListener(view -> drawerLayout.open());

        toolbar.setOnMenuItemClickListener(item -> {

            if (item.getItemId() == R.id.account){
                Intent intent = new Intent(CustomerDashboardActivity.this, CustomerDetailsActivity.class);
                startActivity(intent);
            }
            return false;
        });
        CardView loan_application = findViewById(R.id.loan_application);
        CardView application_status = findViewById(R.id.application_status_card);
        CardView eligibility_checkCard = findViewById(R.id.eligibility_checkCard);
        CardView repaymentCard = findViewById(R.id.loan_repayment_card);
        CardView transactionHistoryCard = findViewById(R.id.transactionHistoryCard);
        CardView customerAccountCard = findViewById(R.id.customerAccountCard);

        customerAccountCard.setOnClickListener(view -> {
            Intent intent = new Intent(CustomerDashboardActivity.this, CustomerDetailsActivity.class);
            startActivity(intent);
        });
        transactionHistoryCard.setOnClickListener(view -> {
            Intent intent = new Intent(CustomerDashboardActivity.this,TransactionHistoryActivity.class);
            startActivity(intent);
        });
        repaymentCard.setOnClickListener(view -> {
            Intent intent = new Intent(CustomerDashboardActivity.this,RepaymentActivity.class);
            startActivity(intent);
        });
        application_status.setOnClickListener(view -> {
            Intent intent = new Intent(CustomerDashboardActivity.this, ApplicationStatusActivity.class);
            startActivity(intent);
        });
        loan_application.setOnClickListener(view -> {
            // Handle loan application button click here
            // For example, start a new activity for loan application form
            Intent intent = new Intent(CustomerDashboardActivity.this, LoanApplicationActivity.class);
            startActivity(intent);
        });
        eligibility_checkCard.setOnClickListener(view -> {            Intent intent = new Intent(CustomerDashboardActivity.this, EligibilityCheckActivity.class);
            startActivity(intent);
        });

        // Find the NotificationFragment and add it to the ActionBar
        notificationFragment = new NotificationFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.notification_layout, notificationFragment).commit();

    }








}
