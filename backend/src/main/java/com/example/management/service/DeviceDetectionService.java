package com.example.management.service;

import jakarta.servlet.http.HttpServletRequest;

public interface DeviceDetectionService {

    String extractClientIpAddress(HttpServletRequest request);

    String extractDeviceInfo(String userAgent);

    String extractBrowserInfo(String userAgent);
}
