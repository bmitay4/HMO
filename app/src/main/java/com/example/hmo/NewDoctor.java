package com.example.hmo;

import java.io.Serializable;

public class NewDoctor implements Serializable {
    private String uid,userID,userFirstName, userLastName, userEmail, userPass, userSpec;

    public NewDoctor() {
    }

    public NewDoctor(String uid, String userID, String userFirstName, String userLastName, String userEmail, String userPass, String userSpec) {
        this.uid = uid;
        this.userID = userID;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userEmail = userEmail;
        this.userPass = userPass;
        this.userSpec = userSpec;
    }

    public NewDoctor(String userID, String userFirstName, String userLastName, String userEmail, String userPass, String userSpec) {
        this.userID = userID;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userEmail = userEmail;
        this.userPass = userPass;
        this.userSpec = userSpec;
    }

    public NewDoctor(String firstName, String lastName, String email, String Pass, String spec) {
        this.userFirstName = firstName;
        this.userLastName = lastName;
        this.userEmail = email;
        this.userPass = Pass;
        this.userSpec = spec;
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

    public String getUserSpec() {
        return userSpec;
    }

    public void setUserSpec(String userSpec) {
        this.userSpec = userSpec;
    }
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}