package com.example.management.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import java.util.Set;

@Slf4j
@UtilityClass
public class SortUtils {

    private static final String DEFAULT_SORT_FIELD = "createdAt";
    private static final Sort.Direction DEFAULT_DIRECTION = Sort.Direction.DESC;

    /**
     * Tạo Sort với validation cho allowed fields
     * @param sortBy field name
     * @param sortDirection ASC/DESC
     * @param allowedFields set of allowed fields
     * @param defaultField default field nếu invalid
     * @return Sort object
     */
    public static Sort createSort(String sortBy, String sortDirection, 
                                 Set<String> allowedFields, String defaultField) {
        String validatedField = validateSortField(sortBy, allowedFields, defaultField);
        Sort.Direction direction = validateSortDirection(sortDirection);
        
        log.debug("Creating sort: {} {}", validatedField, direction);
        return Sort.by(direction, validatedField);
    }

    /**
     * Tạo Sort với default field và direction
     */
    public static Sort createSort(String sortBy, String sortDirection, Set<String> allowedFields) {
        return createSort(sortBy, sortDirection, allowedFields, DEFAULT_SORT_FIELD);
    }

    /**
     * Tạo Sort chỉ với allowed fields (dùng default direction)
     */
    public static Sort createSort(String sortBy, Set<String> allowedFields) {
        return createSort(sortBy, null, allowedFields, DEFAULT_SORT_FIELD);
    }

    /**
     * Validate sort field
     */
    private static String validateSortField(String sortBy, Set<String> allowedFields, String defaultField) {
        if (!StringUtils.hasText(sortBy)) {
            return defaultField;
        }

        String normalizedField = normalizeSortField(sortBy);
        
        if (allowedFields.contains(normalizedField)) {
            return normalizedField;
        }

        log.warn("Invalid sort field: '{}'. Allowed fields: {}. Using default: '{}'", 
                sortBy, allowedFields, defaultField);
        return defaultField;
    }

    /**
     * Normalize field name (handle variations)
     */
    private static String normalizeSortField(String sortBy) {
        if (!StringUtils.hasText(sortBy)) {
            return DEFAULT_SORT_FIELD;
        }

        return switch (sortBy.toLowerCase().replace("_", "")) {
            case "createdat" -> "createdAt";
            case "updatedat" -> "updatedAt";
            case "username" -> "username";
            case "email" -> "email";
            case "fullname" -> "fullName";
            case "phone" -> "phone";
            case "isactive" -> "isActive";
            case "id" -> "id";
            default -> sortBy.trim();
        };
    }

    /**
     * Validate sort direction
     */
    private static Sort.Direction validateSortDirection(String sortDirection) {
        if (!StringUtils.hasText(sortDirection)) {
            return DEFAULT_DIRECTION;
        }

        return switch (sortDirection.toUpperCase().trim()) {
            case "ASC", "ASCENDING" -> Sort.Direction.ASC;
            case "DESC", "DESCENDING" -> Sort.Direction.DESC;
            default -> {
                log.warn("Invalid sort direction: '{}'. Using default: '{}'", 
                        sortDirection, DEFAULT_DIRECTION);
                yield DEFAULT_DIRECTION;
            }
        };
    }

    /**
     * Utility methods cho common cases
     */
    public static Sort byCreatedAtDesc() {
        return Sort.by(Sort.Direction.DESC, "createdAt");
    }

    public static Sort byCreatedAtAsc() {
        return Sort.by(Sort.Direction.ASC, "createdAt");
    }

    public static Sort byIdDesc() {
        return Sort.by(Sort.Direction.DESC, "id");
    }

    public static Sort byIdAsc() {
        return Sort.by(Sort.Direction.ASC, "id");
    }

    public static Sort unsorted() {
        return Sort.unsorted();
    }
}