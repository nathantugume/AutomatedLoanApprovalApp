package com.example.automatedloanapprovalapp.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.automatedloanapprovalapp.R;
import com.example.automatedloanapprovalapp.classes.FirestoreCRUD;
import com.example.automatedloanapprovalapp.classes.UserManager;
import com.example.automatedloanapprovalapp.ui.admin.AdminDashboardActivity;
import com.example.automatedloanapprovalapp.ui.customer.CustomerDashboardActivity;
import com.example.automatedloanapprovalapp.ui.loanofficer.OfficerDashboardActivity;
import com.example.automatedloanapprovalapp.ui.registration.PersonalInfoActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Objects;

public class LogInActivity extends AppCompatActivity {
    private TextInputEditText etEmail;
    private TextInputEditText etPassword;
    private Button btnSignIn;
    private TextView resetPasswordText;
    private TextView createAccountText;

    private UserManager userManager = new UserManager(this);
    private FirestoreCRUD firestoreCRUD = new FirestoreCRUD();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        FirebaseAnalytics.getInstance(this).setAnalyticsCollectionEnabled(true);
        FirebaseAnalytics.getInstance(this).setUserProperty("debug", "true");

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        resetPasswordText = findViewById(R.id.resetPasswordText);
        createAccountText = findViewById(R.id.createAccountText);

        btnSignIn.setOnClickListener(view -> {
            // Validate email and password here
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // Show progress dialog
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Signing In...");
            progressDialog.show();
            userManager.signInUser(email, password, task -> {
                if (task.isSuccessful()){
                    String uid = null;
                    if (!userManager.getCurrentUser().getUid().isEmpty()){
                        uid  = userManager.getCurrentUser().getUid();
                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(LogInActivity.this, "login failed please try again!!", Toast.LENGTH_SHORT).show();
                        userManager.signOutUser();
                    }
                    firestoreCRUD.readDocument("users", uid, task1 -> {
                        DocumentSnapshot documentSnapshot = task1.getResult();
                        String role = documentSnapshot.getString("role");


                        try {
                            switch (role){
                                case "default":
                                case  "admin":
                                    progressDialog.dismiss();
                                    Intent intent = new Intent(LogInActivity.this, AdminDashboardActivity.class);
                                    startActivity(intent);
                                    break;
                                case "loan_manager":
                                    progressDialog.dismiss();
                                    intent = new Intent(LogInActivity.this, OfficerDashboardActivity.class);
                                    startActivity(intent);
                                    break;
                                case "customer":
                                    progressDialog.dismiss();
                                    intent = new Intent(LogInActivity.this, CustomerDashboardActivity.class);
                                    startActivity(intent);
                                    break;
                                default:
                                    progressDialog.dismiss();
                                    Toast.makeText(LogInActivity.this, "Invalid user role please contact Admin", Toast.LENGTH_LONG).show();

                            }
                        }catch (Exception e){
                            progressDialog.dismiss();
                            Log.d("error",e.getMessage());
                        }

                    });
                }
                else {
                    progressDialog.dismiss();
                    task.addOnFailureListener(e -> Toast.makeText(LogInActivity.this, e.getMessage(), Toast.LENGTH_LONG).show());
                }



            });
        });

        resetPasswordText.setOnClickListener(view -> {
            Intent intent = new Intent(LogInActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });

        createAccountText.setOnClickListener(view -> {
            // Implement the logic for creating a new account here

             Intent registrationIntent = new Intent(LogInActivity.this, PersonalInfoActivity.class);
             startActivity(registrationIntent);

        });

    }

    // Validation methods
//    private boolean isValidEmail(String email) {
//        // Implement your email validation logic here
//        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
//    }
//
//    private boolean isValidPassword(String password) {
//        // Implement your password validation logic here (e.g., minimum length)
//        return password.length() >= 6;
//    }
}