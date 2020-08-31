package com.example.studentchatapss;

public class ActiveUser {

    String GroupName;
    String UserName;
    String lastActiveTime;
    String UserID;
    String active;

    public ActiveUser(String groupName, String userName, String lastActiveTime, String userID, String active) {
        GroupName = groupName;
        UserName = userName;
        this.lastActiveTime = lastActiveTime;
        UserID = userID;
        if(active=="true"){
            active = "Online";
        }else{
            active = "Offline";
        }
        this.active = active;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getLastActiveTime() {
        return lastActiveTime;
    }

    public void setLastActiveTime(String lastActiveTime) {
        this.lastActiveTime = lastActiveTime;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }
}
