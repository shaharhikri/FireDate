package com.android_final_project.firedate.data;

public class ChatEntity {

    private String message;
    private boolean currentUser;

    public ChatEntity() { }

    public ChatEntity(String message, boolean currentUser) {
        this.message = message;
        this.currentUser = currentUser;
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
}
