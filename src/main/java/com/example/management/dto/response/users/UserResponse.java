package com.example.management.dto.response;

import com.example.management.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class UserResponse {
    Long id;
    String username;
    String email;
    String fullName;
    String phone;
    String roles;
    Boolean active;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
