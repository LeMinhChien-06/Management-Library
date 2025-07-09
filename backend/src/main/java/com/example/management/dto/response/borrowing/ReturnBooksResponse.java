package com.example.management.dto.response.borrowing;

import com.example.management.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReturnBooksResponse {
    private Long borrowingId;
    private Long userId;
    private String userFullName;
    private LocalDate returnDate;
    private List<ReturnedBookDto> returnedBooks;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReturnedBookDto {
        private Long bookId;
        private String bookTitle;
        private String author;
        private LocalDate borrowDate;
        private LocalDate dueDate;
        private LocalDate returnDate;
        private Long overdueDays;
        private Double fine;
    }
}
