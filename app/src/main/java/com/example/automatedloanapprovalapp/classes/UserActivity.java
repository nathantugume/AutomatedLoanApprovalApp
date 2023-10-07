package com.example.automatedloanapprovalapp.classes;

import com.google.firebase.firestore.ServerTimestamp;
import java.util.Date;

public class UserActivity {

    private String uid;
    private double latitude;
    private double longitude;
    @ServerTimestamp
    private Date loginTime;

    // Default constructor (required for Firestore)
    public UserActivity() {
        // Default constructor required for calls to DataSnapshot.getValue(UserActivity.class)
    }

    public UserActivity(String uid, double latitude, double longitude, Date loginTime) {
        this.uid = uid;
        this.latitude = latitude;
        this.longitude = longitude;
        this.loginTime = loginTime;
    }

    public String getUid() {
        return uid;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    // Convert Firestore Timestamp to Date
    public void setLoginTime(com.google.firebase.Timestamp timestamp) {
        this.loginTime = timestamp.toDate();
    }
}
