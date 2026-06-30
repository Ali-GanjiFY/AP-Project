package org.example.backend.dto.response;

public class CategoryResponse {

    private final Long id;
    private final String name;
    private final String description;
    private final Long parentCategoryId;
    private final String parentCategoryName;

    public CategoryResponse(Long id, String name, String description,
                            Long parentCategoryId, String parentCategoryName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.parentCategoryId = parentCategoryId;
        this.parentCategoryName = parentCategoryName;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Long getParentCategoryId() { return parentCategoryId; }
    public String getParentCategoryName() { return parentCategoryName; }
}

