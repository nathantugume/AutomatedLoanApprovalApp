package com.example.automatedloanapprovalapp.ui.registration;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.automatedloanapprovalapp.R;
import com.example.automatedloanapprovalapp.classes.FirestoreCRUD;
import com.example.automatedloanapprovalapp.classes.RegistrationData;
import com.example.automatedloanapprovalapp.classes.User;
import com.example.automatedloanapprovalapp.classes.UserManager;
import com.example.automatedloanapprovalapp.ui.login.LogInActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class EmploymentDetailsActivity extends AppCompatActivity {

    private TextInputLayout professionLayout,passwordLayout, incomeLayout, dependantsLayout, businessTypeLayout, businessLocationLayout, businessRevenueLayout;
    private EditText professionEditText,passwordEditText, incomeEditText, dependantsEditText, businessTypeEditText, businessLocationEditText, businessRevenueEditText;
    private Button btnNextEmployment;
    private String profession,  dependants, businessType, businessLocation, businessRevenue,password;
    private Double income;
    private FirestoreCRUD firestoreCRUD = new FirestoreCRUD();
    private UserManager userManager = new UserManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employment_details);

        professionLayout = findViewById(R.id.etProfession);
        incomeLayout = findViewById(R.id.etIncome);
        dependantsLayout = findViewById(R.id.etDependants);
        businessTypeLayout = findViewById(R.id.etBusinessType);
        businessLocationLayout = findViewById(R.id.etBusinessLocation);
        businessRevenueLayout = findViewById(R.id.etBusinessRevenue);
        passwordLayout = findViewById(R.id.etPassword);


        professionEditText = professionLayout.getEditText();
        incomeEditText = incomeLayout.getEditText();
        dependantsEditText = dependantsLayout.getEditText();
        businessTypeEditText = businessTypeLayout.getEditText();
        businessLocationEditText = businessLocationLayout.getEditText();
        businessRevenueEditText = businessRevenueLayout.getEditText();
        passwordEditText = passwordLayout.getEditText();


        btnNextEmployment = findViewById(R.id.btnFinish);

        btnNextEmployment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateInput()) {
                    // Data is valid, proceed to the next step or save it
                    saveData();
                    Toast.makeText(EmploymentDetailsActivity.this, "Data is valid. Proceed to the next step.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validateInput() {
        boolean isValid = true;

         profession = professionEditText.getText().toString().trim();
         income = Double.valueOf(incomeEditText.getText().toString().trim());
         dependants = dependantsEditText.getText().toString().trim();
         businessType = businessTypeEditText.getText().toString().trim();
         businessLocation = businessLocationEditText.getText().toString().trim();
         businessRevenue = businessRevenueEditText.getText().toString().trim();
         password = passwordEditText.getText().toString().trim();


        if (TextUtils.isEmpty(profession)) {
            professionLayout.setError("Profession is required");
            isValid = false;
        } else {
            professionLayout.setError(null);
        }

        if (income.isNaN()) {
            incomeLayout.setError("please enter a number");
            isValid = false;
        } else {
            incomeLayout.setError(null);
        }

        if (TextUtils.isEmpty(dependants)) {
            dependantsLayout.setError("Number of dependants is required");
            isValid = false;
        } else {
            dependantsLayout.setError(null);
        }

        if (TextUtils.isEmpty(businessType)) {
            businessTypeLayout.setError("Business type is required");
            isValid = false;
        } else {
            businessTypeLayout.setError(null);
        }

        if (TextUtils.isEmpty(businessLocation)) {
            businessLocationLayout.setError("Business location is required");
            isValid = false;
        } else {
            businessLocationLayout.setError(null);
        }

        if (TextUtils.isEmpty(businessRevenue)) {
            businessRevenueLayout.setError("Business revenue is required");
            isValid = false;
        } if (TextUtils.isEmpty(password)){
            passwordLayout.setError("Please create a password");
        }
        else {
            businessRevenueLayout.setError(null);
        }

        return isValid;
    }


    private void saveData(){
        RegistrationData registrationData = new RegistrationData();
        Intent intent = getIntent();

// Retrieve each item individually
        String fullName = intent.getStringExtra("fullName");
        String email = intent.getStringExtra("email");
        String dateOfBirth = intent.getStringExtra("dateOfBirth");
        String nationalID = intent.getStringExtra("nationalID");
        String address = intent.getStringExtra("address");
        String phoneNumber = intent.getStringExtra("phoneNumber");
        String gender = intent.getStringExtra("selectedGender");

// Now you have access to each individual item
        registrationData.setJobTitle(profession);
        registrationData.setMonthlyIncome(income);
        registrationData.setNumberOfDependents(Integer.parseInt(dependants));
        registrationData.setBusinessType(businessType);
        registrationData.setBusinessLocation(businessLocation);
        registrationData.setBusinessRevenue(Double.parseDouble(businessRevenue));
        registrationData.setFullName(fullName);
        registrationData.setEmail(email);
        registrationData.setDateOfBirth(dateOfBirth);
        registrationData.setNationalID(nationalID);
        registrationData.setAddress(address);
        registrationData.setPhoneNumber(phoneNumber);
        registrationData.setGender(gender);
        registrationData.setPasswordResetRequired(true);


        userManager.signUpUser(email, password, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // User account created successfully
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String userId = user.getUid();
                        // Now you have the user's UID (User ID)
                        User customer = new User(fullName,email,password,"customer");
                        firestoreCRUD.createDocumentWithId("users", userId, customer, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(EmploymentDetailsActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();

                                }else{
                                    Toast.makeText(EmploymentDetailsActivity.this, "error: "+ Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                        // You can pass the user's UID to your Firestore CRUD method or perform any other actions with it.
                        firestoreCRUD.createDocumentWithId("customer_details", userId, registrationData, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(EmploymentDetailsActivity.this, "Account Details saved successfully", Toast.LENGTH_SHORT).show();
                           Intent intent = new Intent(EmploymentDetailsActivity.this, LogInActivity.class);
                            startActivity(intent);
                            }
                        });
                    } else {
                        // Handle the case where user is null
                        Toast.makeText(EmploymentDetailsActivity.this, "User is null", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Handle the case where account creation failed
                    Toast.makeText(EmploymentDetailsActivity.this, "User account creation failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
   }
}
