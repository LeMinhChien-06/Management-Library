package com.example.management.service.impl;

import com.example.management.annotation.TrackAction;
import com.example.management.constants.SortConstants;
import com.example.management.dto.request.books.BookCreateRequest;
import com.example.management.dto.request.books.BookUpdateRequest;
import com.example.management.dto.response.PageDTO;
import com.example.management.dto.response.books.BookResponse;
import com.example.management.entity.Book;
import com.example.management.enums.AuditAction;
import com.example.management.enums.EntityType;
import com.example.management.exception.book.BookExceptions;
import com.example.management.mapper.BookMapper;
import com.example.management.repository.BookRepository;
import com.example.management.service.BookService;
import com.example.management.service.GeneratorService;
import com.example.management.utils.SortUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final GeneratorService generatorService;

    @Override
    @Transactional
    @TrackAction(
            action = AuditAction.BOOK_CREATE,
            entityType = EntityType.BOOK,
            entityId = "#result.id",
            entityName = "#result.title"
    )
    public BookResponse creatBook(BookCreateRequest bookCreateRequest, MultipartFile fileImage) throws IOException {
        Book book = bookMapper.toBook(bookCreateRequest);

        if (bookCreateRequest.getIsbn() == null || bookCreateRequest.getIsbn().isEmpty()) {
            book.setIsbn(generatorService.generateIsbn());
            log.info("Auto-generated ISBN for book '{}' is '{}'", book.getTitle(), book.getIsbn());
        }

        // Tạo temporary QR code trước khi save (để đảm bảo unique)
        String tempQRCode = generatorService.generateTemporaryQRCode(
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn()
        );
        book.setQrCode(tempQRCode);


        Book savedBook = bookRepository.save(book);

        log.info("Saved book with temporary QR: {}", savedBook.getId());

        String qrData = generatorService.generateUniqueQRCode(savedBook.getId(), savedBook.getTitle(), savedBook.getIsbn());

        // Đặt tên object cho MinIO
        String objectName = "covers/" + UUID.randomUUID() + "_" + fileImage.getOriginalFilename();

        // Chèn QR vào ảnh bìa và upload lên MinIO
        String imageUrl = generatorService.addQRCodeAndUploadToMinio(
                fileImage.getInputStream(),
                qrData,
                objectName
        );

        savedBook.setImageUrl(imageUrl);

        String finalQRImageUrl = generatorService.createQRCodeImage(
                qrData,
                savedBook.getId()
        );
        savedBook.setQrCode(finalQRImageUrl);

        savedBook = bookRepository.save(savedBook);

        log.info("Saved book: {}", savedBook);

        return bookMapper.toBookResponse(savedBook);
    }

    @Override
    @Transactional
    @TrackAction(
            action = AuditAction.BOOK_UPDATE,
            entityType = EntityType.BOOK,
            entityId = "#result.id",
            entityName = "#result.title"
    )
    public BookResponse updateBook(Long id, BookUpdateRequest bookUpdateRequest) {
        Book book = bookRepository.findByIdWithCategory(id).orElseThrow(BookExceptions::bookNotFound);

        String oldQRCode = book.getQrCode();
        Book updatedBook = bookMapper.toBook(book, bookUpdateRequest);

//        if (!book.getTitle().equals(bookUpdateRequest.getTitle())) {
//            generatorService.deleteQRCodeImage(oldQRCode);
//        }

        log.info("Regenerated QR Code for updated book: {}", id);

        Book savedBook = bookRepository.save(updatedBook);

        log.info("Updated book: {}", savedBook);

        return bookMapper.toBookResponse(savedBook);
    }

    @Override
    @Transactional
    @TrackAction(
            action = AuditAction.BOOK_GET_BY_ID,
            entityType = EntityType.BOOK,
            entityId = "#result.id",
            entityName = "#result.title"
    )
    public BookResponse getBookById(Long id) {
        Book book = bookRepository.findByIdWithCategory(id).orElseThrow(BookExceptions::bookNotFound);
        log.info("Retrieved book: {}", book);
        return bookMapper.toBookResponse(book);
    }

    @Override
    @TrackAction(
            action = AuditAction.BOOK_VIEW,
            entityType = EntityType.BOOK,
            entityId = "#arg"
    )
    public PageDTO<BookResponse> getAllBooks(int page, int size, String sortBy, String sortDirection,
                                             String title, String author, Integer publicationYear, Long categoryId
    ) {
        Sort sort = SortUtils.createSort(
                sortBy,
                sortDirection,
                SortConstants.Book.ALLOWED_FIELDS,
                SortConstants.Book.CREATED_AT
        );

        PageRequest pageRequest = PageRequest.of(page, size, sort);

        Page<Book> bookList = bookRepository.getAllBooksWithFilters(title, author, publicationYear, categoryId, pageRequest);

        List<BookResponse> bookResponseList = bookList.stream()
                .map(bookMapper::toBookResponse)
                .toList();

        log.info("Retrieved {} books", bookResponseList.size());

        return PageDTO.of(
                bookResponseList,
                bookList.getNumber(),
                bookList.getSize(),
                bookList.getTotalElements()
        );
    }

    @Override
    @Transactional
    @TrackAction(
            action = AuditAction.BOOK_DELETE,
            entityType = EntityType.BOOK,
            entityId = "#result.id",
            entityName = "#result.title"
    )
    public void deleteBookById(Long id) {
        Book book = bookRepository.findByIdWithCategory(id).orElseThrow(BookExceptions::bookNotFound);
        if (book.getQrCode() != null) {
            generatorService.deleteQRCodeImage(book.getQrCode());
            log.info("Deleted QR Code image for book: {}", id);
        }

        bookRepository.deleteById(id);
        log.info("Deleted book with ID: {}", id);
    }
}
