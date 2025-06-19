package com.example.management.enums;

public enum AuditAction {

    // Category actions
    CATEGORY_CREATE("Create Category"),
    CATEGORY_DELETE("Delete Category"),
    CATEGORY_VIEW("View Category"),
    CATEGORY_GET_BY_ID("Get Category By Id"),

    // Book actions
    BOOK_CREATE("Create Book"),
    BOOK_UPDATE("Update Book"),
    BOOK_DELETE("Delete Book"),
    BOOK_VIEW("View Book"),
    BOOK_GET_BY_ID("Get Book By Id"),

    // Borrow actions
    BORROWING_CREATE("Create Borrowing"),

    PENDING_BORROWING("Pending Borrowing"),
    APPROVED_BORROWING("Approved Borrowing"),
    REJECTED_BORROWING("Rejected Borrowing"),
    RETURNED_BORROWING("Returned Borrowing"),
    OVERDUE_BORROWING("Overdue Borrowing"),

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
