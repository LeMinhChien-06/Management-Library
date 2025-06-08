package com.example.management.enums;

public enum AuditAction {

    // Book actions
    BOOK_CREATE("Create Book"),
    BOOK_UPDATE("Update Book"),
    BOOK_DELETE("Delete Book"),
    BOOK_VIEW("View Book"),

    // User actions
    USER_CREATE("Create User"),
    USER_UPDATE("Update User"),
    USER_DELETE("Delete User"),
    USER_GET_BY_ID("Get User By Id"),
    USER_VIEW("View User"),


    // Auth actions
    LOGIN("User Login"),
    LOGOUT("User Logout"),
    LOGIN_FAILED("Failed Login Attempt");

    private final String description;

    AuditAction(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
