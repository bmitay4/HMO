package com.example.hmo;

import java.io.Serializable;

public class NewMember implements Serializable {
    private String authID, userID, userFirstName, userLastName, userEmail, userDOB, userPassword;

    //no-argument constructor
    public NewMember(){

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
}