package com.example.management.service.impl;

import com.example.management.config.QRCodeConfigProperties;
import com.example.management.repository.BookRepository;
import com.example.management.service.QRCodeService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class QRCodeServiceImpl implements QRCodeService {

    private final BookRepository bookRepository;
    private final QRCodeConfigProperties qrCodeConfigProperties;
    private final Random random = new Random();


    @Override
    public String generateUniqueQRCode(Long bookId, String title, String isbn) {
        log.info("Generating unique QR Code for book ID: {}", bookId);

        // 1. Tạo QR data unique
        String qrData = createUniqueQRData(bookId, title, isbn);

        // 2. Tạo ảnh QR Code
        String qrImageUrl = createQRCodeImage(qrData, bookId);

        log.info("Generated QR Code: {} -> Image URL: {}", qrData, qrImageUrl);
        return qrImageUrl;

    }

    @Override
    public String generateTemporaryQRCode(String title, String author, String isbn) {
        log.info("Generating temporary QR Code for book: {}", title);

        // Tạo temporary ID unique
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);

        return String.format("TEMP_BOOK_%s_%s_%s_%s",
                cleanString(title),
                cleanString(author),
                isbn != null ? isbn : "NO_ISBN",
                timestamp + "_" + uniqueId
        );

    }

    @Override
    public String updateToFinalQRCode(Long bookId, String title, String isbn, String temporaryQRCode) {
        log.info("Updating to final QR Code for book ID: {}", bookId);

        // Tạo QR Code chính thức
        return generateUniqueQRCode(bookId, title, isbn);
    }

    @Override
    public boolean isQRCodeExists(String qrCode) {
        return bookRepository.existsByQrCode(qrCode);
    }

    @Override
    public void deleteQRCodeImage(String qrCodeUrl) {
        if (qrCodeUrl == null || qrCodeUrl.isEmpty()) {
            return;
        }

        try {
            // Extract filename from URL
            String fileName = qrCodeUrl.substring(qrCodeUrl.lastIndexOf("/") + 1);
            Path filePath = Paths.get(qrCodeConfigProperties.getQrStoragePath(), fileName);

            if (Files.exists(filePath)) {
                Files.delete(filePath);
                log.info("Deleted QR Code image: {}", filePath);
            }
        } catch (Exception e) {
            log.error("Error deleting QR Code image: {}", qrCodeUrl, e);
        }
    }

    /**
     * Tạo QR data unique cho sách
     */
    private String createUniqueQRData(Long bookId, String title, String isbn) {
        // Format: BOOK|{bookId}|{title}|{isbn}|{timestamp}|{uniqueHash}
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String uniqueHash = generateUniqueHash(bookId, title, isbn);

        return String.format("BOOK|%d|%s|%s|%s|%s",
                bookId,
                cleanString(title),
                isbn != null ? isbn : "NO_ISBN",
                timestamp,
                uniqueHash
        );
    }

    /**
     * Tạo hash unique để đảm bảo QR Code không trùng
     */
    private String generateUniqueHash(Long bookId, String title, String isbn) {
        String baseString = String.format("%d_%s_%s_%d",
                bookId,
                title,
                isbn,
                System.currentTimeMillis()
        );

        return UUID.nameUUIDFromBytes(baseString.getBytes()).toString().substring(0, 8).toUpperCase();
    }

    /**
     * Tạo ảnh QR Code và lưu vào storage
     */
    private String createQRCodeImage(String qrData, Long bookId) {
        try {
            // 1. Tạo thư mục nếu chưa có
            Path uploadDir = Paths.get(qrCodeConfigProperties.getQrStoragePath());
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
                log.info("Created QR storage directory: {}", uploadDir);
            }

            // 2. Tạo tên file unique
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            String fileName = String.format("qr_book_%d_%s_%s.png",
                    bookId,
                    timestamp,
                    UUID.randomUUID().toString().substring(0, 6)
            );

            Path filePath = uploadDir.resolve(fileName);

            // 3. Tạo QR Code matrix
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(
                    qrData,
                    BarcodeFormat.QR_CODE,
                    qrCodeConfigProperties.getQrImageSize(),
                    qrCodeConfigProperties.getQrImageSize()
            );

            // 4. Lưu thành ảnh PNG
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", filePath);

            // 5. Tạo URL để truy cập
            String qrImageUrl = qrCodeConfigProperties.getQrBaseUrl() + "/" + fileName;

            log.info("Created QR Code image: {} -> URL: {}", filePath, qrImageUrl);
            return qrImageUrl;

        } catch (WriterException | IOException e) {
            log.error("Error creating QR Code image for book ID {}: {}", bookId, e.getMessage(), e);
            throw new RuntimeException("Failed to create QR Code image", e);
        }
    }

    /**
     * Làm sạch chuỗi để dùng trong QR Code
     */
    private String cleanString(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "UNKNOWN";
        }

        return input.trim()
                .replaceAll("[^\\p{L}\\p{N}\\s]", "") // Chỉ giữ chữ cái, số và khoảng trắng
                .replaceAll("\\s+", "_") // Thay khoảng trắng bằng underscore
                .substring(0, Math.min(input.length(), 30)); // Giới hạn độ dài
    }

}
