package org.example.backend.service.impl;

import org.example.backend.dto.request.CategoryRequest;
import org.example.backend.dto.response.CategoryResponse;
import org.example.backend.entity.Category;
import org.example.backend.exception.DuplicateResourceException;
import org.example.backend.exception.InvalidInputException;
import org.example.backend.exception.ResourceNotFoundException;
import org.example.backend.repository.AdvertisementRepository;
import org.example.backend.repository.CategoryRepository;
import org.example.backend.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final AdvertisementRepository advertisementRepository; // For checking ad dependencies before deletion

    public CategoryServiceImpl(CategoryRepository categoryRepository, AdvertisementRepository advertisementRepository) {
        this.categoryRepository = categoryRepository;
        this.advertisementRepository = advertisementRepository;
    }

    // Create a new category with unique name validation
    @Override
    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        // Check if category name already exists
        if (categoryRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("این دسته‌بندی قبلاً ثبت شده است");
        }

        Category category = new Category(request.getName(), request.getDescription());
        // Default to active if not specified
        category.setActive(request.getActive() == null || request.getActive());

        // Set parent category if provided
        if (request.getParentCategoryID() != null) {
            category.setParentCategory(getCategoryEntityById(request.getParentCategoryID()));
        }

        return toResponse(categoryRepository.save(category));
    }

    // Update category: name, description, parent, active status
    @Override
    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category category = getCategoryEntityById(id);

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

    // Delete category with dependency checks (subcategories and ads)
    @Override
    @Transactional
    public void deleteCategory(Long id) {
        Category category = getCategoryEntityById(id);

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

    // Get category by ID as DTO
    @Override
    public CategoryResponse getCategoryById(Long id) {
        return toResponse(getCategoryEntityById(id));
    }

    // Get category by name (case-insensitive)
    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryByName(String name) {
        Category category = categoryRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new ResourceNotFoundException("دسته‌بندی با نام '" + name + "' یافت نشد"));
        return toResponse(category);
    }

    // Get category entity by ID (internal use by other services)
    @Override
    public Category getCategoryEntityById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("دسته‌بندی یافت نشد"));
    }

    // Get all categories
    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream().map(this::toResponse).toList();
    }

    // Convert Category entity to CategoryResponse DTO
    private CategoryResponse toResponse(Category category) {
        Long parentId = category.getParentCategory() != null ? category.getParentCategory().getId() : null;
        String parentName = category.getParentCategory() != null ? category.getParentCategory().getName() : null;

        return new CategoryResponse(
                category.getId(), category.getName(), category.getDescription(), parentId, parentName,
                category.isActive()
        );
    }
}