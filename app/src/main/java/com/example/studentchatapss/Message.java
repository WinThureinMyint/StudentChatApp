package com.example.studentchatapss;

public class Message {
    private String date;
    private String message;
    private String messageID;
    private String time;
    private String userName;
    private String userID;
    private String groupID;

    public Message() {
    }


    public Message(String date, String message, String time, String userName,String messageID,String userID,String groupID) {
        this.date = date;
        this.message = message;
        this.time = time;
        this.userName = userName;
        this.messageID = messageID;
        this.userID = userID;
        this.groupID = groupID;
    }

    public Message(String date, String message, String time, String userName) {
        this.date = date;
        this.message = message;
        this.time = time;
        this.userName = userName;

    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
