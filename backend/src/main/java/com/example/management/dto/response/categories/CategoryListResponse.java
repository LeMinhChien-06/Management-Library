package com.example.management.dto.response.categories;

import com.example.management.dto.response.books.BookResponse;
import com.example.management.dto.response.books.BookResponseDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryListResponse {
    String name;
    String description;
    LocalDateTime createdAt;
    List<BookResponseDto> books;
}
