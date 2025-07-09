package com.example.management.repository;

import com.example.management.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.id = :id AND u.active = true")
    Optional<User> findByIdAndActive(@Param("id") Long id);

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    @Query("SELECT u FROM User u WHERE " +
            "(:email IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
            "(:phone IS NULL OR u.phone LIKE CONCAT('%', :phone, '%')) AND " +
            "(:isActive IS NULL OR u.active = :isActive)")
    Page<User> getAllUsersWithFilters(
            @Param("email") String email,
            @Param("phone") String phone,
            @Param("fullName") String fullName,
            @Param("isActive") Boolean isActive,
            Pageable pageable
    );
}