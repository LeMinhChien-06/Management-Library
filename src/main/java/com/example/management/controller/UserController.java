package com.example.management.controller;

import com.example.management.constants.MessageCode;
import com.example.management.dto.request.users.UserCreatRequest;
import com.example.management.dto.request.users.UserUpdateRequest;
import com.example.management.dto.response.ApiResponse;
import com.example.management.dto.response.PageDTO;
import com.example.management.dto.response.users.UserListResponse;
import com.example.management.dto.response.users.UserResponse;
import com.example.management.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    @Operation(
            summary = "Lấy danh sách người dùng với phân trang, tìm kiếm và lọc",
            description = "API để lấy danh sách người dùng với khả năng phân trang, tìm kiếm theo email/sdt/fullName" +
                    " và lọc theo trạng thái active"
    )
    public ApiResponse<PageDTO<UserListResponse>> getAllUsers(
            @RequestParam(required = false, defaultValue = "0") @Min(0) int page,
            @RequestParam(required = false, defaultValue = "10") @Min(1) int size,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String direction,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) Boolean isActive
    ) {
        return ApiResponse.success(MessageCode.USER_LIST_SUCCESS, userService.getAllUsers(page, size, sort, direction,
                email, phone, fullName, isActive));
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
