package com.example.automatedloanapprovalapp.ui.admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.automatedloanapprovalapp.R;
import com.example.automatedloanapprovalapp.classes.FirestoreCRUD;
import com.example.automatedloanapprovalapp.classes.User;
import com.example.automatedloanapprovalapp.classes.UserManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;

public class UserActivity extends AppCompatActivity {
    private String selectedUserType;
    private TextInputEditText usernameEdt;
    private TextInputEditText emailEdt;
    private TextInputEditText edtPassword;
    private TextInputEditText confirmPassordEdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        findViewById(R.id.fabAddUser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a LayoutInflater to inflate the dialog_add_user.xml layout
                LayoutInflater inflater = LayoutInflater.from(UserActivity.this);
                View dialogView = inflater.inflate(R.layout.dialog_add_user, null);

                // Find TextInputEditText views inside dialogView
                usernameEdt = dialogView.findViewById(R.id.editTextUsername);
                emailEdt = dialogView.findViewById(R.id.editTextEmail);
                edtPassword = dialogView.findViewById(R.id.editTextPassword);
                confirmPassordEdt = dialogView.findViewById(R.id.editTextConfirmPassword);

                // Set up the Spinner with an ArrayAdapter
                Spinner userRoleSpinner = dialogView.findViewById(R.id.userRoleSpinner);
                setUpUserRoleSpinner(userRoleSpinner);

                // Build the AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(UserActivity.this);
                builder.setView(dialogView)
                        .setTitle("Add User")
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String usernameTxt = usernameEdt.getText().toString();
                                String emailTxt = emailEdt.getText().toString();
                                String passwordTxt = edtPassword.getText().toString();
                                String cPassword = confirmPassordEdt.getText().toString();

                                validateInputs(usernameTxt, emailTxt, passwordTxt, cPassword);

                                // Add your logic to save the user data
                                User user = new User(usernameTxt, emailTxt, passwordTxt, selectedUserType);

                                UserManager userManager = new UserManager(UserActivity.this);
                                userManager.signUpUser(emailTxt, passwordTxt, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            String uid = userManager.getCurrentUser().getUid();
                                            FirestoreCRUD firestoreCRUD = new FirestoreCRUD();
                                            firestoreCRUD.createDocumentWithId("users", uid, user, new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(UserActivity.this, "User saved Successfully!!", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(UserActivity.this, "Sorry we have failed to create a new User!!", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        } else {
                                            Toast.makeText(UserActivity.this, "User registration failed!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss(); // Dismiss the dialog
                            }
                        });

                // Create and show the dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void setUpUserRoleSpinner(Spinner spinner) {
        String[] userTypeList = {"customer", "loan_manager", "admin"};
        ArrayAdapter<String> userTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, userTypeList);
        userTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(userTypeAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedUserType = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Handle the case where nothing is selected (if needed)
            }
        });
    }

    private void validateInputs(String username, String email, String password, String confirm) {
        if (username.isEmpty()) {
            usernameEdt.setError("Username required");
            usernameEdt.requestFocus();
        } else if (email.isEmpty()) {
            emailEdt.setError("Email required");
            emailEdt.requestFocus();
        } else if (password.isEmpty()) {
            edtPassword.setError("Password cannot be empty");
            edtPassword.requestFocus();
        } else if (confirm.isEmpty()) {
            confirmPassordEdt.setError("Please confirm your password");
            confirmPassordEdt.requestFocus();
        }
    }
}
