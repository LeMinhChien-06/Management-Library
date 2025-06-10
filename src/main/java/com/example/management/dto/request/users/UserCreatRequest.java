package com.example.management.dto.request.users;

import com.example.management.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreatRequest {

    @NotBlank(message = "ERR_REQUIRED_USERNAME")
    @Size(min = 4, max = 50, message = "ERR_REQUIRED_MIN_USERNAME_MAX")
    String username;

    @NotBlank(message = "ERR_REQUIRED_PASSWORD")
    @Size(min = 8, message = "ERR_PASSWORD_TOO_SHORT")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).*$", message = "ERR_PASSWORD_TOO_WEAK")
    String password;

    @NotBlank(message = "ERR_REQUIRED_EMAIL")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "ERR_INVALID_EMAIL")
    String email;

    @NotBlank(message = "ERR_REQUIRED_FULL_NAME")
    String fullName;

    @NotBlank(message = "ERR_REQUIRED_PHONE")
    @Pattern(regexp = "^(\\+84|0)\\d{9,10}$", message = "ERR_INVALID_PHONE")
    String phone;

    Role roles;
    Boolean active;
}
