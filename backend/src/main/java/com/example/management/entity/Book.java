package com.example.management.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "books")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Book extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String title;

    @Column(nullable = false)
    String author;

    @Column(unique = true, length = 20)
    String isbn; // Mã số chuẩn quốc tế: ISBN: 978-604-77-5555-1

    @Column(name = "qr_code", unique = true)
    String qrCode; // URL đến ảnh QR Code

    @Column(name = "total_quantity")
    Integer totalQuantity;

    @Column(name = "available_quantity")
    Integer availableQuantity; // Số lượng có sẵn

    @Column(name = "publication_year")
    Integer publicationYear; // Năm xuất bản

    String publisher; // Nhà xuất bản

    @Column(columnDefinition = "TEXT")
    String description;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    Categories category;


}
