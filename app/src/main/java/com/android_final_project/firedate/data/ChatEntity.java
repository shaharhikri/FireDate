package com.android_final_project.firedate.data;

import java.util.Date;

public class ChatEntity {

    private String message;
    private boolean currentUser;
    private Date time;


    public ChatEntity() { }

//    public ChatEntity(String message, boolean currentUser) {
//        this.message = message;
//        this.currentUser = currentUser;
//    }

    public ChatEntity(String message, boolean currentUser, Date time) {
        this.message = message;
        this.currentUser = currentUser;
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public ChatEntity setMessage(String message) {
        this.message = message;
        return this;
    }

    public boolean isCurrentUser() {
        return currentUser;
    }

    public ChatEntity setCurrentUser(boolean currentUser) {
        this.currentUser = currentUser;
        return this;
    }

    public Date getTime() {
        return time;
    }

    public ChatEntity setTime(Date time) {
        this.time = time;
        return this;
    }
}
