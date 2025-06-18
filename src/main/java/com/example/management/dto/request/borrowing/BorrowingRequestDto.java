package com.example.management.dto.request.borrowing;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class BorrowingRequestDto {
    private Long userId;
    private List<Long> bookIds;
    private LocalDate dueDate;
}