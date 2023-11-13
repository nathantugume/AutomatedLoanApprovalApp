package com.example.automatedloanapprovalapp.ui.loanofficer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.automatedloanapprovalapp.R;
import com.example.automatedloanapprovalapp.classes.FirestoreCRUD;
import com.example.automatedloanapprovalapp.classes.UserManager;
import com.example.automatedloanapprovalapp.fragments.NotificationFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationView;

public class OfficerDashboardActivity extends AppCompatActivity {
    private MaterialToolbar topAppBar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private Intent intent;
    private NotificationFragment notificationFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_officer_dashboard);
        FirestoreCRUD firestoreCRUD = new FirestoreCRUD();
        UserManager userManager = new UserManager(this);

        topAppBar = findViewById(R.id.topAppBar);
        drawerLayout = findViewById(R.id.drawerLayout);

        topAppBar.setOnMenuItemClickListener(item ->
        {
            if (item.getItemId() == R.id.account){
                Toast.makeText(OfficerDashboardActivity.this, "working perfectly", Toast.LENGTH_SHORT).show();
            return true;
            }
            return false;
        });

        NavigationView navigationView = findViewById(R.id.navigationView);
        View headerView = navigationView.getHeaderView(0);

        TextView officerNAME = headerView.findViewById(R.id.officerNameNavigation);

        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_manage_loan){
                newActivity(ManageLoanActivity.class);
                return true;
            }
            if (item.getItemId() == R.id.navTransactions){
                newActivity(TransactionActivity.class);
                return true;
            }
            if (item.getItemId() == R.id.navReports){
                newActivity(OfficerReportActivity.class);
                return true;
            }
            if (item.getItemId() == R.id.navManageAccount){
                newActivity(ManageAccountActivity.class);
                return true;
            }
            if (item.getItemId() == R.id.navSignOut){
                userManager.signOutUser();
                return true;
            }
            return false;
        });



        String uid = userManager.getCurrentUser().getUid();

        firestoreCRUD.readDocument("users", uid, task -> {
            String userName = task.getResult().getString("username");
            officerNAME.setText(userName.toUpperCase());
        });

        CardView manage_loan_card = findViewById(R.id.manage_loan);
        CardView manage_applications = findViewById(R.id.manage_applications);
        CardView monitor_repayment_card = findViewById(R.id.monitor_repayment_card);
        CardView transactionCard = findViewById(R.id.transactions_card);
        CardView manage_account_card = findViewById(R.id.manage_account_card);
        CardView reportCard = findViewById(R.id.reportCard);

        // Find the NotificationFragment and add it to the ActionBar
        notificationFragment = new NotificationFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.notification_layout, notificationFragment).commit();

        reportCard.setOnClickListener(view -> newActivity(OfficerReportActivity.class));
        manage_account_card.setOnClickListener(view -> newActivity(ManageAccountActivity.class));

        transactionCard.setOnClickListener(view -> newActivity(TransactionActivity.class));

        monitor_repayment_card.setOnClickListener(view -> newActivity(MonitorRepaymentActivity.class));

        manage_applications.setOnClickListener(view -> newActivity(ManageApplicationActivity.class));

        manage_loan_card.setOnClickListener(view -> newActivity(ManageLoanActivity.class));


        topAppBar.setNavigationOnClickListener(view -> drawerLayout.open());

    }

    public void newActivity(Class<?> destinationActivity) {
        intent = new Intent(OfficerDashboardActivity.this, destinationActivity);
        startActivity(intent);
    }

}
