package com.example.hmo;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class Appointment implements Serializable {
    private String date, time, docID, docName, docLastName, userID, userName, userLastName;
    private boolean isAvailable;

    public Appointment() {
    }

    public Appointment(String date, String time, String docID, String docName, String docLastName, String userID, String userName, String userLastName, boolean available) {
        this.date = date;
        this.time = time;
        this.docID = docID;
        this.docName = docName;
        this.docLastName = docLastName;
        this.userID = userID;
        this.userName = userName;
        this.userLastName = userLastName;
        this.isAvailable = available;
    }

    public Appointment(String date, String time, String docID, String docName, String userID, String userName, boolean available) {
        this.date = date;
        this.time = time;
        this.docID = docID;
        this.docName = docName;
        this.userID = userID;
        this.userName = userName;
        this.isAvailable = available;
    }

//    @Override
//    public String toString() {
//        return "Appointment{" +
//                "date='" + date + '\'' +
//                ", time='" + time + '\'' +
//                ", docID='" + docID + '\'' +
//                ", docName='" + docName + '\'' +
//                ", docLastName='" + docLastName + '\'' +
//                ", userID='" + userID + '\'' +
//                ", userName='" + userName + '\'' +
//                ", userLastName='" + userLastName + '\'' +
//                ", isAvailable=" + isAvailable +
//                '}';
//    }
    @NotNull
    public String toString(NewMember m) {
        return "פרטי התור שנקבע לך\n" +
                "בתאריך " + date + ", בשעה " + time + "\n" +
                "נקבע תור עבור " + m.getUserFirstName() + " " + m.getUserLastName() + "\n" +
                "ת.ז. " + m.getUserID() + "\n" +
                "הרופא המטפל: " + docName + " " + docLastName;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getDocID() {
        return docID;
    }

    public String getDocName() {
        return docName;
    }

    public String getDocLastName() {
        return docLastName;
    }

    public String getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public boolean getAvailable() {
        return isAvailable;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public void setDocLastName(String docLastName) {
        this.docLastName = docLastName;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}
