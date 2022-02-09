package com.android_final_project.firedate.data;

import java.util.Objects;

public class UserEntity {
    private String userId;
    private String name;
    private String description;
    private String profileImageUrl = null;
    private Long usersAgeInMillis;


    public UserEntity() { }

    @Deprecated
    public UserEntity(String userId, String name, String description, String profileImageUrl) {
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.profileImageUrl = profileImageUrl;
    }

    public UserEntity(String userId, String name, String description, String profileImageUrl, Long usersAgeInMillis) {
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.profileImageUrl = profileImageUrl;
        this.usersAgeInMillis = usersAgeInMillis;
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

    public Long getUsersAgeInMillis() {
        return usersAgeInMillis;
    }

    public UserEntity setUsersAgeInMillis(Long usersAgeInMillis) {
        this.usersAgeInMillis = usersAgeInMillis;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return userId.equals(that.userId);
    }

    @Override
    public int hashCode() {
        return userId.hashCode();
    }
}
