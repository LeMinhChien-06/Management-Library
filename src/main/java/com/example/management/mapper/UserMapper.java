package com.example.management.mapper;

import com.example.management.dto.request.users.UserCreatRequest;
import com.example.management.dto.request.users.UserUpdateRequest;
import com.example.management.dto.response.users.UserListResponse;
import com.example.management.dto.response.users.UserResponse;
import com.example.management.entity.User;
import com.example.management.enums.Role;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toUser(UserCreatRequest userCreatRequest) {
        return User.builder()
                .username(userCreatRequest.getUsername())
                .password(userCreatRequest.getPassword())
                .email(userCreatRequest.getEmail())
                .fullName(userCreatRequest.getFullName())
                .phone(userCreatRequest.getPhone())
                .role(Role.USER)
                .active(true)
                .build();
    }

    public User toUser(UserUpdateRequest userUpdateRequest, User user) {
        return User.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .email(userUpdateRequest.getEmail())
                .fullName(userUpdateRequest.getFullName())
                .phone(userUpdateRequest.getPhone())
                .role(user.getRole())
                .active(user.getActive())
                .build();
    }

    public UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .phone(user.getPhone())
                .roles(String.valueOf(user.getRole()))
                .active(true)
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    public UserListResponse toUserListResponse(User user) {
        return UserListResponse.builder()
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .phone(user.getPhone())
                .build();
    }
}
