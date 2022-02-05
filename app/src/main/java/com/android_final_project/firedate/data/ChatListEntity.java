package com.android_final_project.firedate.data;

public class ChatListEntity {
    private String chatId;
    private String userId;
    private String userSexualGroup;
    private String name;
    private String ProfileImageUrl;
    private String time;

    public ChatListEntity() { }

    public ChatListEntity(String chatId, String userId, String userSexualGroup, String name, String profileImageUrl, String time) {
        this.chatId = chatId;
        this.userId = userId;
        this.userSexualGroup = userSexualGroup;
        this.name = name;
        ProfileImageUrl = profileImageUrl;
        this.time = time;
    }

    public String getChatId() {
        return chatId;
    }

    public ChatListEntity setChatId(String chatId) {
        this.chatId = chatId;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public ChatListEntity setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getUserSexualGroup() {
        return userSexualGroup;
    }

    public ChatListEntity setUserSexualGroup(String userSexualGroup) {
        this.userSexualGroup = userSexualGroup;
        return this;
    }

    public String getName() {
        return name;
    }

    public ChatListEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getProfileImageUrl() {
        return ProfileImageUrl;
    }

    public ChatListEntity setProfileImageUrl(String profileImageUrl) {
        ProfileImageUrl = profileImageUrl;
        return this;
    }

    public String getTime() {
        return time;
    }

    public ChatListEntity setTime(String time) {
        this.time = time;
        return this;
    }
}
