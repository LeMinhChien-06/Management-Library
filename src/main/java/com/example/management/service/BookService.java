package com.example.management.service;

import com.example.management.dto.request.books.BookCreateRequest;
import com.example.management.dto.request.books.BookUpdateRequest;
import com.example.management.dto.response.PageDTO;
import com.example.management.dto.response.books.BookResponse;


public interface BookService {

    BookResponse creatBook(BookCreateRequest bookCreateRequest);

    BookResponse updateBook(Long id, BookUpdateRequest bookUpdateRequest);

    BookResponse getBookById(Long id);

    PageDTO<BookResponse> getAllBooks(int page, int size, String sortBy, String sortDirection,
                                            String title, String author, Integer publicationYear, Long categoryId);

    void deleteBookById(Long id);

}
