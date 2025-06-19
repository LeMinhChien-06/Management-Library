package com.example.management.repository;

import com.example.management.entity.Borrowing;
import com.example.management.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowingRepository extends JpaRepository<Borrowing, Long> {

    @Query("SELECT b FROM Borrowing b JOIN b.borrowingDetails bd WHERE b.user.id = :userId AND bd.status = :status")
    List<Borrowing> findByUserIdAndDetailStatus(@Param("userId")Long userId, @Param("status") Status status);

}