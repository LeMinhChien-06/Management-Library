package com.example.management.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserListResponse {
    String username;
    String email;
    String fullName;
    String phone;
}
