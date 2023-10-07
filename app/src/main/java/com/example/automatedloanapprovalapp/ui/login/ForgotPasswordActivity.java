package com.example.automatedloanapprovalapp.ui.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.automatedloanapprovalapp.R;
import com.example.automatedloanapprovalapp.classes.UserManager;
import com.google.android.material.textfield.TextInputEditText;

public class ForgotPasswordActivity extends AppCompatActivity {

    private TextInputEditText etEmail;
    private UserManager userManager = new UserManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);


        etEmail = findViewById(R.id.etEmail);
        Button btnResetPassword = findViewById(R.id.btnSignIn);

        btnResetPassword.setOnClickListener(view -> {
            // Get the email entered by the user
            String email = etEmail.getText().toString().trim();

            // Validate email
            if (isValidEmail(email)) {
                // Email is valid, implement the reset password logic here
                resetPassword(email);
            } else {
                // Email is not valid, show an error message
                etEmail.setError("Invalid email address");
            }
        });

        // Add a TextWatcher to clear the error message when the user starts typing
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // No action needed
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                etEmail.setError(null); // Clear the error message
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // No action needed
            }
        });
    }

    // Email validation method
    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Reset password method (replace with your actual implementation)
    private void resetPassword(String email) {
        // Implement your reset password logic here
        userManager.resetUserPassword(email, task -> Toast.makeText(ForgotPasswordActivity.this, "Password reset email has been sent successfully!!", Toast.LENGTH_LONG).show());


    }
}
