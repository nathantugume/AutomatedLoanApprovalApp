package com.example.automatedloanapprovalapp.ui.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.automatedloanapprovalapp.R;
import com.example.automatedloanapprovalapp.fragments.NotificationFragment;
import com.example.automatedloanapprovalapp.ui.loanofficer.ManageLoanActivity;
import com.example.automatedloanapprovalapp.ui.loanofficer.TransactionActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

public class AdminDashboardActivity extends AppCompatActivity {
    private MaterialToolbar topAppBar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private NotificationFragment notificationFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        topAppBar = findViewById(R.id.topAppBar);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);

        topAppBar.setNavigationOnClickListener(view -> drawerLayout.open());
        topAppBar.setTitle("Admin Dashboard");
        topAppBar.setTitleCentered(true);

        topAppBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.account){
                startActivity(new Intent(AdminDashboardActivity.this, UserActivity.class));
                return true;
            }
            return false;
        });
        CardView userCard = findViewById(R.id.users_card);
        CardView transaction_card = findViewById(R.id.admin_transaction_card);
        CardView loan_management_card = findViewById(R.id.loan_mgt);
        CardView adminReport = findViewById(R.id.adminReport);

        adminReport.setOnClickListener(view ->
                startActivity(new Intent(AdminDashboardActivity.this,AdminReportActivity.class)));


        userCard.setOnClickListener(view -> {
            Intent intent = new Intent(AdminDashboardActivity.this, UserActivity.class);
            startActivity(intent);
        });

        transaction_card.setOnClickListener(view -> {
            Intent intent = new Intent(AdminDashboardActivity.this, AdminTransactionActivity.class );
            startActivity(intent);
        });

        loan_management_card.setOnClickListener(view -> {
            Intent intent = new Intent(AdminDashboardActivity.this, AdminManageLoanActivity.class);
            startActivity(intent);
        });

        // Find the NotificationFragment and add it to the ActionBar
        notificationFragment = new NotificationFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.notification_layout, notificationFragment).commit();


    }

    }
