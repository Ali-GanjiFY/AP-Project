package org.example.backend.repository;

import org.example.backend.entity.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<CityEntity, Long> {
    Optional<CityEntity> findByNameIgnoreCase(String name);
    boolean existsByName(String name);
}

