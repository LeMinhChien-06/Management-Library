package com.example.management.repository;

import com.example.management.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.category WHERE b.id = :id")
    Optional<Book> findByIdWithCategory(Long id);

    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.category WHERE " +
            "(:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
            "(:author IS NULL OR LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%'))) AND " +
            "(:publicationYear IS NULL OR b.publicationYear = :publicationYear) AND " +
            "(:categoryId IS NULL OR b.category.id = :categoryId)")
    Page<Book> getAllBooksWithFilters(
            @Param("title") String title,
            @Param("author") String author,
            @Param("publicationYear") Integer publicationYear,
            @Param("categoryId") Long categoryId,
            Pageable pageable
    );

    boolean existsByIsbn(String isbn);

    boolean existsByQrCode(String qrCode);

}