package com.example.management.controller;

import com.example.management.config.QRCodeConfigProperties;
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

import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
@Tag(name = "File Controller", description = "API xem ảnh")
public class FileController {

    private final QRCodeConfigProperties qrCodeConfigProperties;

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
}