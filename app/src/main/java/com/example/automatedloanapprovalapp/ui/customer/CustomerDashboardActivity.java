package com.example.automatedloanapprovalapp.ui.customer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.automatedloanapprovalapp.R;
import com.example.automatedloanapprovalapp.classes.FirestoreCRUD;
import com.example.automatedloanapprovalapp.classes.UserActivity;
import com.example.automatedloanapprovalapp.classes.UserManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Date;
import java.util.Objects;

public class CustomerDashboardActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard);

        // Initialize the fused location provider client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Your other initialization code for toolbar, drawer, and card view
        // ...

        CardView loan_application = findViewById(R.id.loan_application);
        loan_application.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle loan application button click here
                // For example, start a new activity for loan application form
                 Intent intent = new Intent(CustomerDashboardActivity.this, LoanApplicationActivity.class);
                 startActivity(intent);
            }
        });

        // Check and request location permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            // If permission is granted, get the user's location
            getUserLocation();
        }
    }

    private void getUserLocation() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);

        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    Location location = locationResult.getLastLocation();
                    if (location != null) {
                        // Handle the location here
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();

                        // Get user UID
                        UserManager userManager = new UserManager(CustomerDashboardActivity.this);
                        String uid = userManager.getCurrentUser().getUid();

                        // Get current login time
                        long loginTimeMillis = System.currentTimeMillis();
                        Date loginTime = new Date(loginTimeMillis);

                        // Create a new UserActivity object with location, UID, and login time
                        UserActivity userActivity = new UserActivity(uid, latitude, longitude, loginTime);

                        // Save user activity to Firestore database
                        FirestoreCRUD firestoreCRUD = new FirestoreCRUD();
                        firestoreCRUD.createDocument("user_activity", userActivity, new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if (task.isSuccessful()) {
                                    Log.d("CustomerDashboard", "User activity saved successfully.");
                                } else {
                                    Log.e("CustomerDashboard", "Failed to save user activity: " + Objects.requireNonNull(task.getException()).getMessage());
                                }
                            }
                        });
                    }
                }
            }
        };

        // Request location updates
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    // Handle the result of the location permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // If permission is granted, get the user's location
                getUserLocation();
            } else {
                // If permission is denied, show a message to the user
                Toast.makeText(this, "Location permission denied. Cannot fetch user location.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
