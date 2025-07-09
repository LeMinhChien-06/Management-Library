package com.example.management.common.log;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiLogModel {
    private String traceId;
    private String type; // REQUEST or RESPONSE
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime timestamp;
    private String httpMethod;
    private String requestUri;
    private String clientIp;
    private String userAgent;
    private Map<String, String> headers;
    private Integer statusCode;
    private Long responseTimeMs;
    private Object body;
    private String exception;
}

