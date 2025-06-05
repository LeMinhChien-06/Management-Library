package com.example.management.service;

import com.example.management.dto.request.LoginRequest;
import com.example.management.dto.request.LogoutRequest;
import com.example.management.dto.response.LoginResponse;
import com.example.management.entity.User;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpRequest;

import java.text.ParseException;

public interface AuthenticationService {

    LoginResponse login(LoginRequest request, HttpServletRequest httpRequest);

    String generateToken(User user);

    void logout(LogoutRequest request) throws ParseException, JOSEException;

    SignedJWT verifyToken(String token) throws JOSEException, ParseException;
}
