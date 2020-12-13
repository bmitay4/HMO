package com.example.hmo.General_Objects;

import java.io.Serializable;

//This department handles information about the admins, who have access to management and system data
public class Admin implements Serializable {
    private String adminID, adminPassword;

    //Empty constructor
    public Admin() {
    }

    public Admin(String adminID, String adminPassword) {
        this.adminID = adminID;
        this.adminPassword = adminPassword;
    }

    public String getAdminID() {
        return adminID;
    }

    public void setAdminID(String adminID) {
        this.adminID = adminID;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "adminID='" + adminID + '\'' +
                ", adminPassword='" + adminPassword + '\'' +
                '}';
    }
}
