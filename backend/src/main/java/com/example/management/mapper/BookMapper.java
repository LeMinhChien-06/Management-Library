package com.example.management.mapper;

import com.example.management.dto.request.books.BookCreateRequest;
import com.example.management.dto.request.books.BookUpdateRequest;
import com.example.management.dto.response.books.BookInfo;
import com.example.management.dto.response.books.BookResponse;
import com.example.management.dto.response.books.BookResponseDto;
import com.example.management.dto.response.borrowing.BorrowingDetailResponseDto;
import com.example.management.entity.Book;
import com.example.management.entity.BorrowingDetail;
import com.example.management.entity.Categories;
import com.example.management.exception.categories.CategoryExceptions;
import com.example.management.repository.CategoriesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookMapper {

    private final CategoriesRepository categoriesRepository;

    public Book toBook(BookCreateRequest bookCreateRequest) {

        Categories categories = categoriesRepository.findById(bookCreateRequest.getCategoryId())
                .orElseThrow(CategoryExceptions::categoryNotFound);

        return Book.builder()
                .title(bookCreateRequest.getTitle())
                .author(bookCreateRequest.getAuthor())
                .isbn(bookCreateRequest.getIsbn())
                .qrCode(bookCreateRequest.getQrCode())
                .totalQuantity(bookCreateRequest.getTotalQuantity())
                .availableQuantity(bookCreateRequest.getTotalQuantity())
                .publicationYear(bookCreateRequest.getPublicationYear())
                .publisher(bookCreateRequest.getPublisher())
                .description(bookCreateRequest.getDescription())
                .category(categories)
                .build();
    }

    public Book toBook(Book book, BookUpdateRequest bookUpdateRequest) {
        return Book.builder()
                .id(book.getId())
                .title(bookUpdateRequest.getTitle())
                .author(bookUpdateRequest.getAuthor())
                .totalQuantity(bookUpdateRequest.getTotalQuantity())
                .availableQuantity(bookUpdateRequest.getAvailableQuantity())
                .publicationYear(bookUpdateRequest.getPublicationYear())
                .category(book.getCategory())
                .build();
    }

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
                .imageUrl(book.getImageUrl())
                // ## Cú pháp Ternary Operator: điều_kiện ? giá_trị_nếu_đúng : giá_trị_nếu_sai
                .categoryId(book.getCategory() != null ? book.getCategory().getId() : null)
                .categoryName(book.getCategory() != null ? book.getCategory().getName() : null)
                .build();
    }

    public BookResponseDto toBookResponseDto(Book book) {
        return BookResponseDto.builder()
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

    public List<BookResponseDto> toBookResponseDto(List<Book> books) {
        return books.stream()
                .map(this::toBookResponseDto)
                .toList();
    }

    public BookInfo toBookInfo(Book book) {
        return BookInfo.builder()
                .bookId(book.getId())
                .author(book.getAuthor())
                .title(book.getTitle())
                .build();
    }
}
