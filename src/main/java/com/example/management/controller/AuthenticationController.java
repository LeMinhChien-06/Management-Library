package com.example.management.controller;

import com.example.management.constants.MessageCode;
import com.example.management.dto.request.LoginRequest;
import com.example.management.dto.request.LogoutRequest;
import com.example.management.dto.response.ApiResponse;
import com.example.management.dto.response.LoginResponse;
import com.example.management.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    AuthenticationService authenticationService;

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        return ApiResponse.success(MessageCode.LOGIN_SUCCESS, authenticationService.login(request, httpRequest));
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout( @RequestBody LogoutRequest request) throws ParseException, JOSEException {
        authenticationService.logout(request);
        return ApiResponse.success(MessageCode.LOGOUT_SUCCESS);
    }
}
