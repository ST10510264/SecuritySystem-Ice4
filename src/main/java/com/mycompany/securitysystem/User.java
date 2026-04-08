
package com.mycompany.securitysystem;


public class User {
    String username;
    String pin;
    int role;
    String timestamp;
    boolean isLocked;
    int failedAttempts;

    public User(String username, String pin, int role, String timestamp) {
        this.username = username;
        this.pin = pin;
        this.role = role;
        this.timestamp = timestamp;
        this.isLocked = false;
        this.failedAttempts = 0;
    }
}