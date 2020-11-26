package com.example.hmo;

import java.io.Serializable;

public class NewMember implements Serializable {
    private String uid,userID,userFirstName, userLastName, userEmail, userPassword;

    public NewMember() {
    }

    public NewMember(String uid, String userID, String userFirstName, String userLastName, String userEmail, String userPassword) {
        this.uid = uid;
        this.userID = userID;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
    }

    public NewMember(String userID, String userFirstName, String userLastName, String userEmail, String userPassword) {
        this.userID = userID;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
    }

    public NewMember(String firstName, String lastName, String email, String password) {
        this.userFirstName = firstName;
        this.userLastName = lastName;
        this.userEmail = email;
        this.userPassword = password;
    }

    //Getters
    public String getUserFirstName() {
        return userFirstName;
    }
    public String getUserLastName() {
        return userLastName;
    }
    public String getUserEmail() {
        return userEmail;
    }
    public String getUserPassword() {
        return userPassword;
    }
    //Setters
    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }
    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}