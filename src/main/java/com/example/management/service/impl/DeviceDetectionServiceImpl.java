package com.example.management.service.impl;

import com.example.management.service.DeviceDetectionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DeviceDetectionServiceImpl implements DeviceDetectionService {

    /**
     * Trích xuất  Ip address từ HTTP Request
     */
    @Override
    public String extractClientIpAddress(HttpServletRequest request) {
        // Kiểm tra header X-Forwarded-For ( khi qua proxy/load balancer )
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unKnown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }

        // Kiểm tra header X-Real-IP ( khi qua nginx )
        String xRealIpHeader = request.getHeader("X-Real-IP");
        if (xRealIpHeader != null && !xRealIpHeader.isEmpty() && !"unKnown".equalsIgnoreCase(xRealIpHeader)) {
            return xRealIpHeader;
        }

        // Kiểm tra header khác
        String xForwardedHeader = request.getHeader("X-Forwarded");
        if (xForwardedHeader != null && !xForwardedHeader.isEmpty() && !"unKnown".equalsIgnoreCase(xForwardedHeader)) {
            return xForwardedHeader;
        }

        return request.getRemoteAddr();
    }

    /**
     * Phân tích thông tin thiết bị từ User-Agent
     *
     * @param userAgent
     * @return
     */
    @Override
    public String extractDeviceInfo(String userAgent) {
        if (userAgent == null || userAgent.isEmpty()) {
            return "Unknown Device";
        }

        userAgent = userAgent.toLowerCase();

        // Mobile device
        if (userAgent.contains("mobile") || userAgent.contains("android")) {
            if (userAgent.contains("android")) {
                return "Android Mobile";
            }
            return "Mobile Device";
        }

        // Tables
        if (userAgent.contains("tablet") || userAgent.contains("ipad")) {
            if (userAgent.contains("ipad")) {
                return "iPad";
            }
            return "Tablet";
        }

        // Desktop Operating System
        if (userAgent.contains("windows")) {
            return "Windows PC";
        } else if (userAgent.contains("mac") && !userAgent.contains("iphone")) {
            return "Mac";
        } else if (userAgent.contains("linux")) {
            return "Linux PC";
        } else if (userAgent.contains("iphone")) {
            return "iPhone";
        }

        return "Desktop/Unknown";
    }

    /**
     * Trích xuất thông tin trình duyệt từ User-Agent
     *
     * @param userAgent
     * @return
     */
    @Override
    public String extractBrowserInfo(String userAgent) {
        if (userAgent == null || userAgent.isEmpty()) {
            return "Unknown Browser";
        }

        userAgent = userAgent.toLowerCase();

        if (userAgent.contains("chrome") && !userAgent.contains("edge")) {
            return "Chrome";
        } else if (userAgent.contains("firefox")) {
            return "Firefox";
        } else if (userAgent.contains("safari") && !userAgent.contains("chrome")) {
            return "Safari";
        } else if (userAgent.contains("edge")) {
            return "Edge";
        } else if (userAgent.contains("opera")) {
            return "Opera";
        }

        return "Unknown Browser";

    }
}
