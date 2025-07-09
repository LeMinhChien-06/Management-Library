package com.example.management.service.impl;

import com.example.management.annotation.TrackAction;
import com.example.management.entity.User;
import com.example.management.entity.UserLog;
import com.example.management.enums.AuditAction;
import com.example.management.enums.EntityType;
import com.example.management.repository.UserLogRepository;
import com.example.management.service.DeviceDetectionService;
import com.example.management.service.UserLogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserLogServiceImpl implements UserLogService {

    private final UserLogRepository userLogRepository;
    private final DeviceDetectionService deviceDetectionService;

    @Override
    public void logAction(User user, String action, String entityType, Long entityId, String details, String ipAddress, String userAgent) {
        try {
            UserLog userLog = new UserLog();
            userLog.setUser(user);
            userLog.setAction(action);
            userLog.setEntityType(entityType);
            userLog.setEntityId(entityId);
            userLog.setDetails(details);
            userLog.setIpAddress(ipAddress);
            userLog.setUserAgent(userAgent);
            userLog.setCreatedAt(LocalDateTime.now());

            userLogRepository.save(userLog);

            log.info("Audit: User '{}' performed '{}' on {} ID: {} ({})",
                    user.getUsername(), action, entityType, entityId, details);

        } catch (Exception e) {
            log.error("Failed to save audit log for user: {}, action: {}",
                    user.getUsername(), action, e);
        }
    }

    @Override
    public void logSuccessfulLogin(User user, HttpServletRequest request) {
        String ipAddress = deviceDetectionService.extractClientIpAddress(request);
        String userAgent = request.getHeader("User-Agent");
        String deviceInfo = deviceDetectionService.extractDeviceInfo(userAgent);
        String browserInfo = deviceDetectionService.extractBrowserInfo(userAgent);

        String details = String.format("Login successful from %s using %s", deviceInfo, browserInfo);

        logAction(user, String.valueOf(AuditAction.LOGIN), String.valueOf(EntityType.USER), user.getId(), details, ipAddress, userAgent);

        log.info("User {} logged in from {} using {}",
                user.getUsername(), deviceInfo, browserInfo);

    }

    @Override
    public void logFailedLogin(String username, HttpServletRequest request) {
        String ipAddress = deviceDetectionService.extractClientIpAddress(request);
        String userAgent = request.getHeader("User-Agent");
        String deviceInfo = deviceDetectionService.extractDeviceInfo(userAgent);
        String browserInfo = deviceDetectionService.extractBrowserInfo(userAgent);

        String details = String.format("Failed login attempt for username '%s' from %s using %s",
                username, deviceInfo, browserInfo);

        // Create system user for failed attempts
        User systemUser = createSystemUser();

        logAction(systemUser, String.valueOf(AuditAction.LOGIN_FAILED), String.valueOf(EntityType.USER), null, details, ipAddress, userAgent);

        log.warn("Failed login attempt for username: {} from {} using {}",
                username, deviceInfo, browserInfo);

    }

    @Override
    public void logLogout(User user, String ipAddress, String userAgent) {
        String deviceInfo = deviceDetectionService.extractDeviceInfo(userAgent);
        String browserInfo = deviceDetectionService.extractBrowserInfo(userAgent);
        String details = String.format("Logout from %s using %s", deviceInfo, browserInfo);

        logAction(user, String.valueOf(AuditAction.LOGOUT), String.valueOf(EntityType.USER), user.getId(), details, ipAddress, userAgent);

        log.info("User {} logged out from {} using {}",
                user.getUsername(), deviceInfo, browserInfo);

    }

    /**
     * Create system user for system-generated logs (like failed login attempts)
     */
    private User createSystemUser() {
        User systemUser = new User();
        systemUser.setId(0L);
        systemUser.setUsername("SYSTEM");
        return systemUser;
    }

}
