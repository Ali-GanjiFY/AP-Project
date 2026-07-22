package org.example.backend.service;

import org.example.backend.dto.request.CategoryRequest;
import org.example.backend.dto.response.CategoryResponse;
import org.example.backend.entity.CategoryEntity;

import java.util.List;

/**
 * Contract for category service.
 */
public interface CategoryService {

    /**
     * Create new category.
     * @param request the request
     * @return the result
     */
    CategoryResponse createCategory(CategoryRequest request);

    /**
     * Update category details.
     * @param id the id
     * @param request the request
     * @return the result
     */
    CategoryResponse updateCategory(Long id, CategoryRequest request);

    /**
     * Delete category.
     * @param id the id
     */
    void deleteCategory(Long id);

    /**
     * Get category by ID.
     * @param id the id
     * @return the result
     */
    CategoryResponse getCategoryById(Long id);

    /**
     * Get category by name (case-insensitive).
     * @param name the name
     * @return the result
     */
    CategoryResponse getCategoryByName(String name);

    /**
     * Get category entity by ID (internal).
     * @param id the id
     * @return the result
     */
    CategoryEntity getCategoryEntityById(Long id);

    /**
     * Get all categories.
     * @return the result
     */
    List<CategoryResponse> getAllCategories();
}