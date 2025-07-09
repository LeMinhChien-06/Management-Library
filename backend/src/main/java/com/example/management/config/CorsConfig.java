package com.example.management.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {

    public static final String[] CORS_ALLOWED_ORIGINS = {
            "http://localhost:4200",
    };

    public static final String[] CORS_ALLOWED_METHODS = {
            "GET", "POST", "PUT", "DELETE", "OPTIONS"
    };

    public static final String[] CORS_ALLOWED_HEADERS = {
            "Content-Type", "Authorization", "X-Requested-With", "Accept", "Origin", "Access-Control-Request-Method",
            "Access-Control-Request-Headers"
    };

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of(CORS_ALLOWED_ORIGINS));
        corsConfiguration.setAllowedMethods(List.of(CORS_ALLOWED_METHODS));
        corsConfiguration.setAllowedHeaders(List.of(CORS_ALLOWED_HEADERS));
        corsConfiguration.setAllowCredentials(true); // hỗ trợ thông tin đăng nhập

        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return urlBasedCorsConfigurationSource;
    }
}
