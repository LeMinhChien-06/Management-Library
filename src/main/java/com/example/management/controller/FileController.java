package com.example.management.controller;

import com.example.management.config.QRCodeConfigProperties;
import com.example.management.config.minio.MinioConfigProperties;
import com.example.management.entity.Book;
import com.example.management.exception.book.BookExceptions;
import com.example.management.repository.BookRepository;
import io.minio.GetObjectArgs;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
@Tag(name = "File Controller", description = "API xem ảnh")
public class FileController {

    private final QRCodeConfigProperties qrCodeConfigProperties;
    private final MinioConfigProperties minioConfigProperties;
    private final BookRepository bookRepository;

    @GetMapping("/qr/{fileName}")
    @Operation(summary = "Lấy ảnh QR", description = "API để lấy ảnh QR từ hệ thống lưu trữ")
    public ResponseEntity<Resource> getQRImage(@PathVariable String fileName) {
        try {
            Path filePath = Paths.get(qrCodeConfigProperties.getQrStoragePath()).resolve(fileName);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_PNG)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                        .header(HttpHeaders.CACHE_CONTROL, "max-age=3600")
                        .body(resource);
            } else {
                log.warn("QR image not found: {}", fileName);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Error retrieving QR image {}: {}", fileName, e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/image/{bookId}")
    @Operation(summary = "Lấy ảnh bìa sách", description = "Lấy ở bìa sách từ MinIO")
    public ResponseEntity<byte[]> getBookImage(@PathVariable Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(BookExceptions::bookNotFound);

        String imageUrl = book.getImageUrl();
        if (imageUrl == null || imageUrl.isBlank()) {
            log.warn("Book {} does not have an imageUrl", bookId);
            return ResponseEntity.notFound().build();
        }
        try {
            String objectName = extractObjectNameFromUrl(imageUrl);
            log.info("Extracted object name: {}", objectName);

            InputStream stream = minioConfigProperties.minioClient().getObject(
                    GetObjectArgs.builder()
                            .bucket(minioConfigProperties.getBucketName())
                            .object(objectName)
                            .build());

            byte[] imageData = stream.readAllBytes();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(imageData);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    public String extractObjectNameFromUrl(String imageUrl) {
        String[] parts = imageUrl.split("/");
        if (parts.length >= 3) {
            StringBuilder objectName = new StringBuilder();
            for (int i = 4; i < parts.length; i++) {
                if (i > 4) objectName.append("/");
                objectName.append(parts[i]);
            }
            return objectName.toString();
        }
        return null;
    }
}