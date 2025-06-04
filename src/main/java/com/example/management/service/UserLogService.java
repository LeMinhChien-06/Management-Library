package com.example.management.service;

import com.example.management.entity.User;

public interface UserLogService {

    void logUserActivity(User user, String action, String entityType, Long entityId, String details, String ipAddress, String userAgent);


}
