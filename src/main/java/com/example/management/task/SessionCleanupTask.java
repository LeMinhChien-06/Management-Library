package com.example.management.task;

import com.example.management.repository.UserSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class SessionCleanupTask {

    private final UserSessionRepository userSessionRepository;

    @Scheduled(cron = "0 0 2 * * ?") // Chạy lúc 2h sáng hàng ngày
    public void cleanupExpiredSessions() {
        userSessionRepository.deleteExpiredSessions(LocalDateTime.now());
        log.info(" Cleaned up expired sessions at {}", LocalDateTime.now());
    }
}