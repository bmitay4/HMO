package com.example.hmo;

import java.io.Serializable;

public class Massages implements Serializable {
    private String fromID,subject,content,toID,fromName,toName;

    public Massages(String subject, String content,String fromID,String fromName, String toID,String toName) {
        this.fromID = fromID;
        this.fromName=fromName;
        this.toID = toID;
        this.toName=toName;
        this.subject = subject;
        this.content = content;
    }
    public Massages(){
//        this.fromID = "";
//        this.fromName="";
//        this.toID = "";
//        this.toName="";
//        this.subject = "";
//        this.content = "";
    }
    //Getters
    public String getfromID() {
        return fromID;
    }
    public String getSubject() {
        return subject;
    }
    public String getContent() {
        return content;
    }
    public String getToID() {
        return toID;
    }
    public String getFromName(){return fromName;}
    public String getToName(){return toName;}

    //Setters
    public void setToID(String toID) {
        this.toID = toID;
    }
    public void setFromID(String fromID) {
        this.fromID = fromID;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public void setToName(String toName){this.toName=toName;}
    public void setFromName(String fromName){this.fromName=fromName;}


}


