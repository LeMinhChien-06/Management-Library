package com.example.management.dto.response;

import com.example.management.constants.MessageCode;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiResponse<T> {

    boolean success;
    String messageCode;
    String message;
    T data;
    List<String> errors;
    String error;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    LocalDateTime timestamp;
    String path;
    Integer statusCode;

    public static <T> ApiResponse<T> success(MessageCode messageCode) {
        return ApiResponse.<T>builder()
                .success(true)
                .statusCode(messageCode.getStatusCode())
                .messageCode(messageCode.getCode())
                .message(messageCode.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> success(MessageCode messageCode, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .statusCode(messageCode.getStatusCode())
                .messageCode(messageCode.getCode())
                .message(messageCode.getMessage())
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> success(T data){
        return success(MessageCode.SUCCESS, data);
    }

    public static <T>ApiResponse<T> success(){
        return success(MessageCode.SUCCESS);
    }

    public static <T> ApiResponse<T> error(MessageCode messageCode) {
        return ApiResponse.<T>builder()
                .success(false)
                .statusCode(messageCode.getStatusCode())
                .messageCode(messageCode.getCode())
                .message(messageCode.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> error(MessageCode messageCode, List<String> errors) {
        return ApiResponse.<T>builder()
                .success(false)
                .statusCode(messageCode.getStatusCode())
                .messageCode(messageCode.getCode())
                .message(messageCode.getMessage())
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> error(MessageCode messageCode, String path) {
        return ApiResponse.<T>builder()
                .success(false)
                .statusCode(messageCode.getStatusCode())
                .messageCode(messageCode.getCode())
                .message(messageCode.getMessage())
                .path(path)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> error(MessageCode messageCode, List<String> errors, String path) {
        return ApiResponse.<T>builder()
                .success(false)
                .statusCode(messageCode.getStatusCode())
                .messageCode(messageCode.getCode())
                .message(messageCode.getMessage())
                .errors(errors)
                .path(path)
                .timestamp(LocalDateTime.now())
                .build();
    }


    // Backward compatibility methods
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .timestamp(LocalDateTime.now())
                .statusCode(400)
                .build();
    }

    public static <T> ApiResponse<T> error(String message, Integer statusCode) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .statusCode(statusCode)
                .timestamp(LocalDateTime.now())
                .build();
    }

}
