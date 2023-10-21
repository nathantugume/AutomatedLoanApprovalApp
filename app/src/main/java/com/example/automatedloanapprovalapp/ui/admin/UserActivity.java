package com.example.automatedloanapprovalapp.ui.admin;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.automatedloanapprovalapp.R;
import com.example.automatedloanapprovalapp.adapters.AdminUserAdapter;
import com.example.automatedloanapprovalapp.adapters.UserAdapter;
import com.example.automatedloanapprovalapp.classes.FirestoreCRUD;
import com.example.automatedloanapprovalapp.classes.User;
import com.example.automatedloanapprovalapp.classes.UserManager;
import com.example.automatedloanapprovalapp.fragments.EditUserDialogFragment;
import com.example.automatedloanapprovalapp.ui.customer.CustomerDetailsActivity;
import com.example.automatedloanapprovalapp.ui.loanofficer.ManageAccountActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity {
    private String selectedUserType;
    private TextInputEditText usernameEdt;
    private TextInputEditText emailEdt;
    private TextInputEditText edtPassword;
    private TextInputEditText confirmPassordEdt;
    private RecyclerView recyclerView;
    private AdminUserAdapter adapter;

    private FirestoreCRUD firestoreCRUD = new FirestoreCRUD();

    @SuppressLint("NotifyDataSetChanged")
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
                        .setPositiveButton("Add", (dialogInterface, i) -> {
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
                        })
                        .setNegativeButton("Cancel", (dialogInterface, i) -> {
                            dialogInterface.dismiss(); // Dismiss the dialog
                        });

                // Create and show the dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Assume userList contains your User objects
        List<User> userList = new ArrayList<>();
        // Add User objects to the list

        adapter = new AdminUserAdapter(userList);
        recyclerView.setAdapter(adapter);


        firestoreCRUD.getAllDocuments("users",  new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (DocumentSnapshot document:task.getResult() ) {
                        User user = document.toObject(User.class);
                        assert user != null;
                        user.setUserId(document.getId());

                        userList.add(user);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });

        adapter.setOnEditClickListener(new AdminUserAdapter.OnEditClickListener() {
            @Override
            public void onEditClick(int position) {
                // Implement edit functionality here
                User user = userList.get(position);

                // Show edit dialog or navigate to edit activity

                EditUserDialogFragment dialogFragment = new EditUserDialogFragment(user);
                dialogFragment.show(getSupportFragmentManager(), "EditUserDialogFragment");
            }
        });

        adapter.setOnDeleteClickListener(position -> {
            // Implement delete functionality here
            User user = userList.get(position);
            // Show delete confirmation dialog and handle deletion
            AlertDialog.Builder builder = new AlertDialog.Builder(UserActivity.this);
            builder.setTitle("Confirm Deletion")
                    .setMessage("Are you sure you want to delete "+user.getUsername()+" ?")
                    .setPositiveButton("Delete", (dialogInterface, i) -> {
                        // Handle the delete action here
                        //
                        firestoreCRUD.deleteDocument("users", user.getUserId(), task -> {
                            if (task.isSuccessful()){
                                Toast.makeText(UserActivity.this, "user deleted successfully!!", Toast.LENGTH_SHORT).show();

                                adapter.notifyDataSetChanged();
                            }
                        });

                    })
                    .setNegativeButton("Cancel", (dialogInterface, i) -> {
                        dialogInterface.dismiss(); // Dismiss the dialog if canceled
                    });

            AlertDialog dialog = builder.create();
            dialog.show();

        });

        adapter.setOnDetailsClickListener(position -> {
            // Implement more details functionality here
            User user = userList.get(position);
            // Show more details activity or dialog
            Intent intent = new Intent(UserActivity.this, CustomerDetailsActivity.class);
            intent.putExtra("USER_DETAILS", user);
            startActivity(intent);
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
