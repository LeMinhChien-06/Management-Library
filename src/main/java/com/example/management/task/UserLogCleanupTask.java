package com.example.management.task;

import com.example.management.repository.UserLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserLogCleanupTask {

    private final UserLogRepository userLogRepository;

    @Scheduled(cron = "0 0 2 * * ?")
//    @Scheduled(fixedRate = 10000)
    public void cleanupExpiredLogs() {
        userLogRepository.deleteAllByCreatedAt(LocalDateTime.now().minusYears(1));
        log.info(" Cleaned up expired logs at {}", LocalDateTime.now());
    }
}
