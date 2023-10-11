package com.example.automatedloanapprovalapp.ui.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.automatedloanapprovalapp.R;
import com.example.automatedloanapprovalapp.classes.FirestoreCRUD;
import com.example.automatedloanapprovalapp.classes.PersonalInformation;
import com.example.automatedloanapprovalapp.classes.UserManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

public class CustomerDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);

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

        FirestoreCRUD firestoreCRUD = new FirestoreCRUD();
        UserManager userManager = new UserManager(CustomerDetailsActivity.this);

        String uid = userManager.getCurrentUser().getUid();

     firestoreCRUD.readDocument("customer_details", uid, new OnCompleteListener<DocumentSnapshot>() {
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
                 monthlyIncomeTextView.setText(String.valueOf(information.getMonthlyIncome()));
                 nationalIDTextView.setText(information.getNationalID());

             }else
             {
                 Log.d("CustomerDetails",task.getException().getMessage());
             }




         }
     });
    }
}