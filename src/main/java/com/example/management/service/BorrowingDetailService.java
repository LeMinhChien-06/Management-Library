package com.example.management.service;

import com.example.management.dto.request.borrowing.ReturnBookRequest;
import com.example.management.dto.response.borrowing.BorrowingDetailResponseDto;
import com.example.management.dto.response.borrowing.ReturnBooksResponse;

import java.time.LocalDate;
import java.util.List;

public interface BorrowingDetailService {

    BorrowingDetailResponseDto borrowBooks(Long userId, List<Long> bookIds, LocalDate dueDate);

    BorrowingDetailResponseDto approveBorrowing(Long borrowingId, Boolean approved, String rejReason, List<Long> bookIds);

    BorrowingDetailResponseDto rejectBorrowing(Long borrowingId, Boolean approved, String rejReason, List<Long> bookIds);


}
