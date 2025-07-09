package com.example.management.task;

import com.example.management.entity.Borrowing;
import com.example.management.entity.BorrowingDetail;
import com.example.management.enums.Status;
import com.example.management.repository.BorrowingDetailRepository;
import com.example.management.repository.BorrowingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OverdueTask {

    private final BorrowingDetailRepository borrowingDetailRepository;

    /**
     * Tự động cập nhật trạng thái quá hạn - Scheduled task to update overdue status
     * Chạy hàng ngày lúc 00:01
     */
    @Scheduled(cron = "0 1 0 * * ?")
    @Transactional
    public void updateOverdueStatus() {
        List<BorrowingDetail> details = borrowingDetailRepository
                .findOverdueBorrowingDetails(Status.BORROWED, LocalDate.now());

        for (BorrowingDetail detail : details) {
            detail.setStatus(Status.OVERDUE);
        }
        borrowingDetailRepository.saveAll(details);
    }
}


