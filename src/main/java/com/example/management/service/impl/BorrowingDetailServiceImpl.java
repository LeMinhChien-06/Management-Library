package com.example.management.service.impl;

import com.example.management.annotation.TrackAction;
import com.example.management.dto.response.borrowing.BorrowingDetailResponseDto;
import com.example.management.entity.Book;
import com.example.management.entity.Borrowing;
import com.example.management.entity.BorrowingDetail;
import com.example.management.entity.User;
import com.example.management.enums.AuditAction;
import com.example.management.enums.EntityType;
import com.example.management.enums.Status;
import com.example.management.exception.borrowing.BorrowingExceptions;
import com.example.management.exception.user.UserExceptions;
import com.example.management.mapper.BorrowingDetailMapper;
import com.example.management.repository.BookRepository;
import com.example.management.repository.BorrowingDetailRepository;
import com.example.management.repository.BorrowingRepository;
import com.example.management.repository.UserRepository;
import com.example.management.service.BorrowingDetailService;
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
public class BorrowingDetailServiceImpl implements BorrowingDetailService {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final BorrowingRepository borrowingRepository;
    private final BorrowingDetailMapper borrowingDetailMapper;

    @Override
    @Transactional
    @TrackAction(action = AuditAction.BORROWING_CREATE, entityType = EntityType.BORROWING,
            entityId = "#result.borrowingId", entityName = "#result.userFullName")
    public BorrowingDetailResponseDto borrowBooks(Long userId, List<Long> bookIds, LocalDate dueDate) {
        if (bookIds == null || bookIds.isEmpty() || bookIds.size() > 3) {
            throw BorrowingExceptions.borrowBooks();
        }

        User user = userRepository.findById(userId).orElseThrow(UserExceptions::userNotFound);

        Borrowing borrowing = new Borrowing();
        borrowing.setUser(user);
        borrowing.setBorrowDate(LocalDate.now());
        borrowing.setDueDate(dueDate);

        List<Book> books = bookRepository.findAllById(bookIds);

        books.forEach(book -> {
            if (book.getAvailableQuantity() == null || book.getAvailableQuantity() <= 0) {
                throw BorrowingExceptions.bookOutOfStock(book.getId());
            }
        });

        List<BorrowingDetail> details = books.stream()
                .map(book -> {
                    BorrowingDetail borrowingDetail = new BorrowingDetail();
                    borrowingDetail.setBorrowing(borrowing);
                    borrowingDetail.setBook(book);
                    borrowingDetail.setStatus(Status.BORROWED);

                    book.setAvailableQuantity(book.getAvailableQuantity() - 1);

                    return borrowingDetail;
                })
                .collect(Collectors.toList());

        bookRepository.saveAll(books);

        borrowing.setBorrowingDetails(details);
        borrowingRepository.save(borrowing);

        return borrowingDetailMapper.borrowBooks(user, borrowing);
    }
}
