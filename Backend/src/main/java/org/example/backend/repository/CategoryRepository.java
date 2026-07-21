package org.example.backend.repository;

import org.example.backend.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Contract for category repository.
 */
@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    /**
     * Finds by name ignore case.
     * @param name the name
     * @return the result
     */
    Optional<CategoryEntity> findByNameIgnoreCase(String name);

    /**
     * Checks whether by name.
     * @param name the name
     * @return the result
     */
    boolean existsByName(String name);

    /**
     * Counts by parent category.
     * @param parentCategory the parent category
     * @return the result
     */
    long countByParentCategory(CategoryEntity parentCategory);
}
