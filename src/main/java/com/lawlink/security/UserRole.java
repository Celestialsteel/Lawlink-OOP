package com.lawlink.security;

import java.util.Set;
import java.util.HashSet;

public enum UserRole {
    ROLE_ADMIN("Full system access", Set.of(
        "MANAGE_USERS",
        "VIEW_ALL_CASES",
        "MANAGE_SYSTEM_SETTINGS",
        "VIEW_REPORTS",
        "MANAGE_BILLING",
        "MANAGE_DOCUMENTS",
        "MANAGE_STAFF"
    )),
    ROLE_LAWYER("Case management, client communication, document handling", Set.of(
        "VIEW_ASSIGNED_CASES",
        "MANAGE_CASE_DOCUMENTS",
        "COMMUNICATE_WITH_CLIENTS",
        "CREATE_TASKS",
        "MANAGE_CALENDAR",
        "CREATE_CASE_NOTES",
        "VIEW_CLIENT_INFO"
    )),
    ROLE_CLIENT("View own cases and documents, communicate with lawyer", Set.of(
        "VIEW_OWN_CASES",
        "VIEW_OWN_DOCUMENTS",
        "COMMUNICATE_WITH_LAWYER",
        "VIEW_OWN_CALENDAR",
        "VIEW_OWN_BILLING",
        "UPLOAD_DOCUMENTS"
    )),
    ROLE_STAFF("Document processing, task management, calendar scheduling", Set.of(
        "VIEW_ASSIGNED_TASKS",
        "MANAGE_DOCUMENTS",
        "SCHEDULE_APPOINTMENTS",
        "VIEW_CALENDAR",
        "PROCESS_DOCUMENTS",
        "BASIC_CASE_ACCESS"
    ));

    private final String description;
    private final Set<String> permissions;

    UserRole(String description, Set<String> permissions) {
        this.description = description;
        this.permissions = permissions;
    }

    public String getDescription() {
        return description;
    }

    public Set<String> getPermissions() {
        return new HashSet<>(permissions);
    }

    public String getAuthority() {
        return name();
    }

    public boolean hasPermission(String permission) {
        return permissions.contains(permission);
    }
}
