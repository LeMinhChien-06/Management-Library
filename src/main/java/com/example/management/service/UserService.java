package com.example.management.service;

import com.example.management.dto.request.UserCreatRequest;
import com.example.management.dto.request.UserUpdateRequest;
import com.example.management.dto.response.UserListResponse;
import com.example.management.dto.response.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse createUser(UserCreatRequest userCreatRequest);

    UserResponse updateUser(Long id, UserUpdateRequest userUpdateRequest);

    List<UserListResponse> getAllUsers();

    UserResponse getUserById(Long id);

    void deleteUserById(Long id);
}
