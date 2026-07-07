package org.example.backend.repository;

import org.example.backend.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    Optional<CategoryEntity> findByNameIgnoreCase(String name);

    boolean existsByName(String name);

    long countByParentCategory(CategoryEntity parentCategory);
}
