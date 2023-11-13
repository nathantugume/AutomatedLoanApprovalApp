package com.example.automatedloanapprovalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.automatedloanapprovalapp.classes.User;
import com.example.automatedloanapprovalapp.classes.UserManager;
import com.example.automatedloanapprovalapp.ui.login.LogInActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class SplashActivity extends AppCompatActivity {
    private static final long SPLASH_DELAY = 3000; // 3 seconds
    private ImageView imageView;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;



// ...

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
    }

    private void checkDefaultUser(ProgressDialog progressDialog) {
        // Check if a user with role "default" exists in Firestore users collection
        Query docRef = firestore.collection("users").whereEqualTo("role","default");
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().isEmpty()) {
                    // If user with role "default" doesn't exist, create one
                    mAuth.createUserWithEmailAndPassword("admin@loanapp.com", "admin@1234")
                            .addOnCompleteListener(this, task1 -> {
                                if (task1.isSuccessful()) {
                                    // Add user with role "default" to Firestore users collection
                                    String uid = mAuth.getCurrentUser().getUid();
                                    User defaultUser = new User("admin","admin@loanapp.com","admin@1234", "default");
                                    firestore.collection("users").document(uid).set(defaultUser)
                                            .addOnCompleteListener(task2 -> {
                                                // Dismiss progress dialog
                                                progressDialog.dismiss();

                                                // Start the main activity after sign-out
                                                signOutAndStartMainActivity();
                                            });
                                }
                            });
                } else {
                    // User with role "default" already exists, proceed to sign-out
                    progressDialog.dismiss();
                    signOutAndStartMainActivity();
                }
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
