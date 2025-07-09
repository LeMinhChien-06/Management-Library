package com.example.management.exception;

import com.example.management.constants.MessageCode;
import com.example.management.dto.response.ApiResponse;
import com.example.management.exception.auth.AuthenticationExceptions;
import com.example.management.utils.ValidationErrorMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import jakarta.validation.ConstraintViolationException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class) // bắt validation
    public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException ex, WebRequest request) {

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> ValidationErrorMapper.mapFieldErrors(
                        fieldError.getField(),
                        fieldError.getDefaultMessage()
                ))
                .collect(Collectors.toList());

        ApiResponse<Void> response = ApiResponse.error(
                MessageCode.VALIDATION_ERROR,
                errors,
                request.getDescription(false)
        );

        log.error("Validation error: {}", errors);
        return ResponseEntity.status(MessageCode.VALIDATION_ERROR.getStatusCode()).body(response);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ApiResponse<Void>> handleHandlerMethodValidationException(
            HandlerMethodValidationException ex, WebRequest request) {

        List<String> errors = ex.getValueResults()
                .stream()
                .flatMap(result -> result.getResolvableErrors().stream())
                .map(error -> {
                    String message = error.getDefaultMessage();
                    // Lấy tên parameter từ codes nếu có
                    String[] codes = error.getCodes();
                    String parameterName = "parameter";
                    if (codes != null && codes.length > 0) {
                        String code = codes[0];
                        if (code.contains(".")) {
                            String[] parts = code.split("\\.");
                            if (parts.length > 1) {
                                parameterName = parts[parts.length - 1];
                            }
                        }
                    }
                    return ValidationErrorMapper.mapFieldErrors(parameterName, message != null ? message : "Invalid value");
                })
                .collect(Collectors.toList());

        ApiResponse<Void> response = ApiResponse.error(
                MessageCode.VALIDATION_ERROR,
                errors,
                request.getDescription(false)
        );

        log.error("Handler method validation error: {}", errors);
        return ResponseEntity.status(MessageCode.VALIDATION_ERROR.getStatusCode()).body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolationException(
            ConstraintViolationException ex, WebRequest request) {

        List<String> errors = ex.getConstraintViolations()
                .stream()
                .map(violation -> ValidationErrorMapper.mapPropertyPath(
                        violation.getMessage(),
                        violation.getPropertyPath().toString()
                ))
                .collect(Collectors.toList());

        ApiResponse<Void> response = ApiResponse.error(
                MessageCode.VALIDATION_ERROR,
                errors,
                request.getDescription(false)
        );

        log.warn("Constraint violation: {}", errors);
        return ResponseEntity.status(MessageCode.VALIDATION_ERROR.getStatusCode()).body(response);
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<Void>> handleAppException(
            AppException ex, WebRequest request) {

        ApiResponse<Void> response = ApiResponse.error(
                ex.getMessageCode(),
                request.getDescription(false)
        );

        log.warn("App exception: {} - {}", ex.getErrorCode(), ex.getMessage());
        return ResponseEntity.status(ex.getStatusCode()).body(response);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodNotSupportedException(
            HttpRequestMethodNotSupportedException ex, WebRequest request) {

        ApiResponse<Void> response = ApiResponse.error(
                MessageCode.METHOD_NOT_ALLOWED,
                request.getDescription(false)
        );

        log.warn("Method not allowed: {}", ex.getMethod());
        return ResponseEntity.status(MessageCode.METHOD_NOT_ALLOWED.getStatusCode()).body(response);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNoHandlerFoundException(
            NoHandlerFoundException ex, WebRequest request) {

        ApiResponse<Void> response = ApiResponse.error(
                MessageCode.ENDPOINT_NOT_FOUND,
                request.getDescription(false)
        );

        log.warn("Endpoint not found: {}", ex.getRequestURL());
        return ResponseEntity.status(MessageCode.ENDPOINT_NOT_FOUND.getStatusCode()).body(response);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiResponse<Void>> handleNullPointerExceptions(NullPointerException ex, WebRequest request) {
        ApiResponse<Void> response = ApiResponse.error(
                MessageCode.DATA_NULL_ERROR,
                request.getDescription(false)
        );

        log.error("Null pointer exception: ", ex);
        return ResponseEntity.status(MessageCode.DATA_NULL_ERROR.getStatusCode()).body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex, WebRequest request) {

        String errorMessage = "Invalid JSON format";
        List<String> errors = List.of(errorMessage);

        // Kiểm tra các loại lỗi JSON phổ biến
        Throwable cause = ex.getCause();
        if (cause instanceof UnrecognizedPropertyException) {
            // Lỗi field không được nhận diện
            UnrecognizedPropertyException unrecognizedEx = (UnrecognizedPropertyException) cause;
            String fieldName = unrecognizedEx.getPropertyName();
            String className = unrecognizedEx.getReferringClass().getSimpleName();

            // Lấy danh sách các field hợp lệ
            String knownFields = unrecognizedEx.getKnownPropertyIds()
                    .stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(", "));

            errorMessage = String.format("Field '%s' không được nhận diện trong %s. Các field hợp lệ: [%s]",
                    fieldName, className, knownFields);
            errors = List.of(errorMessage);

        } else if (cause instanceof InvalidFormatException) {
            // Lỗi format dữ liệu (như gửi string cho field number)
            InvalidFormatException formatEx = (InvalidFormatException) cause;
            String fieldName = formatEx.getPath().isEmpty() ? "unknown" :
                    formatEx.getPath().get(formatEx.getPath().size() - 1).getFieldName();
            String expectedType = formatEx.getTargetType().getSimpleName();
            String actualValue = formatEx.getValue().toString();

            errorMessage = String.format("Giá trị '%s' không hợp lệ cho field '%s'. Mong đợi kiểu dữ liệu: %s",
                    actualValue, fieldName, expectedType);
            errors = List.of(errorMessage);

        } else if (cause instanceof MismatchedInputException) {
            // Lỗi mismatch input (như thiếu field bắt buộc)
            MismatchedInputException mismatchEx = (MismatchedInputException) cause;
            String fieldPath = mismatchEx.getPath().stream()
                    .map(ref -> ref.getFieldName())
                    .filter(name -> name != null)
                    .collect(Collectors.joining("."));

            errorMessage = String.format("Dữ liệu đầu vào không khớp cho field: %s",
                    fieldPath.isEmpty() ? "root" : fieldPath);
            errors = List.of(errorMessage);

        } else if (ex.getMessage().contains("JSON parse error")) {
            // Lỗi cú pháp JSON
            errorMessage = "Cú pháp JSON không hợp lệ. Vui lòng kiểm tra format JSON của bạn";
            errors = List.of(errorMessage);
        }

        ApiResponse<Void> response = ApiResponse.error(
                MessageCode.VALIDATION_ERROR,
                errors,
                request.getDescription(false)
        );

        log.warn("JSON parsing error: {}", errorMessage);
        return ResponseEntity.status(MessageCode.VALIDATION_ERROR.getStatusCode()).body(response);
    }

    @ExceptionHandler(Exception.class) // Bắt tất cả exception chưa được xử lý
    public ResponseEntity<ApiResponse<Void>> handleGenericException(
            Exception ex, WebRequest request) {

        ApiResponse<Void> response = ApiResponse.error(
                MessageCode.INTERNAL_SERVER_ERROR,
                request.getDescription(false)
        );

        log.error("Unexpected error: ", ex);
        return ResponseEntity.status(MessageCode.INTERNAL_SERVER_ERROR.getStatusCode()).body(response);
    }

}