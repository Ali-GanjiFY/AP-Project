package org.example.backend.service;

import org.example.backend.dto.request.CategoryRequest;
import org.example.backend.dto.response.CategoryResponse;
import org.example.backend.entity.Category;

import java.util.List;

public interface CategoryService {

    // Create a new category with unique name validation
    CategoryResponse createCategory(CategoryRequest request);

    // Update category details (name, description, parent, active status)
    CategoryResponse updateCategory(Long id, CategoryRequest request);

    // Delete category by ID (soft delete or hard delete)
    void deleteCategory(Long id);

    // Get category by ID as DTO
    CategoryResponse getCategoryById(Long id);

    // Get category by name (case-insensitive)
    CategoryResponse getCategoryByName(String name);

    // Get category entity by ID (internal use by other services)
    Category getCategoryEntityById(Long id);

    // Get list of all categories
    List<CategoryResponse> getAllCategories();
}