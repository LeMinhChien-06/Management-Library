package com.example.management.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_sessions", indexes = {
        @Index(name = "idx_username_active", columnList = "username, active"),
        @Index(name = "idx_session_id_active", columnList = "sessionId, active"),
        @Index(name = "idx_expires_at", columnList = "expiresAt"), // Để cleanup expired sessions
        @Index(name = "idx_created_at", columnList = "createdAt")   // Để query by time range
})
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserSession {

    @Id
    @Column(length = 36)
    String sessionId; // UUID

    @Column(nullable = false, length = 50)
    String username;

    @Column(nullable = false)
    @Builder.Default
    LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    LocalDateTime expiresAt;

    @Builder.Default
    @Column(nullable = false)
    Boolean active = true;

    // Tracking info
    @Column(length = 45) // IPv6 max length
            String ipAddress;

    @Column(length = 500)
    String userAgent;

    @Column(length = 100) // Device fingerprint hoặc device name
    String deviceInfo;

    @Column
    LocalDateTime lastAccessedAt; // Track activity
}
