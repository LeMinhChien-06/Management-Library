package com.example.management.service;

import com.example.management.dto.request.UserCreatRequest;
import com.example.management.dto.request.UserUpdateRequest;
import com.example.management.dto.response.PageDTO;
import com.example.management.dto.response.UserListResponse;
import com.example.management.dto.response.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse createUser(UserCreatRequest userCreatRequest);

    UserResponse updateUser(Long id, UserUpdateRequest userUpdateRequest);

    PageDTO<UserListResponse> getAllUsers(int page, int size, String sortBy, String sortDirection,
                                          String email, String phone, String fullName, Boolean isActive);

    UserResponse getUserById(Long id);

    void deleteUserById(Long id);
}
