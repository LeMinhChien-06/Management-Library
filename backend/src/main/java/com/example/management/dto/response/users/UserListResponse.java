package com.example.management.dto.response.users;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
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
