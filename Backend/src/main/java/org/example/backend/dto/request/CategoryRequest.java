package org.example.backend.dto.request;

import jakarta.validation.constraints.NotBlank;

public class CategoryRequest {

    @NotBlank(message = "Category name is required")
    private String name;

    private String description;

    private Long parentCategoryID;

    // Constructor
    public CategoryRequest() {
    }

    public CategoryRequest(String name, String description, Long parentCategoryID) {
        this.name = name;
        this.description = description;
        this.parentCategoryID = parentCategoryID;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Long getParentCategoryID() {
        return parentCategoryID;
    }
    public void setParentCategoryID(Long parentCategoryID) {
        this.parentCategoryID = parentCategoryID;
    }
}