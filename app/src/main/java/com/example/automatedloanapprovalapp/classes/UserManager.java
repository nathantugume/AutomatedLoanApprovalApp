package com.example.automatedloanapprovalapp.classes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.example.automatedloanapprovalapp.ui.login.LogInActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class UserManager {

    private FirebaseAuth mAuth;
    private Context context;

    public UserManager(){}

    public UserManager(Context context) {
        mAuth = FirebaseAuth.getInstance();
        this.context = context;
    }

    public void signUpUser(String email, String password, OnCompleteListener<AuthResult> onCompleteListener) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, onCompleteListener);
    }


    public void signInUser(String email, String password, OnCompleteListener onCompleteListener) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, onCompleteListener);
    }

    public boolean isUserAuthenticated() {
        return mAuth.getCurrentUser() != null;
    }

    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    public void signOutUser() {
        mAuth.signOut();
        // Optionally, you can navigate to the login or main activity after signing out.
        // For example:
         Intent intent = new Intent(context, LogInActivity.class);
         context.startActivity(intent);
         ((Activity) context).finish(); // Finish the current activity
    }
    public void updateUserProfile(String displayName, OnCompleteListener<Void> onCompleteListener) {
        FirebaseUser user = mAuth.getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .build();

        if (user != null) {
            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(onCompleteListener);
        }
    }

    public void changeUserPassword(String newPassword, OnCompleteListener<Void> onCompleteListener) {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            user.updatePassword(newPassword)
                    .addOnCompleteListener(onCompleteListener);
        }
    }

    public void resetUserPassword(String email, OnCompleteListener<Void> onCompleteListener) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Password reset email sent successfully
                            onCompleteListener.onComplete(task);
                        } else {
                            // Password reset email sending failed
                            onCompleteListener.onComplete(task);
                        }
                    }
                });
    }
}
