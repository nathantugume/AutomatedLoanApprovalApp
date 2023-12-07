package com.example.automatedloanapprovalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.example.automatedloanapprovalapp.classes.User;
import com.example.automatedloanapprovalapp.ui.login.LogInActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class SplashActivity extends AppCompatActivity {
    private static final long SPLASH_DELAY = 3000; // 3 seconds
    private ImageView imageView;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        imageView = findViewById(R.id.appIcon);

        // Load animation
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        imageView.startAnimation(animation);

        // Show progress dialog
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading ...");
        progressDialog.show();

        // Check for default user before transitioning
        checkDefaultUser(progressDialog);
        signOutAndStartMainActivity();
    }

    private void checkDefaultUser(ProgressDialog progressDialog) {
        String email = "admin@loanapp.com";

        // Check if a user with the given email address already exists
        mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().getSignInMethods().isEmpty()) {
                    // User does not exist, create a new user
                    createUserAndProceed(progressDialog, email);
                } else {
                    // User already exists, proceed to sign-out
                    progressDialog.dismiss();
                    signOutAndStartMainActivity();
                }
            }
        });
    }

    private void createUserAndProceed(ProgressDialog progressDialog, String email) {
        // Show progress dialog
        progressDialog.setMessage("Creating user...");
        progressDialog.show();

        // Create a new user with the given email and password
        mAuth.createUserWithEmailAndPassword(email, "admin@1234")
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Add user with role "default" to Firestore users collection
                        String uid = mAuth.getCurrentUser().getUid();
                        User defaultUser = new User("admin", email, "admin@1234", "default");
                        firestore.collection("users").document(uid).set(defaultUser)
                                .addOnCompleteListener(task2 -> {

                                    // Dismiss progress dialog
                                    progressDialog.dismiss();

                                    // Start the main activity after sign-out
                                    signOutAndStartMainActivity();
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        signOutAndStartMainActivity();
                                    }
                                });
                    } else {
                        // User creation failed
                        progressDialog.dismiss();
                        // Handle the error, e.g., display a message or log the error
                    }
                });
    }

    private void signOutAndStartMainActivity() {
        // Show progress dialog
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Signing out...");
        progressDialog.show();

        // Sign out the user
        mAuth.signOut();

        // Start the main activity after sign-out
        new Handler().postDelayed(() -> {
            progressDialog.dismiss(); // Dismiss progress dialog
            Intent intent = new Intent(SplashActivity.this, LogInActivity.class);
            startActivity(intent);
            finish();
        }, SPLASH_DELAY);
    }
}
