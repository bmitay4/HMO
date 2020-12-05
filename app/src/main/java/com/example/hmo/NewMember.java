package com.example.hmo;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class NewMember implements Serializable {
    private String authID, userID, userFirstName, userLastName, userEmail, userDOB, userPassword,userGender;

    //no-argument constructor
    public NewMember(){

    }

    public NewMember(String authID, String userID, String userFirstName, String userLastName, String userEmail, String userPassword, String userDOB, String userGender) {
        this.authID = authID;
        this.userID = userID;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userEmail = userEmail;
        this.userDOB = userDOB;
        this.userPassword = userPassword;
        this.userGender = userGender;
    }

    public NewMember(String aID, String uID, String firstName, String lastName, String email, String password, String DOB) {
        this.authID = aID;
        this.userID = uID;
        this.userFirstName = firstName;
        this.userLastName = lastName;
        this.userEmail = email;
        this.userPassword = password;
        this.userDOB = DOB;
    }

    @NotNull
    @Override
    public String toString() {
        return "תעודת זהות: " + userID + "\n" +
                "שם מלא: " + userFirstName + " " + userLastName + "\n" +
                "כתובת דואל: " + userEmail + "\n" +
                "תאריך לידה: " + userDOB + "\n" +
                "מין: " + userGender;
    }

    //Setters
    public String getUserID() {
        return userID;
    }

    //Setters
    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getAuthID() {
        return authID;
    }

    public void setAuthID(String authID) {
        this.authID = authID;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserDOB() {
        return userDOB;
    }

    public void setUserDOB(String userDOB) {
        this.userDOB = userDOB;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }
}