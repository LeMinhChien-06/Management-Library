package com.example.management.dto.request.users;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {

    @NotBlank(message = "ERR_REQUIRED_EMAIL")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "ERR_INVALID_EMAIL")
    String email;

    @NotBlank(message = "ERR_REQUIRED_FULL_NAME")
    String fullName;

    @NotBlank(message = "ERR_REQUIRED_PHONE")
    @Pattern(regexp = "^(\\+84|0)\\d{9,10}$", message = "ERR_INVALID_PHONE")
    String phone;
}
