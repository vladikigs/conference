package com.waveaccess.conference.config;

import com.google.common.collect.Sets;

import static com.waveaccess.conference.config.ApplicationPermission.*;

public enum ApplicationRole {
    Admin(Sets.newHashSet(ADMIN)),
    Presenter(Sets.newHashSet(PRESENTER)),
    User(Sets.newHashSet(USER));

    private final java.util.Set<ApplicationPermission> applicationPermissions;

    public java.util.Set<ApplicationPermission> getApplicationPermissions() {
        return applicationPermissions;
    }

    ApplicationRole(java.util.Set<ApplicationPermission> permissions) {
        this.applicationPermissions = permissions;
    }
}
