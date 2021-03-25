package com.waveaccess.conference.config;

public enum ApplicationPermission {


    ADMIN("admin"),
    PRESENTER("presenter"),
    USER("user");

    public String getPermission() {
        return permission;
    }

    private final String permission;

    ApplicationPermission(String permission) {
        this.permission = permission;
    }
}
