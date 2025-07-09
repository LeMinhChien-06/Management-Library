package com.example.management.mapper;

import com.example.management.dto.request.categories.CategoriesRequest;
import com.example.management.dto.response.categories.CategoriesResponse;
import com.example.management.dto.response.categories.CategoryListResponse;
import com.example.management.entity.Categories;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryMapper {

    private final BookMapper bookMapper;

    public Categories toCategory(CategoriesRequest categoriesRequest) {
        return Categories.builder()
                .name(categoriesRequest.getName())
                .description(categoriesRequest.getDescription())
                .build();

    }

    public CategoriesResponse toCategoryResponse(Categories categories) {
        return CategoriesResponse.builder()
                .id(categories.getId())
                .name(categories.getName())
                .description(categories.getDescription())
                .createdAt(categories.getCreatedAt())
                .build();
    }

    public CategoryListResponse toCategoryListResponse(Categories categories) {
        return CategoryListResponse.builder()
                .name(categories.getName())
                .description(categories.getDescription())
                .createdAt(categories.getCreatedAt())
                .books(bookMapper.toBookResponseDto(categories.getBooks()))
                .build();
    }

}
