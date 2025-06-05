package com.example.management.config.security;

import com.example.management.exception.AppException;
import com.example.management.service.impl.AuthenticationServiceImpl;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CustomJwtDecoder implements JwtDecoder {

    @Value("${jwt.signerKey}")
    private String signerKey;

    private final AuthenticationServiceImpl authenticationService;

    private NimbusJwtDecoder nimbusJwtDecoder = null;

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            authenticationService.verifyToken(token);
        } catch (ParseException | JOSEException | AppException e) {
            throw new JwtException(e.getMessage());
        }

        // khoi tao NimbusJwtDecoder neu chua co
        if (Objects.isNull(nimbusJwtDecoder)) {
            SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
            nimbusJwtDecoder = NimbusJwtDecoder
                    .withSecretKey(secretKeySpec) // Chuyển signerKey thành SecretKeySpec
                    .macAlgorithm(MacAlgorithm.HS512) // Tạo NimbusJwtDecoder với thuật toán HS512 (HMAC-SHA512).
                    .build();
        }

        return nimbusJwtDecoder.decode(token); // Nếu token hợp lệ, trả về đối tượng Jwt
    }
}
