package com.example.management.dto.request.books;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookCreateRequest {

    @NotBlank(message = "ERR_REQUIRED_BOOK_NAME")
    String title;

    @NotBlank(message = "ERR_REQUIRED_BOOK_AUTHOR")
    String author;

    String isbn;
    String qrCode;
    Integer totalQuantity;
    Integer availableQuantity;
    Integer publicationYear;
    String publisher;
    String description;
    Long categoryId;
}
