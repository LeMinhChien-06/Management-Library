package com.example.management.dto.response.books;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookInfo {

    Long bookId;

    String title;

    String author;

}
