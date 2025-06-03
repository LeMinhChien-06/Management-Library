package com.example.management.service;

import com.example.management.dto.request.LoginRequest;
import com.example.management.dto.request.LogoutRequest;
import com.example.management.dto.response.LoginResponse;
import com.example.management.entity.User;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;

import java.text.ParseException;

public interface AuthenticationService {

    LoginResponse login(LoginRequest request);

    String generateToken(User user);

    void logout(LogoutRequest request) throws ParseException, JOSEException;

    SignedJWT verifyToken(String token) throws JOSEException, ParseException;
}
