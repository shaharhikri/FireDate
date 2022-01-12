package com.android_final_project.firedate.data;

public class UserEntity {
    private String name;
    private String description;

    public UserEntity() { }

    public UserEntity(String name, String description) {
        this.name = name;
        this.description = description;
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
}
