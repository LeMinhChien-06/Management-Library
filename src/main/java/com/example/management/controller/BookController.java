package com.example.management.controller;

import com.example.management.constants.MessageCode;
import com.example.management.dto.request.books.BookCreateRequest;
import com.example.management.dto.request.books.BookUpdateRequest;
import com.example.management.dto.response.ApiResponse;
import com.example.management.dto.response.PageDTO;
import com.example.management.dto.response.books.BookResponse;
import com.example.management.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
@Tag(name = "Book Controller", description = "API quản lý sách")
public class BookController {

    private final BookService bookService;

    @PostMapping("/create")
    @Operation(summary = "Tạo sách", description = "API để tạo một sách mới")
    public ApiResponse<BookResponse> createBook(@Valid @RequestPart("book") BookCreateRequest bookCreateRequest,
                                                @RequestPart("file") MultipartFile fileImage
    ) throws IOException {
        return ApiResponse.success(MessageCode.BOOK_CREATED_SUCCESS, bookService.creatBook(bookCreateRequest, fileImage));
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Cập nhật thông tin sách", description = "API để cập nhật thông tin sách")
    public ApiResponse<BookResponse> updateBook(@Valid @PathVariable @Min((1)) Long id, @RequestBody BookUpdateRequest bookUpdateRequest) {
        return ApiResponse.success(MessageCode.BOOK_UPDATED_SUCCESS, bookService.updateBook(id, bookUpdateRequest));
    }


    @GetMapping("/{id}")
    @Operation(summary = "Lấy thông tin chi tiết sách", description = "API để lấy thông tin chi tiết sách")
    public ApiResponse<BookResponse> getBookById(@PathVariable @Min((1)) Long id) {
        return ApiResponse.success(MessageCode.BOOK_DETAIL_SUCCESS, bookService.getBookById(id));
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Xóa sách", description = "API để xóa một sách")
    public ApiResponse<Void> deleteBookById(@PathVariable @Min((1)) Long id) {
        bookService.deleteBookById(id);
        return ApiResponse.success(MessageCode.BOOK_DELETED_SUCCESS);
    }

    @GetMapping("/all")
    @Operation(summary = "Lấy danh sách các sách", description = "API để lấy danh sách các sách" +
            " với khả năng phân trang, tìm kiếm theo title/author và lọc theo publicationYear/category")
    public ApiResponse<PageDTO<BookResponse>> getAllBooks(
            @RequestParam(required = false, defaultValue = "0") @Min(0) int page,
            @RequestParam(required = false, defaultValue = "10") @Min(1) int size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) Integer publicationYear,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDirection
    ) {
        return ApiResponse.success(MessageCode.BOOK_LIST_SUCCESS, bookService.getAllBooks(page, size, sortBy, sortDirection,
                title, author, publicationYear, categoryId));
    }
}
