package com.example.management.controller;

import com.example.management.constants.MessageCode;
import com.example.management.dto.request.borrowing.ApproveRequest;
import com.example.management.dto.request.borrowing.BorrowingRequestDto;
import com.example.management.dto.request.borrowing.RejectRequest;
import com.example.management.dto.request.borrowing.ReturnBookRequest;
import com.example.management.dto.response.ApiResponse;
import com.example.management.dto.response.borrowing.BorrowingDetailResponseDto;
import com.example.management.dto.response.borrowing.ReturnBooksResponse;
import com.example.management.service.BorrowingDetailService;
import com.example.management.service.BorrowingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/borrowings")
@RequiredArgsConstructor
@Tag(name = "Borrowing Controller", description = "API quản lý mượn/trả/gia hạn sách")
public class BorrowingController {

    private final BorrowingDetailService borrowingDetailService;
    private final BorrowingService borrowingService;

    @PutMapping("/{borrowingId}/return")
//    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    @Operation(summary = "Trả sách", description = "Trả tất cả các sách theo phiếu mượn")
    public ApiResponse<ReturnBooksResponse> returnBooks(
            @Parameter(description = "Borrowing ID") @PathVariable Long borrowingId,
            @Valid @RequestBody ReturnBookRequest request) {


        return ApiResponse.success(MessageCode.RETURN_BOOK_SUCCESS, borrowingService.returnBooks(borrowingId, request));
    }

    @PostMapping("/borrow")
    @Operation(summary = "Tạo mới phiếu mượn sách", description = "API tạo phiếu mượn sách")
    public ApiResponse<BorrowingDetailResponseDto> borrowBooks(@Valid @RequestBody BorrowingRequestDto requestDto) {
        return ApiResponse.success(MessageCode.BORROW_CREATED_SUCCESS, borrowingDetailService.borrowBooks(requestDto.getUserId(), requestDto.getBookIds(), requestDto.getDueDate()));
    }

    @PostMapping("/{id}/approve")
    @Operation(summary = "Duyệt mượn sách", description = "API duyệt yêu cầu mượn sách của người dùng")
    public ApiResponse<BorrowingDetailResponseDto> approveBorrowing(@PathVariable Long id, @RequestBody ApproveRequest request) {
        return ApiResponse.success(MessageCode.APPROVE_SUCCESS, borrowingDetailService.approveBorrowing(id, true, null, request.getBookIds()));
    }

    @PostMapping("/{id}/reject")
    @Operation(summary = "Từ chối mượn sách", description = "API từ chối yêu cầu mượn sách của người dùng")
    public ApiResponse<BorrowingDetailResponseDto> rejectBorrowing(@PathVariable Long id, @RequestBody RejectRequest request) {
        return ApiResponse.success(MessageCode.REJECT_SUCCESS, borrowingDetailService.rejectBorrowing(id, false, request.getRejectReason(), null));
    }
}
