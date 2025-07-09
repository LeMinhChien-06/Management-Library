package com.example.management.dto.request.books;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookUpdateRequest {
    String title;
    String author;
    Integer totalQuantity;
    Integer availableQuantity;
    Integer publicationYear;
    String publisher;
    String description;
}
