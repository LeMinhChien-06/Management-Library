package com.example.management.dto.request.borrowing;

import lombok.Data;

import java.util.List;

@Data
public class ReturnBookRequest {

    /**
     * List of book IDs to return. If null or empty, all borrowed books will be returned
     */
    private List<Long> bookIds;

    /**
     * Optional notes for the return
     */
    private String notes;
}
