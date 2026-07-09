package org.example.frontend.advertisement;

public class CategoryOption {
    private final Long id;
    private final String name;
    private final String description;
    private final Long parentCategoryId;
    private final String parentCategoryName;
    private final boolean active;

    // for swagger
    public CategoryOption(Long id, String name) {
        this(id, name, null, null, null, true);
    }

    public CategoryOption(Long id, String name, String description,
                          Long parentCategoryId, String parentCategoryName, boolean active) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.parentCategoryId = parentCategoryId;
        this.parentCategoryName = parentCategoryName;
        this.active = active;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Long getParentCategoryId() { return parentCategoryId; }
    public String getParentCategoryName() { return parentCategoryName; }
    public boolean isActive() { return active; }

    @Override
    public String toString() {
        return name;
    }
}