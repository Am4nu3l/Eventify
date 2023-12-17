package com.example.eventplanner.Objects;

public class User {
    private  String firstName;
    private String middleName;
    private String userId;
    private String phoneNumber;
    private String userEmail;

    public User(String firstName, String middleName, String userId, String phoneNumber, String userEmail) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        this.userEmail = userEmail;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getUserId() {
        return userId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getUserEmail() {
        return userEmail;
    }
}
