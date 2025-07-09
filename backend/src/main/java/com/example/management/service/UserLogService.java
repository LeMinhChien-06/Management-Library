package com.example.management.service;

import com.example.management.entity.User;
import com.example.management.enums.AuditAction;
import jakarta.servlet.http.HttpServletRequest;

public interface UserLogService {

    void logAction(User user, String action, String entityType, Long entityId, String details, String ipAddress, String userAgent);

    void logSuccessfulLogin(User user, HttpServletRequest request);

    void logFailedLogin(String username, HttpServletRequest request);

    void logLogout(User user, String ipAddress, String userAgent);


}
