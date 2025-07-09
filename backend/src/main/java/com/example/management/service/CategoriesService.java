package com.example.management.service;

import com.example.management.dto.request.categories.CategoriesRequest;
import com.example.management.dto.response.categories.CategoriesResponse;
import com.example.management.dto.response.categories.CategoryListResponse;

import java.util.List;

public interface CategoriesService {

    CategoriesResponse createCategory(CategoriesRequest categoriesRequest);

    CategoryListResponse getCategoryById(Long id);

    List<CategoryListResponse> getAllCategories();

    void deleteCategoryById(Long id);
}