package com.example.management.service;

public interface GeneratorService {

    /**
     * Tạo ISBN duy nhất cho sách mới
     * @return ISBN string theo format "978-1-xxxx-xxxx-x"
     */
    String generateIsbn();

    /**
     * Kiểm tra xem ISBN đã tồn tại hay chưa
     * @param isbn ISBN string theo format "978-1-xxxx-xxxx-x"
     * @return true nếu ISBN đã tồn tại, false nếu chưa tồn tại
     */
    boolean isIsbnExists(String isbn);
}
