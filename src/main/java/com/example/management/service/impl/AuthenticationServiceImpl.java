package com.example.management.service.impl;

import com.example.management.dto.request.auth.LoginRequest;
import com.example.management.dto.request.auth.LogoutRequest;
import com.example.management.dto.response.auth.LoginResponse;
import com.example.management.entity.InvalidatedToken;
import com.example.management.entity.User;
import com.example.management.entity.UserSession;
import com.example.management.exception.auth.AuthenticationExceptions;
import com.example.management.exception.user.UserExceptions;
import com.example.management.repository.InvalidatedTokenRepository;
import com.example.management.repository.UserRepository;
import com.example.management.repository.UserSessionRepository;
import com.example.management.service.AuthenticationService;
import com.example.management.service.DeviceDetectionService;
import com.example.management.service.UserLogService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final InvalidatedTokenRepository invalidatedTokenRepository;
    private final UserSessionRepository userSessionRepository;
    private final UserLogService userLogService;
    private final DeviceDetectionService deviceDetectionService;

    @NonFinal
    @Value("${jwt.valid-duration}")
    private int VALID_DURATION;

    @NonFinal //ngăn Lombok đánh dấu một field là final
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @Override
    public LoginResponse login(LoginRequest request, HttpServletRequest httpRequest) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        var user = userRepository.findByUsername(request.getUsername().trim())
                .orElseThrow(UserExceptions::usernameNotExists);

        log.info("User {} logged in", user.getUsername());

        if (!user.getActive()) // =true mới chạy vào throw
            throw UserExceptions.accountLocked();

        boolean authenticated = passwordEncoder.matches(request.getPassword().trim(), user.getPassword().trim());

        if (!authenticated) {
            throw UserExceptions.passwordInvalid();
        }

        userSessionRepository.deactivateAllSessionsForUser(user.getUsername().trim());
        log.info("Deactivated all previous sessions for user: {}", user.getUsername());

        String newToken = generateToken(user);
        saveUserSession(user.getUsername().trim(), newToken, httpRequest);

        userLogService.logSuccessfulLogin(user, httpRequest);

        log.info("User {} logged in successfully with new session", user.getUsername());

        return LoginResponse.builder()
                .token(newToken)
                .build();
    }

    @Override
    public String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("management-library")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.HOURS).toEpochMilli()
                ))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", user.getRole().name())
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes())); // Ký token với khóa bí mật
            return jwsObject.serialize();  // Trả về token đã được ký và mã hóa thành chuỗi
        } catch (JOSEException e) {
            throw AuthenticationExceptions.tokenNotCreated();
        }
    }

    @Override
    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        var signToken = verifyToken(request.getToken());

        String jwt = signToken.getJWTClaimsSet().getJWTID();
        Date expirationTime = signToken.getJWTClaimsSet().getExpirationTime();

        // backlist
        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(jwt)
                .expiryTime(expirationTime)
                .build();

        invalidatedTokenRepository.save(invalidatedToken);

        userSessionRepository.findById(jwt).ifPresent(session -> {
            session.setActive(false);
            userSessionRepository.save(session);

            userRepository.findByUsername(session.getUsername())
                    .ifPresent(user -> userLogService.logLogout(user, session.getIpAddress(), session.getUserAgent()));

        });

        log.info(" User logged out, session deactivated: {}", jwt);

    }

    @Override
    public SignedJWT verifyToken(String token) throws JOSEException, ParseException {
        JWSVerifier jwsVerifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verify = signedJWT.verify(jwsVerifier);

        if (!(verify && expirationTime.after(new Date())))
            throw AuthenticationExceptions.tokenExpired();

        String jwtId = signedJWT.getJWTClaimsSet().getJWTID();

        // check backlist
        if (invalidatedTokenRepository.existsById(jwtId))
            throw AuthenticationExceptions.tokenInvalid();

        if (!userSessionRepository.existsBySessionIdAndActiveTrue(jwtId)) {
            throw AuthenticationExceptions.tokenInvalid();
        }

        return signedJWT;
    }

    /**
     * Lưu session mới vào database
     */
    private void saveUserSession(String username, String token, HttpServletRequest request) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            String jwtId = signedJWT.getJWTClaimsSet().getJWTID();
            Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();

            String ipAddress = deviceDetectionService.extractClientIpAddress(request);
            String userAgent = request.getHeader("User-Agent");
            String deviceInfo = deviceDetectionService.extractDeviceInfo(userAgent);

            UserSession session = UserSession.builder()
                    .sessionId(jwtId)
                    .username(username)
                    .createdAt(LocalDateTime.now())
                    .expiresAt(expirationTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                    .ipAddress(ipAddress)
                    .userAgent(userAgent)
                    .deviceInfo(deviceInfo)
                    .lastAccessedAt(LocalDateTime.now())
                    .active(true)
                    .build();

            userSessionRepository.save(session);
            log.info(" Saved new session for user: {}", username);

        } catch (ParseException e) {
            log.error("Error parsing token for session storage", e);
            throw new RuntimeException("Failed to save user session", e);
        }
    }

}
