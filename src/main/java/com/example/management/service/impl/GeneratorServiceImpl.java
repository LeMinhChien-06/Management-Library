package com.example.management.service.impl;

import com.example.management.repository.BookRepository;
import com.example.management.service.GeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeneratorServiceImpl implements GeneratorService {

    private final BookRepository bookRepository;
    private final Random random = new Random();

    @Override
    public String generateIsbn() {
        String isbn;
        int attempts = 0;
        int maxAttempts = 100;

        do {
            isbn = generateISBN();
            attempts++;
            if (attempts > maxAttempts) {
                log.warn("Exceeded maximum attempts to generate unique ISBN. Last generated: {}", isbn);
                break;
            }
        } while (isIsbnExists(isbn));

        log.info("Generated ISBN: {}", isbn);
        return isbn;
    }

    @Override
    public boolean isIsbnExists(String isbn) {
        return bookRepository.existsByIsbn(isbn);
    }

    /**
     * Tạo ISBN theo format: 978-XXX-XXXX-XXX-X
     * Đây là format giả lập cho hệ thống nội bộ
     */
    private String generateISBN() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        // Lấy 3 số cuối của timestamp
        String part1 = timestamp.substring(timestamp.length() - 3);

        // Tạo 4 số random
        String part2 = String.format("%04d", random.nextInt(10000));

        // Tạo 3 số random
        String part3 = String.format("%03d", random.nextInt(1000));

        // Tạo 1 số check digit random
        String checkDigit = String.valueOf(random.nextInt(10));

        return String.format("978-%s-%s-%s-%s", part1, part2, part3, checkDigit);

    }
}
