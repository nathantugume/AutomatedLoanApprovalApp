package com.example.automatedloanapprovalapp.classes;

public class User {
    private String username;
    private String email;
    private String password; // Note: In practice, passwords should be securely hashed and not stored in plain text
    private String role;

    public User() {
        // Default constructor required for Firestore
    }

    public User(String username, String email, String password, String role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
}
