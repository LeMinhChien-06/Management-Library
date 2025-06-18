package com.example.management.controller;

import com.example.management.constants.MessageCode;
import com.example.management.dto.request.borrowing.BorrowingRequestDto;
import com.example.management.dto.response.ApiResponse;
import com.example.management.dto.response.borrowing.BorrowingDetailResponseDto;
import com.example.management.service.BorrowingDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/borrowings")
@RequiredArgsConstructor
@Tag(name = "Borrowing Controller", description = "API quản lý mượn/trả/gia hạn sách")
public class BorrowingController {

    private final BorrowingDetailService borrowingDetailService;


    @PostMapping("/borrow")
    @Operation(summary = "Tạo mới phiếu mượn sách", description = "API tạo phiếu mượn sách")
    public ApiResponse<BorrowingDetailResponseDto> borrowBooks(@Valid @RequestBody BorrowingRequestDto requestDto) {
        return ApiResponse.success(MessageCode.BORROW_CREATED_SUCCESS, borrowingDetailService.borrowBooks(requestDto.getUserId(), requestDto.getBookIds(), requestDto.getDueDate()));
    }
}
