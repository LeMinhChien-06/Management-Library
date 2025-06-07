package com.example.management.repository;

import com.example.management.entity.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, String> {

    @Modifying
    @Transactional
    @Query("UPDATE UserSession s SET s.active = false WHERE s.username = :username AND s.active = true")
    void deactivateAllSessionsForUser(String username);

    boolean existsBySessionIdAndActiveTrue(String sessionId);

    // Optional: Cleanup expired sessions
    @Modifying
    @Transactional
    @Query("DELETE FROM UserSession s WHERE s.expiresAt < :now")
    int deleteExpiredSessions(LocalDateTime now);

    // Optional: Get active sessions for admin
    @Query("SELECT s FROM UserSession s WHERE s.active = true ORDER BY s.createdAt DESC")
    List<UserSession> findAllActiveSessions();

    // Optional: Count active sessions per user
    @Query("SELECT s.username, COUNT(s) FROM UserSession s WHERE s.active = true GROUP BY s.username")
    List<Object[]> countActiveSessionsByUser();
}