package com.example.management.controller;

import com.example.management.constants.MessageCode;
import com.example.management.dto.request.auth.LoginRequest;
import com.example.management.dto.request.auth.LogoutRequest;
import com.example.management.dto.response.ApiResponse;
import com.example.management.dto.response.auth.LoginResponse;
import com.example.management.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Authentication Controller", description = "API xác thực người dùng")
public class AuthenticationController {

    AuthenticationService authenticationService;

    @PostMapping("/login")
    @Operation(summary = "Đăng nhập người dùng", description = "API để đăng nhập vào hệ thống với username và password")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        return ApiResponse.success(MessageCode.LOGIN_SUCCESS, authenticationService.login(request, httpRequest));
    }

    @PostMapping("/logout")
    @Operation(summary = "Đăng xuất người dùng", description = "API để đăng xuất người dùng")
    public ApiResponse<Void> logout( @RequestBody LogoutRequest request) throws ParseException, JOSEException {
        authenticationService.logout(request);
        return ApiResponse.success(MessageCode.LOGOUT_SUCCESS);
    }
}
