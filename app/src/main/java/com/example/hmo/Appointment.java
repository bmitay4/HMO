package com.example.hmo;

import java.io.Serializable;

public class Appointment implements Serializable {
    private String date;
    private String time;
    private String docID;
    private String docName;
    private String docLastName;
    private String userID;
    private String userName;
    private String userLastName;
    private boolean avilable = true;

    public Appointment() {
    }

    public Appointment(String date, String time, String docID, String docName, String docLastName, String userID, String userName, String userLastName, boolean avilable) {
        this.date = date;
        this.time = time;
        this.docID = docID;
        this.docName = docName;
        this.docLastName = docLastName;
        this.userID = userID;
        this.userName = userName;
        this.userLastName = userLastName;
        this.avilable = avilable;
    }

    public Appointment(String date, String time, String docID, String docName, String userID, String userName, boolean avilable) {
        this.date = date;
        this.time = time;
        this.docID = docID;
        this.docName = docName;
        this.userID = userID;
        this.userName = userName;
        this.avilable = avilable;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean getAvilable() {
        return avilable;
    }

    public void setAvilable(boolean avilable) {
        this.avilable = avilable;
    }

    public boolean isAvilable() {
        return avilable;
    }

    public String getDocLastName() {
        return docLastName;
    }

    public void setDocLastName(String docLastName) {
        this.docLastName = docLastName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }
}
