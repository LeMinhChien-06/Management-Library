package com.example.management.dto.response.borrowing;

import com.example.management.dto.response.books.BookInfo;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BorrowingDetailResponseDto {

    Long borrowingId;

    Long userId;

    String userFullName;

    LocalDate dueDate;

    List<BookInfo> books;
}
