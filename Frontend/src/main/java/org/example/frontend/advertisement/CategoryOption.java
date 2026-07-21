package org.example.frontend.advertisement;

/**
 * Represents category option.
 */
public class CategoryOption {
    private final Long id;
    private final String name;
    private final String description;
    private final Long parentCategoryId;
    private final String parentCategoryName;
    private final boolean active;

    /**
     * For swagger.
     * @param id the id
     * @param name the name
     */
    public CategoryOption(Long id, String name) {
        this(id, name, null, null, null, true);
    }

    /**
     * Constructs a new CategoryOption.
     * @param id the id
     * @param name the name
     * @param description the description
     * @param parentCategoryId the parent category id
     * @param parentCategoryName the parent category name
     * @param active the active
     */
    public CategoryOption(Long id, String name, String description,
                          Long parentCategoryId, String parentCategoryName, boolean active) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.parentCategoryId = parentCategoryId;
        this.parentCategoryName = parentCategoryName;
        this.active = active;
    }

    /**
     * Gets id.
     * @return the result
     */
    public Long getId() { return id; }
    /**
     * Gets name.
     * @return the result
     */
    public String getName() { return name; }
    /**
     * Gets description.
     * @return the result
     */
    public String getDescription() { return description; }
    /**
     * Gets parent category id.
     * @return the result
     */
    public Long getParentCategoryId() { return parentCategoryId; }
    /**
     * Gets parent category name.
     * @return the result
     */
    public String getParentCategoryName() { return parentCategoryName; }
    /**
     * Checks whether active.
     * @return the result
     */
    public boolean isActive() { return active; }

    /**
     * Converts to string.
     * @return the result
     */
    @Override
    public String toString() {
        return name;
    }
}