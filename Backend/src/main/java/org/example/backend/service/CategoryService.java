package org.example.backend.service;

import org.example.backend.dto.request.CategoryRequest;
import org.example.backend.dto.response.CategoryResponse;
import org.example.backend.entity.CategoryEntity;

import java.util.List;

public interface CategoryService {

    // Create new category
    CategoryResponse createCategory(CategoryRequest request);

    // Update category details
    CategoryResponse updateCategory(Long id, CategoryRequest request);

    // Delete category
    void deleteCategory(Long id);

    // Get category by ID
    CategoryResponse getCategoryById(Long id);

    // Get category by name (case-insensitive)
    CategoryResponse getCategoryByName(String name);

    // Get category entity by ID (internal)
    CategoryEntity getCategoryEntityById(Long id);

    // Get all categories
    List<CategoryResponse> getAllCategories();
}