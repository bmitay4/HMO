package com.example.hmo.General_Objects;

import java.io.Serializable;

public class Message implements Serializable {
    private String subject, content, fromID, fromName, toID, toName, date, time;
    private Boolean read;

    public Message() {
    }

    public Message(String subject, String content, String fromID, String fromName, String toID, String toName, String date, String time, Boolean read) {
        this.subject = subject;
        this.content = content;
        this.fromID = fromID;
        this.fromName = fromName;
        this.toID = toID;
        this.toName = toName;
        this.date = date;
        this.time = time;
        this.read = read;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFromID() {
        return fromID;
    }

    public void setFromID(String fromID) {
        this.fromID = fromID;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getToID() {
        return toID;
    }

    public void setToID(String toID) {
        this.toID = toID;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
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

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    @Override
    public String toString() {
        return "Message{" +
                "subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                ", fromID='" + fromID + '\'' +
                ", fromName='" + fromName + '\'' +
                ", toID='" + toID + '\'' +
                ", toName='" + toName + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", read=" + read +
                '}';
    }
}


