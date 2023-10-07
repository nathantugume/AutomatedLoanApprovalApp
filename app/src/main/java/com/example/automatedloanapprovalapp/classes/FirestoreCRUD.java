package com.example.automatedloanapprovalapp.classes;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class FirestoreCRUD {

    private FirebaseFirestore db;

    public FirestoreCRUD() {
        db = FirebaseFirestore.getInstance();
    }

    // Create a new document in a specified collection
    public void createDocument(String collectionName, Object data, OnCompleteListener<DocumentReference> onCompleteListener) {
        CollectionReference collection = db.collection(collectionName);
        collection
                .add(data)
                .addOnCompleteListener(onCompleteListener);
    }
    // Create a new document with id in a specified collection
    public void createDocumentWithId(String collectionName, String Id, Object data, OnCompleteListener<Void> onCompleteListener) {
        CollectionReference collection = db.collection(collectionName);
        collection
                .document(Id)
                .set(data)
                .addOnCompleteListener(onCompleteListener);
    }

    // Read a single document by document ID from a specified collection
    public void readDocument(String collectionName, String documentId, OnCompleteListener<DocumentSnapshot> onCompleteListener) {
        DocumentReference documentReference = db.collection(collectionName).document(documentId);
        documentReference
                .get()
                .addOnCompleteListener(onCompleteListener);
    }


    // Update an existing document in a specified collection
    public void updateDocument(String collectionName, String documentId, Object updatedData, OnCompleteListener<Void> onCompleteListener) {
        DocumentReference documentReference = db.collection(collectionName).document(documentId);
        documentReference
                .set(updatedData)
                .addOnCompleteListener(onCompleteListener);
    }

    // Update specific fields within a document in a specified collection
    public void updateDocumentFields(String collectionName, String documentId, Map<String, Object> updatedFields, OnCompleteListener<Void> onCompleteListener) {
        DocumentReference documentReference = db.collection(collectionName).document(documentId);
        documentReference
                .update(updatedFields)
                .addOnCompleteListener(onCompleteListener);
    }

    // Delete a document by document ID from a specified collection
    public void deleteDocument(String collectionName, String documentId, OnCompleteListener<Void> onCompleteListener) {
        DocumentReference documentReference = db.collection(collectionName).document(documentId);
        documentReference
                .delete()
                .addOnCompleteListener(onCompleteListener);
    }

    // Query documents in a specified collection based on certain criteria
    public void queryDocuments(String collectionName, String field, String value, OnCompleteListener<QuerySnapshot> onCompleteListener) {
        CollectionReference collection = db.collection(collectionName);
        Query query = collection.whereEqualTo(field, value);
        query
                .get()
                .addOnCompleteListener(onCompleteListener);
    }

    // Retrieve all documents from a specified collection
    public void getAllDocuments(String collectionName, OnCompleteListener<QuerySnapshot> onCompleteListener) {
        CollectionReference collection = db.collection(collectionName);
        collection
                .get()
                .addOnCompleteListener(onCompleteListener);
    }

    // Delete multiple documents based on a query in a specified collection
    public void deleteDocumentsByQuery(String collectionName, String field, String value, OnCompleteListener<QuerySnapshot> onCompleteListener) {
        CollectionReference collection = db.collection(collectionName);
        Query query = collection.whereEqualTo(field, value);
        query
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            document.getReference().delete();
                        }
                    }
                    onCompleteListener.onComplete(task);
                });
    }
}

