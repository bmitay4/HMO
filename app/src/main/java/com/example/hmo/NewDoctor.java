package com.example.hmo;

import java.io.Serializable;

public class NewDoctor implements Serializable {
    private String userFirstName, userLastName, userEmail, userPass, userSpec;

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
}