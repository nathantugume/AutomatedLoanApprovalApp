package com.example.automatedloanapprovalapp.fragments;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.example.automatedloanapprovalapp.R;
import com.example.automatedloanapprovalapp.classes.User;
import com.example.automatedloanapprovalapp.classes.FirestoreCRUD;
import com.example.automatedloanapprovalapp.classes.UserManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;

import java.util.HashMap;
import java.util.Map;

public class EditUserDialogFragment extends DialogFragment {

    private TextInputEditText editTextUsername, editTextEmail,newPasswordEdt,confirmPasswordEdt;
    private Button buttonSave, buttonCancel;
    private User user;
    private FirestoreCRUD firestoreCRUD;
    private UserManager userManager = new UserManager(getContext());

    private Context context;

    public EditUserDialogFragment(User user) {
        this.user = user;
        firestoreCRUD = new FirestoreCRUD();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_user_dialog, container, false);
        context = view.getContext();

        editTextUsername = view.findViewById(R.id.editTextUsername);
        editTextEmail = view.findViewById(R.id.textEmailAddress)  ;
        buttonSave = view.findViewById(R.id.buttonSave);
        buttonCancel = view.findViewById(R.id.buttonCancel);
        newPasswordEdt = view.findViewById(R.id.newPassword);
        confirmPasswordEdt = view.findViewById(R.id.confirmPassword);


        // Set initial values
        editTextUsername.setText(user.getUsername());
        editTextEmail.setText(user.getEmail());


        // Save button click listener
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Update user details in the database
                String newUsername = editTextUsername.getText().toString();
                String newEmail = editTextEmail.getText().toString();
                String newPassword = newPasswordEdt.getText().toString();
                String confirmPassword = confirmPasswordEdt.getText().toString();

                Map<String, Object> newData = new HashMap<>();
                newData.put("role","customer");
                newData.put("password",user.getPassword());

                if (!newUsername.isEmpty()){
                   newData.put("username",newUsername);
                    firestoreCRUD.updateDocument("users", user.getUserId(), newData, task -> {
                        if (task.isSuccessful()){
                            Toast.makeText(context, "UserName updated successfully!!", Toast.LENGTH_SHORT).show();
                        }
                    });


                }
                if (!newEmail.isEmpty()) {
                    newData.put("email",newEmail);
                    firestoreCRUD.updateDocument("users",user.getUserId(),newData,task -> {
                        if (task.isSuccessful()){
                            Toast.makeText(context, "Email updated Successfully!!", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                if (!newPassword.isEmpty()){
                    if (newPassword.equals(confirmPassword)){
                        newData.put("password",newPassword);
                        newData.put("resetPassword",true);


                        firestoreCRUD.updateDocument("users",user.getUserId(),newData,task -> {
                            if (task.isSuccessful()){
                                Toast.makeText(context, "Password updated successfully", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        newPasswordEdt.setError("Passwords do not match!!");
                    }
                }

                // Dismiss the dialog
                dismiss();
            }
        });

        // Cancel button click listener
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dismiss the dialog without saving changes
                dismiss();
            }
        });

        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }
}
