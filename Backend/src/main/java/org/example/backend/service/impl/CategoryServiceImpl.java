package org.example.backend.service.impl;

import org.example.backend.dto.request.CategoryRequest;
import org.example.backend.dto.response.CategoryResponse;
import org.example.backend.entity.CategoryEntity;
import org.example.backend.exception.DuplicateResourceException;
import org.example.backend.exception.InvalidInputException;
import org.example.backend.exception.ResourceNotFoundException;
import org.example.backend.repository.AdvertisementRepository;
import org.example.backend.repository.CategoryRepository;
import org.example.backend.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Represents category service impl.
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final AdvertisementRepository advertisementRepository; // For checking ad dependencies

    /**
     * Constructs a new CategoryServiceImpl.
     * @param categoryRepository the category repository
     * @param advertisementRepository the advertisement repository
     */
    public CategoryServiceImpl(CategoryRepository categoryRepository, AdvertisementRepository advertisementRepository) {
        this.categoryRepository = categoryRepository;
        this.advertisementRepository = advertisementRepository;
    }

    /**
     * Create a new category with unique name validation.
     * @param request the request
     * @return the result
     */
    @Override
    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        // Check if category already exists
        if (categoryRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("این دسته‌بندی قبلاً ثبت شده است");
        }

        CategoryEntity category = new CategoryEntity(request.getName(), request.getDescription());
        // Default to active if not specified
        category.setActive(request.getActive() == null || request.getActive());

        // Set parent category if provided
        if (request.getParentCategoryID() != null) {
            category.setParentCategory(getCategoryEntityById(request.getParentCategoryID()));
        }

        return toResponse(categoryRepository.save(category));
    }

    /**
     * Update category: name, description, parent, active status.
     * @param id the id
     * @param request the request
     * @return the result
     */
    @Override
    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        CategoryEntity category = getCategoryEntityById(id);

        // Check name uniqueness if changed
        if (!category.getName().equals(request.getName())
                && categoryRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("این دسته‌بندی قبلاً ثبت شده است");
        }

        // Update fields
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        if (request.getActive() != null) {
            category.setActive(request.getActive());
        }

        // Update parent category
        if (request.getParentCategoryID() != null) {
            // Prevent circular reference
            if (request.getParentCategoryID().equals(id)) {
                throw new InvalidInputException("یک دسته‌بندی نمی‌تواند زیرمجموعه‌ی خودش باشد");
            }
            category.setParentCategory(getCategoryEntityById(request.getParentCategoryID()));
        } else {
            category.setParentCategory(null); // Make it a root category
        }

        return toResponse(categoryRepository.save(category));
    }

    /**
     * Delete category with dependency checks.
     * @param id the id
     */
    @Override
    @Transactional
    public void deleteCategory(Long id) {
        CategoryEntity category = getCategoryEntityById(id);

        // Check for subcategories
        long subCategoriesCount = categoryRepository.countByParentCategory(category);
        if (subCategoriesCount > 0) {
            throw new InvalidInputException(
                    "این دسته‌بندی دارای " + subCategoriesCount + " زیرمجموعه است، ابتدا زیرمجموعه‌ها را حذف کنید."
            );
        }

        // Check for advertisements using this category
        long advertisementsCount = advertisementRepository.countByCategory(category);
        if (advertisementsCount > 0) {
            throw new InvalidInputException(
                    "این دسته‌بندی دارای " + advertisementsCount + " آگهی است. ابتدا آگهی‌ها را حذف یا به دسته‌بندی دیگری منتقل کنید."
            );
        }

        categoryRepository.delete(category);
    }

    /**
     * Get category by ID.
     * @param id the id
     * @return the result
     */
    @Override
    public CategoryResponse getCategoryById(Long id) {
        return toResponse(getCategoryEntityById(id));
    }

    /**
     * Get category by name.
     * @param name the name
     * @return the result
     */
    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryByName(String name) {
        CategoryEntity category = categoryRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new ResourceNotFoundException("دسته‌بندی با نام '" + name + "' یافت نشد"));
        return toResponse(category);
    }

    /**
     * Get category entity by ID.
     * @param id the id
     * @return the result
     */
    @Override
    public CategoryEntity getCategoryEntityById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("دسته‌بندی یافت نشد"));
    }

    /**
     * Get all categories.
     * @return the result
     */
    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream().map(this::toResponse).toList();
    }

    /**
     * Convert Category entity to CategoryResponse DTO.
     * @param category the category
     * @return the result
     */
    private CategoryResponse toResponse(CategoryEntity category) {
        Long parentId = category.getParentCategory() != null ? category.getParentCategory().getId() : null;
        String parentName = category.getParentCategory() != null ? category.getParentCategory().getName() : null;

        return new CategoryResponse(
                category.getId(), category.getName(), category.getDescription(), parentId, parentName,
                category.isActive()
        );
    }
}