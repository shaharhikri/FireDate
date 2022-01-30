package com.android_final_project.firedate.data;

public class ChatListEntity {
    private String userId;
    private String name;
    private String ProfileImageUrl;
    private String time;

    public ChatListEntity() { }

    public ChatListEntity(String userId, String name, String profileImageUrl, String time) {
        this.userId = userId;
        this.name = name;
        ProfileImageUrl = profileImageUrl;
        this.time = time;
    }

    public String getUserId() {
        return userId;
    }

    public ChatListEntity setUserId(String userId) {
        this.userId = userId;
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
