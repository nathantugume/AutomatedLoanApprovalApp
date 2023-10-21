package com.example.automatedloanapprovalapp.ui.loanofficer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.automatedloanapprovalapp.R;
import com.example.automatedloanapprovalapp.adapters.UserAdapter;
import com.example.automatedloanapprovalapp.classes.FirestoreCRUD;
import com.example.automatedloanapprovalapp.classes.User;
import com.example.automatedloanapprovalapp.fragments.EditUserDialogFragment;
import com.example.automatedloanapprovalapp.ui.customer.CustomerDetailsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ManageAccountActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private UserAdapter adapter;

    private FirestoreCRUD firestoreCRUD = new FirestoreCRUD();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_account);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Assume userList contains your User objects
        List<User> userList = new ArrayList<>();
        // Add User objects to the list

        adapter = new UserAdapter(userList);
        recyclerView.setAdapter(adapter);

        firestoreCRUD.queryDocuments("users", "role", "customer", new OnCompleteListener<QuerySnapshot>() {
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

        adapter.setOnEditClickListener(new UserAdapter.OnEditClickListener() {
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
            AlertDialog.Builder builder = new AlertDialog.Builder(ManageAccountActivity.this);
            builder.setTitle("Confirm Deletion")
                    .setMessage("Are you sure you want to delete "+user.getUsername()+" ?")
                    .setPositiveButton("Delete", (dialogInterface, i) -> {
                        // Handle the delete action here
                        //
                            firestoreCRUD.deleteDocument("users", user.getUserId(), new OnCompleteListener<Void>() {
                                @SuppressLint("NotifyDataSetChanged")
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(ManageAccountActivity.this, "user deleted successfully!!", Toast.LENGTH_SHORT).show();

                                   adapter.notifyDataSetChanged();
                                    }
                                }
                            });

                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss(); // Dismiss the dialog if canceled
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();

        });

        adapter.setOnDetailsClickListener(new UserAdapter.OnDetailsClickListener() {
            @Override
            public void onDetailsClick(int position) {
                // Implement more details functionality here
                User user = userList.get(position);
                // Show more details activity or dialog
                Intent intent = new Intent(ManageAccountActivity.this, CustomerDetailsActivity.class);
                intent.putExtra("USER_DETAILS", user);
                startActivity(intent);
            }
        });

    }
}