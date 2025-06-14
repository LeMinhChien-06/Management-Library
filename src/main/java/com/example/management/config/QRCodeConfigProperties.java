package com.example.management.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app")
public class QRCodeConfigProperties {

    private String qrStoragePath;

    private String qrBaseUrl;

    private int qrImageSize;

}
