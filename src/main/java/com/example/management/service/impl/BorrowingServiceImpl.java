package com.example.management.service.impl;

import com.example.management.annotation.TrackAction;
import com.example.management.dto.request.borrowing.ReturnBookRequest;
import com.example.management.dto.response.borrowing.BorrowingDetailResponseDto;
import com.example.management.dto.response.borrowing.ReturnBooksResponse;
import com.example.management.entity.Book;
import com.example.management.entity.Borrowing;
import com.example.management.entity.BorrowingDetail;
import com.example.management.enums.AuditAction;
import com.example.management.enums.EntityType;
import com.example.management.enums.Status;
import com.example.management.exception.borrowing.BorrowingExceptions;
import com.example.management.mapper.BorrowingDetailMapper;
import com.example.management.mapper.BorrowingMapper;
import com.example.management.repository.BookRepository;
import com.example.management.repository.BorrowingRepository;
import com.example.management.service.BorrowingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BorrowingServiceImpl implements BorrowingService {

    private final BookRepository bookRepository;
    private final BorrowingRepository borrowingRepository;
    private final BorrowingMapper borrowingMapper;

    @Override
    @Transactional
    @TrackAction(action = AuditAction.RETURNED_BORROWING, entityType = EntityType.BORROWING,
            entityId = "#result.borrowingId", entityName = "#result.userFullName")
    public ReturnBooksResponse returnBooks(Long borrowingId, ReturnBookRequest request) {
        Borrowing borrowing = borrowingRepository.findById(borrowingId).orElseThrow(BorrowingExceptions::borrowingNotFound);

        List<Long> bookIdsToReturn = request.getBookIds();
        if (bookIdsToReturn == null || bookIdsToReturn.isEmpty()) {
            // tra tat ca danh sach trong phieu muon
            bookIdsToReturn = borrowing.getBorrowingDetails().stream()
                    .filter(borrowingDetail -> borrowingDetail.getStatus() == Status.BORROWED)
                    .map(detail -> detail.getBook().getId())
                    .collect(Collectors.toList());
        }

        // lay nhung detail lien quan den bookID
        List<BorrowingDetail> detailsToReturn = borrowing.getBorrowingDetails().stream()
                .filter(borrowingDetail -> borrowingDetail.getStatus() == Status.BORROWED)
                .collect(Collectors.toList());

        if (detailsToReturn.isEmpty()) {
            throw BorrowingExceptions.noBooksToReturn();
        }

        detailsToReturn.forEach(detail -> {
            detail.setStatus(Status.RETURNED);
            Book book = detail.getBook();
            book.setAvailableQuantity(book.getAvailableQuantity() + 1);
        });

        bookRepository.saveAll(detailsToReturn.stream().map(BorrowingDetail::getBook).collect(Collectors.toList()));

        // Kiểm tra xem tất cả sách đã được trả chưa
        boolean allBooksReturned = borrowing.getBorrowingDetails().stream()
                .allMatch(detail -> detail.getStatus() == Status.RETURNED);

        if (allBooksReturned) {
            borrowing.setStatus(Status.RETURNED);
            borrowing.setNotes(request.getNotes());
            borrowing.setReturnDate(LocalDate.now());
        }

        borrowingRepository.save(borrowing);

        return borrowingMapper.returnBooks(borrowing, detailsToReturn);
    }

}
