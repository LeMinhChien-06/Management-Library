package com.example.management.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginRequest {

    @NotBlank(message = "Tên đăng nhập không được để trống")
    String username;

    @NotBlank(message = "Mật khẩu không được để trống")
    String password;

    String ipAddress;
    String userAgent;
    String deviceInfo;
}
