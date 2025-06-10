package com.example.management.mapper;

import com.example.management.dto.response.books.BookResponse;
import com.example.management.entity.Book;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookMapper {

//    public Book toBook(BookRequest bookRequest){
//
//    }

    public BookResponse toBookResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .qrCode(book.getQrCode())
                .totalQuantity(book.getTotalQuantity())
                .availableQuantity(book.getAvailableQuantity())
                .publicationYear(book.getPublicationYear())
                .publisher(book.getPublisher())
                .description(book.getDescription())
                .build();
    }

    public List<BookResponse> toBookResponseList(List<Book> books){
        return books.stream()
                .map(this::toBookResponse)
                .toList();
    }
}
