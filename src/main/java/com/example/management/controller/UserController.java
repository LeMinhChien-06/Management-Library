package com.example.management.controller;

import com.example.management.constants.MessageCode;
import com.example.management.dto.request.UserCreatRequest;
import com.example.management.dto.response.ApiResponse;
import com.example.management.dto.response.UserResponse;
import com.example.management.service.UserService;
import com.example.management.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    public ApiResponse<UserResponse> createUser(@Valid @RequestBody UserCreatRequest request) {
        return ApiResponse.success(MessageCode.USER_CREATED_SUCCESS,userService.createUser(request));
    }
}
