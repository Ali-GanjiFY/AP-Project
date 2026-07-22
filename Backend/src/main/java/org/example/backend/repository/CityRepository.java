package org.example.backend.repository;

import org.example.backend.entity.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Contract for city repository.
 */
@Repository
public interface CityRepository extends JpaRepository<CityEntity, Long> {
    /**
     * Finds by name ignore case.
     * @param name the name
     * @return the result
     */
    Optional<CityEntity> findByNameIgnoreCase(String name);
    /**
     * Checks whether by name.
     * @param name the name
     * @return the result
     */
    boolean existsByName(String name);
}

