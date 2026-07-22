package org.example.backend.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * Represents category request.
 */
public class    CategoryRequest {

    @NotBlank(message = "Category name is required")
    private String name;

    private String description;

    private Long parentCategoryID;

    private Boolean active;

    /**
     * Constructs a new CategoryRequest.
     */
    public CategoryRequest() {
    }

    /**
     * Constructs a new CategoryRequest.
     * @param name the name
     * @param description the description
     * @param parentCategoryID the parent category i d
     * @param active the active
     */
    public CategoryRequest(String name, String description, Long parentCategoryID, Boolean active) {
        this.name = name;
        this.description = description;
        this.parentCategoryID = parentCategoryID;
        this.active = active;
    }

    /**
     * Gets name.
     * @return the result
     */
    public String getName() {
        return name;
    }
    /**
     * Sets name.
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets description.
     * @return the result
     */
    public String getDescription() {
        return description;
    }
    /**
     * Sets description.
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets parent category i d.
     * @return the result
     */
    public Long getParentCategoryID() {
        return parentCategoryID;
    }
    /**
     * Sets parent category i d.
     * @param parentCategoryID the parent category i d
     */
    public void setParentCategoryID(Long parentCategoryID) {
        this.parentCategoryID = parentCategoryID;
    }

    /**
     * Gets active.
     * @return the result
     */
    public Boolean getActive() {
        return active;
    }
    /**
     * Sets active.
     * @param active the active
     */
    public void setActive(Boolean active) {
        this.active = active;
    }
}