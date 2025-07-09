package com.example.management.common.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApiLoggingFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (shouldSkip(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        
        String traceId = UUID.randomUUID().toString();
        long startTime = System.currentTimeMillis();
        
        try {
            logRequest(requestWrapper, traceId);
            filterChain.doFilter(requestWrapper, responseWrapper);
        } catch (Exception e) {
            logException(requestWrapper, traceId, e);
            throw e;
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            logResponse(requestWrapper, responseWrapper, traceId, duration);
            
            responseWrapper.copyBodyToResponse();
        }
    }
    
    private boolean shouldSkip(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.contains("/actuator") || 
               path.contains("/v3/api-docs") || 
               path.contains("/swagger-ui");
    }
    
    private void logRequest(ContentCachingRequestWrapper request, String traceId) {
        try {
            Object body = null;
            if (request.getContentLength() > 0) {
                body = extractRequestBody(request);
            }
            
            ApiLogModel logModel = ApiLogModel.builder()
                    .traceId(traceId)
                    .type("REQUEST")
                    .timestamp(LocalDateTime.now())
                    .httpMethod(request.getMethod())
                    .requestUri(request.getRequestURI() + 
                              (request.getQueryString() != null ? "?" + request.getQueryString() : ""))
                    .clientIp(getClientIp(request))
                    .userAgent(request.getHeader("User-Agent"))
                    .headers(getHeaders(request))
                    .body(body)
                    .build();
            
            log.info(objectMapper.writeValueAsString(logModel));
        } catch (Exception e) {
            log.error("Error logging request: {}", e.getMessage(), e);
        }
    }
    
    private void logResponse(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response, 
                            String traceId, long duration) {
        try {
            Object responseBody = extractResponseBody(response);
            
            ApiLogModel logModel = ApiLogModel.builder()
                    .traceId(traceId)
                    .type("RESPONSE")
                    .timestamp(LocalDateTime.now())
                    .httpMethod(request.getMethod())
                    .requestUri(request.getRequestURI())
                    .statusCode(response.getStatus())
                    .responseTimeMs(duration)
                    .headers(getResponseHeaders(response))
                    .body(responseBody)
                    .build();

            log.info(objectMapper.writeValueAsString(logModel));
        } catch (Exception e) {
            log.error("Error logging response: {}", e.getMessage(), e);
        }
    }
    
    private void logException(ContentCachingRequestWrapper request, String traceId, Exception e) {
        try {
            ApiLogModel logModel = ApiLogModel.builder()
                    .traceId(traceId)
                    .type("EXCEPTION")
                    .timestamp(LocalDateTime.now())
                    .httpMethod(request.getMethod())
                    .requestUri(request.getRequestURI())
                    .exception(e.getMessage())
                    .build();
                    
            log.error(objectMapper.writeValueAsString(logModel), e);
        } catch (Exception loggingEx) {
            log.error("Error logging exception: {}", loggingEx.getMessage(), loggingEx);
        }
    }
    
    private Object extractRequestBody(ContentCachingRequestWrapper request) {
        try {
            byte[] content = request.getContentAsByteArray();
            if (content.length > 0) {
                String contentType = request.getContentType();
                if (contentType != null && contentType.contains("application/json")) {
                    return objectMapper.readValue(content, Object.class);
                } else {
                    return new String(content);
                }
            }
        } catch (Exception e) {
            log.warn("Could not extract request body: {}", e.getMessage());
        }
        return null;
    }
    
    private Object extractResponseBody(ContentCachingResponseWrapper response) {
        try {
            byte[] content = response.getContentAsByteArray();
            if (content.length > 0) {
                String contentType = response.getContentType();
                if (contentType != null && contentType.contains("application/json")) {
                    return objectMapper.readValue(content, Object.class);
                } else {
                    return new String(content);
                }
            }
        } catch (Exception e) {
            log.warn("Could not extract response body: {}", e.getMessage());
        }
        return null;
    }
    
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
    
    private Map<String, String> getHeaders(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            if (!headerName.equalsIgnoreCase("Authorization") && !headerName.equalsIgnoreCase("Cookie")) {
                headers.put(headerName, request.getHeader(headerName));
            }
        }
        return headers;
    }
    
    private Map<String, String> getResponseHeaders(HttpServletResponse response) {
        Map<String, String> headers = new HashMap<>();
        Collection<String> headerNames = response.getHeaderNames();
        for (String headerName : headerNames) {
            headers.put(headerName, response.getHeader(headerName));
        }
        return headers;
    }
}