package com.example.management.controller;

import com.example.management.constants.MessageCode;
import com.example.management.dto.request.categories.CategoriesRequest;
import com.example.management.dto.response.ApiResponse;
import com.example.management.dto.response.categories.CategoriesResponse;
import com.example.management.dto.response.categories.CategoryListResponse;
import com.example.management.service.CategoriesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Tag(name = "Category Controller", description = "API quản lý thể loại sách")
public class CategoryController {

    private final CategoriesService categoriesService;

    @PostMapping("/create")
    @Operation(summary = "Tạo thể loại sách", description = "API để tạo một thể loại sách mới")
    public ApiResponse<CategoriesResponse> createCategory(@Valid @RequestBody CategoriesRequest categoriesRequest) {
        return ApiResponse.success(MessageCode.CATEGORY_CREATED_SUCCESS, categoriesService.createCategory(categoriesRequest));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy thông tin chi tiết thể loại sách", description = "API để lấy thông tin chi tiết thể loại sách")
    public ApiResponse<CategoryListResponse> getCategoryById(@PathVariable @Min(1) Long id) {
        return ApiResponse.success(MessageCode.CATEGORY_DETAIL_SUCCESS, categoriesService.getCategoryById(id));
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Xóa thể loại sách", description = "API để xóa một thể loại sách")
    public ApiResponse<Void> deleteCategoryById(@PathVariable @Min(1) Long id) {
        categoriesService.deleteCategoryById(id);
        return ApiResponse.success(MessageCode.CATEGORY_DELETED_SUCCESS);
    }


    @GetMapping("/all")
    @Operation(summary = "Lấy danh sách các thể loại sách", description = "API để lấy danh sách các thể loại sách")
    public ApiResponse<List<CategoryListResponse>> getAllCategories() {
        return ApiResponse.success(MessageCode.CATEGORY_LIST_SUCCESS, categoriesService.getAllCategories());
    }
}
