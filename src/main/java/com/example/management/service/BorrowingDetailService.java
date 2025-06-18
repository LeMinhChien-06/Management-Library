package com.example.management.service;

import com.example.management.dto.response.borrowing.BorrowingDetailResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface BorrowingDetailService {

    BorrowingDetailResponseDto borrowBooks(Long userId, List<Long> bookIds, LocalDate dueDate);

}
