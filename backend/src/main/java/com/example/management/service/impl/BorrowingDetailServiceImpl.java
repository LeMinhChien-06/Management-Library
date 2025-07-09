package com.example.management.service.impl;

import com.example.management.annotation.TrackAction;
import com.example.management.dto.request.borrowing.ReturnBookRequest;
import com.example.management.dto.response.borrowing.BorrowingDetailResponseDto;
import com.example.management.entity.Book;
import com.example.management.entity.Borrowing;
import com.example.management.entity.BorrowingDetail;
import com.example.management.entity.User;
import com.example.management.enums.AuditAction;
import com.example.management.enums.EntityType;
import com.example.management.enums.Role;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
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

        User user = userRepository.findByIdAndActive(userId).orElseThrow(UserExceptions::userNotFound);

        List<Borrowing> activeBorrowings = borrowingRepository.findByUserIdAndDetailStatus(userId, Status.BORROWED);

        int currentBorrowingCount = activeBorrowings.stream()
                .mapToInt(b -> b.getBorrowingDetails().size())
                .sum();

        if (currentBorrowingCount + bookIds.size() > 5) {
            throw BorrowingExceptions.exceedsMaxBorrowLimit();
        }

        Set<Long> alreadyBorrowedBookIds = activeBorrowings.stream()
                .flatMap(b -> b.getBorrowingDetails().stream())
                .map(d -> d.getBook().getId())
                .collect(Collectors.toSet());

        List<Long> duplicateBooks = bookIds.stream()
                .filter(alreadyBorrowedBookIds::contains)
                .collect(Collectors.toList());

        if (!duplicateBooks.isEmpty()) {
            throw BorrowingExceptions.booksAlreadyBorrowed();
        }

        Borrowing borrowing = new Borrowing();
        borrowing.setUser(user);
        borrowing.setBorrowDate(LocalDate.now());
        borrowing.setStatus(Status.PENDING);

        if (dueDate.isBefore(LocalDate.now()) ||
                dueDate.isAfter(LocalDate.now().plusDays(30))) {
            throw BorrowingExceptions.invalidDueDate();
        }

        borrowing.setDueDate(dueDate);

        borrowingRepository.save(borrowing);

        return borrowingDetailMapper.borrowBooks(user, borrowing, bookIds);
    }

    @Override
    @Transactional
    @TrackAction(action = AuditAction.APPROVED_BORROWING, entityType = EntityType.BORROWING,
            entityId = "#result.borrowingId", entityName = "#result.userFullName")
    public BorrowingDetailResponseDto approveBorrowing(Long borrowingId, Boolean approved, String rejectReason, List<Long> bookIds) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userRepository.findByUsername(authentication.getName()).orElseThrow(UserExceptions::userNotFound);

        // kiểm tra quyền
        if (!currentUser.getRole().equals(Role.ADMIN) && !currentUser.getRole().equals(Role.LIBRARIAN)) {
            throw UserExceptions.permissionDenied();
        }

        Borrowing borrowing = borrowingRepository.findById(borrowingId).orElseThrow(BorrowingExceptions::borrowingNotFound);

        if (!borrowing.getStatus().equals(Status.PENDING)) {
            throw BorrowingExceptions.invalidStatus();
        }

        if (approved) {
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
            borrowing.setStatus(Status.APPROVED);

            borrowingRepository.save(borrowing);

        }

        return borrowingDetailMapper.approveBorrowBooks(currentUser, borrowing);
    }

    @Override
    @Transactional
    @TrackAction(action = AuditAction.REJECTED_BORROWING, entityType = EntityType.BORROWING,
            entityId = "#result.borrowingId", entityName = "#result.userFullName")
    public BorrowingDetailResponseDto rejectBorrowing(Long borrowingId, Boolean approved, String rejectReason, List<Long> bookIds) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userRepository.findByUsername(authentication.getName()).orElseThrow(UserExceptions::userNotFound);

        // kiểm tra quyền
        if (!currentUser.getRole().equals(Role.ADMIN) && !currentUser.getRole().equals(Role.LIBRARIAN)) {
            throw UserExceptions.permissionDenied();
        }

        Borrowing borrowing = borrowingRepository.findById(borrowingId).orElseThrow(BorrowingExceptions::borrowingNotFound);

        if (!borrowing.getStatus().equals(Status.PENDING)) {
            throw BorrowingExceptions.invalidStatus();
        }

        borrowing.setStatus(Status.REJECTED);
        borrowing.setNotes(rejectReason);

        borrowingRepository.save(borrowing);

        return borrowingDetailMapper.approveBorrowBooks(currentUser, borrowing);
    }

}
