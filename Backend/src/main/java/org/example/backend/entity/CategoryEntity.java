package org.example.backend.entity;

import jakarta.persistence.*;
import java.util.List;

/**
 * Represents category entity.
 */
@Entity
@Table(name = "categories")
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column
    private String description;

    @Column(nullable = false)
    private boolean active = true;

    @OneToMany(mappedBy = "category")
    private List<AdvertisementEntity> advertisements;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private CategoryEntity parentCategory;

    @OneToMany(mappedBy = "parentCategory")
    private List<CategoryEntity> subCategories;

    /**
     * Constructs a new CategoryEntity.
     */
    public CategoryEntity() {}

    /**
     * Constructs a new CategoryEntity.
     * @param name the name
     * @param description the description
     */
    public CategoryEntity(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Getters & Setters.
     * @return the result
     */
    public Long getId() { return id; }
    /**
     * Sets id.
     * @param id the id
     */
    public void setId(Long id) { this.id = id; }

    /**
     * Gets name.
     * @return the result
     */
    public String getName() { return name; }
    /**
     * Sets name.
     * @param name the name
     */
    public void setName(String name) { this.name = name; }

    /**
     * Gets description.
     * @return the result
     */
    public String getDescription() { return description; }
    /**
     * Sets description.
     * @param description the description
     */
    public void setDescription(String description) { this.description = description; }

    /**
     * Checks whether active.
     * @return the result
     */
    public boolean isActive() {
        return active;
    }
    /**
     * Sets active.
     * @param active the active
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Gets advertisements.
     * @return the result
     */
    public List<AdvertisementEntity> getAdvertisements() { return advertisements; }
    /**
     * Sets advertisements.
     * @param advertisements the advertisements
     */
    public void setAdvertisements(List<AdvertisementEntity> advertisements) { this.advertisements = advertisements; }

    /**
     * Gets parent category.
     * @return the result
     */
    public CategoryEntity getParentCategory() {
        return parentCategory;
    }
    /**
     * Sets parent category.
     * @param parentCategory the parent category
     */
    public void setParentCategory(CategoryEntity parentCategory) {
        this.parentCategory = parentCategory;
    }

    /**
     * Gets sub categories.
     * @return the result
     */
    public List<CategoryEntity> getSubCategories() {
        return subCategories;
    }
    /**
     * Sets sub categories.
     * @param subCategories the sub categories
     */
    public void setSubCategories(List<CategoryEntity> subCategories) {
        this.subCategories = subCategories;
    }
}