package com.example.management.repository;

import com.example.management.entity.UserLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Repository
public interface UserLogRepository extends JpaRepository<UserLog, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM UserLog l WHERE l.createdAt < :now")
    void deleteAllByCreatedAt(LocalDateTime now);
}