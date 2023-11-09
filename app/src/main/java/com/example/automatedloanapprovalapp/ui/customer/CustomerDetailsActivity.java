package com.example.automatedloanapprovalapp.ui.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.automatedloanapprovalapp.R;
import com.example.automatedloanapprovalapp.classes.FirestoreCRUD;
import com.example.automatedloanapprovalapp.classes.PersonalInformation;
import com.example.automatedloanapprovalapp.classes.User;
import com.example.automatedloanapprovalapp.classes.UserManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.NumberFormat;
import java.util.Locale;

public class CustomerDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        BottomNavigationView bottomNavigationView  = findViewById(R.id.bottom_navigationView);

        bottomNavigationView.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.bottom_menu_home){
                Intent intent = new Intent(CustomerDetailsActivity.this, CustomerDashboardActivity.class);
                startActivity(intent);
                return true;
            }
            if (item.getItemId() == R.id.bottom_menu_account){
                Intent intent = new Intent(CustomerDetailsActivity.this, CustomerDetailsActivity.class);
                startActivity(intent);
                return true;
            }
            if (item.getItemId() == R.id.bottom_menu_repay){
                Intent intent = new Intent(CustomerDetailsActivity.this, RepaymentActivity.class);
                startActivity(intent);
                return true;
            }
            if (item.getItemId() == R.id.bottom_menu_status){
                Intent intent = new Intent(CustomerDetailsActivity.this, CustomerDetailsActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });


        TextView fullNameTextView = findViewById(R.id.fullNameTextView);
        TextView emailTextView = findViewById(R.id.emailTextView);
        TextView phoneNumberTextView = findViewById(R.id.phoneNumberTextView);
        TextView addressTextView = findViewById(R.id.addressTextView);
        TextView businessNameTextView = findViewById(R.id.businessNameTextView);
        TextView businessTypeTextView = findViewById(R.id.businessTypeTextView);
        TextView genderTextView = findViewById(R.id.genderTextView);
        TextView jobTitleTextView = findViewById(R.id.jobTitleTextView);
        TextView monthlyIncomeTextView = findViewById(R.id.monthlyIncomeTextView);
        TextView nationalIDTextView = findViewById(R.id.nationalIDTextView);
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        Intent intent = getIntent();
        FirestoreCRUD firestoreCRUD = new FirestoreCRUD();
        UserManager userManager = new UserManager(CustomerDetailsActivity.this);
        if (intent != null && intent.hasExtra("USER_DETAILS")) {
            User user = intent.getParcelableExtra("USER_DETAILS");
            firestoreCRUD.readDocument("customer_details", user.getUserId(), new OnCompleteListener<DocumentSnapshot>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        DocumentSnapshot documentSnapshot = task.getResult();
                        PersonalInformation information = documentSnapshot.toObject(PersonalInformation.class);

                        fullNameTextView.setText(information.getFullName());
                        emailTextView.setText(information.getEmail());
                        phoneNumberTextView.setText(information.getPhoneNumber());
                        addressTextView.setText(information.getAddress());
                        businessNameTextView.setText(information.getBusinessName());
                        businessTypeTextView.setText(information.getBusinessType());
                        genderTextView.setText(information.getGender());
                        jobTitleTextView.setText(information.getJobTitle());
                        monthlyIncomeTextView.setText("Ugx "+ numberFormat.format(information.getMonthlyIncome()));
                        nationalIDTextView.setText(information.getNationalID());

                    }else
                    {
                        Log.d("CustomerDetails",task.getException().getMessage());
                    }




                }
            });


        }else {

            String uid = userManager.getCurrentUser().getUid();

            firestoreCRUD.readDocument("customer_details", uid, new OnCompleteListener<DocumentSnapshot>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        DocumentSnapshot documentSnapshot = task.getResult();
                        PersonalInformation information = documentSnapshot.toObject(PersonalInformation.class);

                        fullNameTextView.setText(information.getFullName());
                        emailTextView.setText(information.getEmail());
                        phoneNumberTextView.setText(information.getPhoneNumber());
                        addressTextView.setText(information.getAddress());
                        businessNameTextView.setText(information.getBusinessName());
                        businessTypeTextView.setText(information.getBusinessType());
                        genderTextView.setText(information.getGender());
                        jobTitleTextView.setText(information.getJobTitle());
                        monthlyIncomeTextView.setText("Ugx "+ numberFormat.format(information.getMonthlyIncome()));
                        nationalIDTextView.setText(information.getNationalID());

                    }else
                    {
                        Log.d("CustomerDetails",task.getException().getMessage());
                    }




                }
            });
        }


    }
}