package com.android_final_project.firedate.data;

public class UserEntity {
    private String userId;
    private String name;
    private String description;
    private String profileImageUrl = null;


    public UserEntity() { }

    public UserEntity(String userId, String name, String description, String profileImageUrl) {
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.profileImageUrl = profileImageUrl;
    }

    public String getUserId() {
        return userId;
    }

    public UserEntity setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getName() {
        return name;
    }

    public UserEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public UserEntity setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public UserEntity setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
        return this;
    }
}
