package com.example.hmo.General_Objects;

import java.io.Serializable;

public class NewDoctor implements Serializable {
    private String authID, userID, userFirstName, userLastName, userEmail, userPassword, userSpec, userGender;

    //no-argument constructor
    public NewDoctor(){

    }

    public NewDoctor(String authID, String userID, String userFirstName, String userLastName, String userEmail, String userPassword, String userSpec, String userGender) {
        this.authID = authID;
        this.userID = userID;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userSpec = userSpec;
        this.userGender = userGender;
    }

    public NewDoctor(String aID, String uID, String firstName, String lastName, String email, String Pass, String spec) {
        this.authID = aID;
        this.userID = uID;
        this.userFirstName = firstName;
        this.userLastName = lastName;
        this.userEmail = email;
        this.userPassword = Pass;
        this.userSpec = spec;
    }
    //Getters
    public String getAuthID() {
        return authID;
    }

    public String getUserID() {
        return userID;
    }

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

    public String getUserSpec() {
        return userSpec;
    }

    //Setters

    public void setAuthID(String authID) {
        this.authID = authID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserPass(String userPass) {
        this.userPassword = userPass;
    }

    public void setUserSpec(String userSpec) {
        this.userSpec = userSpec;
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