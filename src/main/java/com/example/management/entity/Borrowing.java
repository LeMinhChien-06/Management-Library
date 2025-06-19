package com.example.management.entity;

import com.example.management.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "borrowings")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Borrowing extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @Column(name = "borrow_date", nullable = false)
    LocalDate borrowDate; // Ngày mượn

    @Column(name = "due_date", nullable = false)
    LocalDate dueDate; // Ngày hết hạn

    @Column(name = "return_date")
    LocalDate returnDate; // Ngày trả

    @Enumerated(EnumType.STRING)
    Status status;

    @Column(columnDefinition = "TEXT")
    String notes;

    @OneToMany(mappedBy = "borrowing", cascade = CascadeType.ALL)
    List<BorrowingDetail> borrowingDetails;


}
