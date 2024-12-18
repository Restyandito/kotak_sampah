package com.example.aplikasi_layanan_sampah;

import com.google.firebase.firestore.FieldValue;

public class UserData {
    private String userId;
    private String email;
    private String displayName;
    private Object timestamp;  // Field untuk timestamp

    // Constructor
    public UserData(String userId, String email, String displayName) {
        this.userId = userId;
        this.email = email;
        this.displayName = displayName;
        this.timestamp = FieldValue.serverTimestamp();  // Mengatur timestamp menggunakan server timestamp
    }

    // Getter and Setter
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }
}
