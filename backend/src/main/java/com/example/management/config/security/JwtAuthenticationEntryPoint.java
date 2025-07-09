package com.example.management.config.security;

import com.example.management.constants.MessageCode;
import com.example.management.dto.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        MessageCode messageCode = MessageCode.UNAUTHORIZED;

        // Set response headers
        response.setStatus(messageCode.getStatusCode());
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .success(false)
                .statusCode(messageCode.getStatusCode())
                .messageCode(messageCode.getCode())
                .message(messageCode.getMessage())
                .path("uri=" + request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();


        // Convert to JSON và ghi response
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        String jsonResponse = mapper.writeValueAsString(apiResponse);
        log.warn("Sending response: {}", jsonResponse);

        response.getWriter().write(jsonResponse);
        response.getWriter().flush(); // ép buộc gửi dữ liệu ngay lập tức
    }
}