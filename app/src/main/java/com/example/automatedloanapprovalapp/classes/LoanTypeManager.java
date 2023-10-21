package com.example.automatedloanapprovalapp.classes;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoanTypeManager {
    private final CollectionReference loanTypesCollection;
    private FirebaseFirestore db;



    public LoanTypeManager() {
        // Initialize Firebase Firestore
         db = FirebaseFirestore.getInstance();
        loanTypesCollection = db.collection("loanTypes");
    }

    public void addLoanType(LoanType loanType, Context context) {
        // Create a new document with a generated ID
        Map<String, Object> loanTypeData = new HashMap<>();
        loanTypeData.put("type", loanType.getType());
        loanTypeData.put("interestRate", loanType.getInterestRate());
        loanTypeData.put("duration", loanType.getDuration());
        loanTypeData.put("status",loanType.getStatus());

        loanTypesCollection.add(loanTypeData)
                .addOnSuccessListener(documentReference -> {
                    // Get the document ID and set it as the loanType's ID
                    String documentId = documentReference.getId();
                    loanType.setId(documentId);
                    // Update the document with the ID
                    loanTypesCollection.document(documentId).set(loanType);
                    // Loan type added successfully
                    Toast.makeText(context, "Loan type added successfully!!", Toast.LENGTH_SHORT).show();

                })
                .addOnFailureListener(e -> {
                    // Failed to add loan type
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // Method to fetch loan types from Firestore
    public void getLoanTypes(OnCompleteListener<QuerySnapshot> onCompleteListener) {
        db.collection("loan_types")
                .get()
                .addOnCompleteListener(onCompleteListener);
    }

    // New method to retrieve loan types as a list
    public Task<List<LoanType>> getLoanTypesAsList() {
        return db.collection("loanTypes")
                .get()
                .continueWith(new Continuation<QuerySnapshot, List<LoanType>>() {
                    @Override
                    public List<LoanType> then(@NonNull Task<QuerySnapshot> task) throws Exception {
                        List<LoanType> loanTypes = new ArrayList<>();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Convert each document to a LoanType object and add it to the list
                                LoanType loanType = document.toObject(LoanType.class);
                                loanTypes.add(loanType);
                            }
                        }
                        return loanTypes;
                    }
                });
    }




}
