package com.example.management.service.impl;

import com.example.management.config.QRCodeConfigProperties;
import com.example.management.config.minio.MinioConfigProperties;
import com.example.management.repository.BookRepository;
import com.example.management.service.GeneratorService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeneratorServiceImpl implements GeneratorService {

    private final BookRepository bookRepository;
    private final QRCodeConfigProperties qrCodeConfigProperties;
    private final MinioConfigProperties minioConfigProperties;
    private final MinioClient minioClient;
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

    @Override
    public boolean isIsbnExists(String isbn) {
        return bookRepository.existsByIsbn(isbn);
    }

    @Override
    public String generateUniqueQRCode(Long bookId, String title, String isbn) {
        log.info("Generating unique QR Code for book ID: {}", bookId);

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

        return null;
    }

    @Override
    public boolean isQRCodeExists(String qrCode) {
        return false;
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

    @Override
    public String createQRCodeImage(String qrData, Long bookId) {
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


    @Override
    public String addQRCodeAndUploadToMinio(InputStream inputStream, String qrData, String objectName) {
        try {
            // 1. Đọc ảnh từ InputStream
            BufferedImage bufferedImage = javax.imageio.ImageIO.read(inputStream);

            // 2. Tạo ảnh QRCode
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(
                    qrData,
                    BarcodeFormat.QR_CODE,
                    qrCodeConfigProperties.getQrImageSize(),
                    qrCodeConfigProperties.getQrImageSize()
            );
            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

            // 3. Chèn QR vào góc dưới bên phải
            Graphics2D graphics2D = bufferedImage.createGraphics();
            int x = bufferedImage.getWidth() - qrImage.getWidth() - 10;
            int y = bufferedImage.getHeight() - qrImage.getHeight() - 10;
            graphics2D.drawImage(qrImage, x, y, null);
            graphics2D.dispose();

            // 4. Chuyển ảnh đã merge thành byte[]
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
            byte[] imageBytes = byteArrayOutputStream.toByteArray();

            // 5. Upload lên MinIO
            try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes)) {
                minioClient.putObject(PutObjectArgs.builder()
                        .bucket(minioConfigProperties.getBucketName())
                        .object(objectName)
                        .stream(byteArrayInputStream, imageBytes.length, -1)
                        .contentType("image/png")
                        .build());
            }

            // 6. Trả về URL ảnh public bucket
            return String.format("%s/%s/%s",
                    minioConfigProperties.getEndpoint(),
                    minioConfigProperties.getBucketName(),
                    objectName);

        } catch (Exception e) {
            log.error("Error uploading QR Code image to MinIO: {}", objectName, e);
            throw new RuntimeException("Failed to upload QR Code image to MinIO", e);
        }
    }


    /**
     * Tạo QR data unique cho sách
     */
    public String createUniqueQRData(Long bookId, String title, String isbn) {
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
