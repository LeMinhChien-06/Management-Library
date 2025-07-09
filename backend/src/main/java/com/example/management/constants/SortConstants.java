package com.example.management.constants;

import java.util.Set;

public final class SortConstants {
    
    private SortConstants() {}

    // User sort fields
    public static final class User {
        public static final String CREATED_AT = "createdAt";
        public static final String USERNAME = "username";
        public static final String EMAIL = "email";
        public static final String FULL_NAME = "fullName";
        public static final String PHONE = "phone";
        public static final String IS_ACTIVE = "isActive";
        public static final String ID = "id";
        
        public static final Set<String> ALLOWED_FIELDS = Set.of(
            CREATED_AT, USERNAME, EMAIL, FULL_NAME, PHONE, IS_ACTIVE, ID
        );
        
        // Commonly used fields
        public static final Set<String> BASIC_FIELDS = Set.of(
            CREATED_AT, USERNAME, EMAIL
        );
        
        private User() {}
    }

    // Book sort fields (example for other entities)
    public static final class Book {
        public static final String CREATED_AT = "createdAt";
        public static final String TITLE = "title";
        public static final String AUTHOR = "author";
        public static final String ISBN = "isbn";
        public static final String ID = "id";
        
        public static final Set<String> ALLOWED_FIELDS = Set.of(
            CREATED_AT, TITLE, AUTHOR, ISBN, ID
        );

        private Book() {}
    }

    // Common fields cho mọi entity
    public static final class Common {
        public static final String ID = "id";
        public static final String CREATED_AT = "createdAt";
        public static final String UPDATED_AT = "updatedAt";
        
        public static final Set<String> BASIC_FIELDS = Set.of(
            ID, CREATED_AT, UPDATED_AT
        );
        
        private Common() {}
    }
}