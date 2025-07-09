package com.example.management.mapper;

import com.example.management.dto.response.borrowing.ReturnBooksResponse;
import com.example.management.entity.Book;
import com.example.management.entity.Borrowing;
import com.example.management.entity.BorrowingDetail;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BorrowingMapper {

    public ReturnBooksResponse returnBooks(Borrowing borrowing, List<BorrowingDetail> borrowingDetail) {
        List<ReturnBooksResponse.ReturnedBookDto> returnedBooks = borrowingDetail.stream()
                .map(detail -> toReturnedBookDto(borrowing, detail))
                .collect(Collectors.toList());

        return ReturnBooksResponse.builder()
                .borrowingId(borrowing.getId())
                .userId(borrowing.getUser().getId())
                .userFullName(borrowing.getUser().getFullName())
                .returnDate(borrowing.getReturnDate())
                .returnedBooks(returnedBooks)
                .build();
    }

    public ReturnBooksResponse.ReturnedBookDto toReturnedBookDto(Borrowing borrowing, BorrowingDetail detail) {
        LocalDate dueDate = borrowing.getDueDate();
        LocalDate returnDate = borrowing.getReturnDate() != null ? borrowing.getReturnDate() : LocalDate.now();
        long overdueDays = 0;
        double fine = 0.0;
        if (dueDate != null && returnDate.isAfter(dueDate)) {
            overdueDays = ChronoUnit.DAYS.between(dueDate, returnDate);
            fine = overdueDays * 5000; // Ví dụ: 5000đ/ngày
        }
        return ReturnBooksResponse.ReturnedBookDto.builder()
                .bookId(detail.getBook().getId())
                .bookTitle(detail.getBook().getTitle())
                .author(detail.getBook().getAuthor())
                .borrowDate(borrowing.getBorrowDate())
                .dueDate(dueDate)
                .returnDate(returnDate)
                .overdueDays(overdueDays)
                .fine(fine)
                .build();
    }
}
