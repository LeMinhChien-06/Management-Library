package com.example.management.service.impl;

import com.example.management.dto.request.LoginRequest;
import com.example.management.dto.request.LogoutRequest;
import com.example.management.dto.response.LoginResponse;
import com.example.management.entity.InvalidatedToken;
import com.example.management.entity.User;
import com.example.management.exception.auth.AuthenticationExceptions;
import com.example.management.exception.user.UserExceptions;
import com.example.management.repository.InvalidatedTokenRepository;
import com.example.management.repository.UserRepository;
import com.example.management.service.AuthenticationService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final InvalidatedTokenRepository invalidatedTokenRepository;

    @NonFinal
    @Value("${jwt.valid-duration}")
    private int VALID_DURATION;

    @NonFinal //ngăn Lombok đánh dấu một field là final
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @Override
    public LoginResponse login(LoginRequest request) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(UserExceptions::userNotFound);

        log.info("User {} logged in", user.getUsername());

        if (!user.getActive())
            throw UserExceptions.accountLocked();

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!authenticated)
            throw UserExceptions.passwordInvalid();

        return LoginResponse.builder()
                .token(generateToken(user))
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
    }

    @Override
    public SignedJWT verifyToken(String token) throws JOSEException, ParseException {
        JWSVerifier jwsVerifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verify = signedJWT.verify(jwsVerifier);

        if (!(verify && expirationTime.after(new Date())))
            throw AuthenticationExceptions.tokenExpired();

        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw AuthenticationExceptions.tokenInvalid();

        return signedJWT;
    }
}
