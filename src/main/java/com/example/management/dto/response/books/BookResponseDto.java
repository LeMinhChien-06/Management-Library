package com.example.management.dto.response.books;

import com.example.management.dto.response.categories.CategoriesResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookResponse {
     Long id;
     String title;
     String author;
     String isbn;
     String qrCode;
     Integer totalQuantity;
     Integer availableQuantity;
     Integer publicationYear;
     String publisher;
     String description;
     String imageUrl;
     Long categoryId;
     String categoryName;
}
