package com.example.management.controller;

import com.example.management.constants.MessageCode;
import com.example.management.dto.request.UserCreatRequest;
import com.example.management.dto.request.UserUpdateRequest;
import com.example.management.dto.response.ApiResponse;
import com.example.management.dto.response.UserListResponse;
import com.example.management.dto.response.UserResponse;
import com.example.management.service.UserService;
import com.example.management.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "API xử lý người dùng")
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    @Operation(summary = "Tạo người dùng", description = "API để tạo một người dùng mới")
    public ApiResponse<UserResponse> createUser(@Valid @RequestBody UserCreatRequest request) {
        return ApiResponse.success(MessageCode.USER_CREATED_SUCCESS, userService.createUser(request));
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Cập nhật thông tin người dùng", description = "API để cập nhật thông tin người dùng")
    public ApiResponse<UserResponse> updateUser(@Valid @PathVariable @Min(1) Long id, @RequestBody UserUpdateRequest request) {
        return ApiResponse.success(MessageCode.USER_UPDATED_SUCCESS, userService.updateUser(id, request));
    }

    @GetMapping("/all")
    @Operation(summary = "Lấy danh sách tất cả người dùng", description = "API để lấy danh sách tất cả người dùng")
    public ApiResponse<List<UserListResponse>> getAllUsers() {
        return ApiResponse.success(MessageCode.USER_LIST_SUCCESS, userService.getAllUsers());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy thông tin chi tiết người dùng", description = "API để lấy thông tin chi tiết người dùng")
    public ApiResponse<UserResponse> getUserById(@PathVariable @Min(1) Long id) {
        return ApiResponse.success(MessageCode.USER_DETAIL_SUCCESS, userService.getUserById(id));
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Xóa người dùng", description = "API để xóa một người dùng")
    public ApiResponse<Void> deleteUserById(@PathVariable @Min(1) Long id) {
        userService.deleteUserById(id);
        return ApiResponse.success(MessageCode.USER_DELETED_SUCCESS);
    }
}
