package com.android_final_project.firedate.data;

import android.location.Location;

import java.util.Objects;

public class UserEntity {
    private String userId;
    private String name;
    private String description;
    private String profileImageUrl = null;
    private long usersAgeInMillis;
    private Location location;
    private int searchDistance;


    public UserEntity() { }

//    @Deprecated
//    public UserEntity(String userId, String name, String description, String profileImageUrl) {
//        this.userId = userId;
//        this.name = name;
//        this.description = description;
//        this.profileImageUrl = profileImageUrl;
//        this.usersAgeInMillis = 807657172701L;
//    }

//    @Deprecated
//    public UserEntity(String userId, String name, String description, String profileImageUrl, long usersAgeInMillis, int searchDistance) {
//        this.userId = userId;
//        this.name = name;
//        this.description = description;
//        this.profileImageUrl = profileImageUrl;
//        this.usersAgeInMillis = usersAgeInMillis;
//        this.searchDistance = searchDistance;
//    }


    public UserEntity(String userId, String name, String description, String profileImageUrl, long usersAgeInMillis, Location location, int searchDistance) {
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.profileImageUrl = profileImageUrl;
        this.usersAgeInMillis = usersAgeInMillis;
        this.location = location;
        this.searchDistance = searchDistance;
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

    public UserEntity setUsersAgeInMillis(long usersAgeInMillis) {
        this.usersAgeInMillis = usersAgeInMillis;
        return this;
    }

    public int getSearchDistance() {
        return searchDistance;
    }

    public UserEntity setSearchDistance(int searchDistance) {
        this.searchDistance = searchDistance;
        return this;
    }

    public Location getLocation() {
        return location;
    }

    public UserEntity setLocation(Location location) {
        this.location = location;
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
