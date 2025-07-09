package com.example.management.repository;

import com.example.management.entity.Book;
import com.example.management.entity.BorrowingDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowingDetailRepository extends JpaRepository<BorrowingDetail, Long> {


}