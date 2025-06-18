package com.example.management.service.impl;

import com.example.management.annotation.TrackAction;
import com.example.management.dto.request.categories.CategoriesRequest;
import com.example.management.dto.response.categories.CategoriesResponse;
import com.example.management.dto.response.categories.CategoryListResponse;
import com.example.management.entity.Categories;
import com.example.management.enums.AuditAction;
import com.example.management.enums.EntityType;
import com.example.management.exception.categories.CategoryExceptions;
import com.example.management.mapper.CategoryMapper;
import com.example.management.repository.CategoriesRepository;
import com.example.management.service.CategoriesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoriesServiceImpl implements CategoriesService {

    private final CategoriesRepository categoriesRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    @TrackAction(action = AuditAction.CATEGORY_CREATE, entityType = EntityType.CATEGORY, entityId = "#result.id", entityName = "#result.name")
    public CategoriesResponse createCategory(CategoriesRequest categoriesRequest) {
        Categories categories = categoryMapper.toCategory(categoriesRequest);

        Categories savedCategories = categoriesRepository.save(categories);

        log.info("Saved categories: {}", savedCategories);

        return categoryMapper.toCategoryResponse(savedCategories);

    }

    @Override
    @Transactional
    @TrackAction(action = AuditAction.CATEGORY_GET_BY_ID, entityType = EntityType.CATEGORY, entityId = "#result.id", entityName = "#result.name")
    public CategoryListResponse getCategoryById(Long id) {
        Categories categories = categoriesRepository.findById(id).orElseThrow(CategoryExceptions::categoryNotFound);

        log.info("Retrieved categories: {}", categories);

        return categoryMapper.toCategoryListResponse(categories);
    }

    @Override
    @Transactional
    @TrackAction(action = AuditAction.CATEGORY_VIEW, entityType = EntityType.CATEGORY, entityId = "#arg")
    public List<CategoryListResponse> getAllCategories() {
        List<Categories> categoriesList = categoriesRepository.findAll();
        List<CategoryListResponse> categoryListResponses = categoriesList.stream().map(categoryMapper::toCategoryListResponse).toList();

        log.info("Retrieved {} categories", categoryListResponses.size());

        return categoryListResponses;
    }

    @Override
    @Transactional
    @TrackAction(action = AuditAction.CATEGORY_DELETE, entityType = EntityType.CATEGORY, entityId = "#result.id", entityName = "#result.name")
    public void deleteCategoryById(Long id) {
        categoriesRepository.findById(id).orElseThrow(CategoryExceptions::categoryNotFound);
        categoriesRepository.deleteById(id);
        log.info("Deleted categories with ID: {}", id);
    }
}
