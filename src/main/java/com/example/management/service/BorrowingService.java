package com.example.management.service;

import com.example.management.dto.request.borrowing.ReturnBookRequest;
import com.example.management.dto.response.borrowing.ReturnBooksResponse;

public interface BorrowingService {
    ReturnBooksResponse returnBooks(Long borrowingId, ReturnBookRequest request);
}
