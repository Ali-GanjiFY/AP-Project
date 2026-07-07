package org.example.backend.repository;

import org.example.backend.entity.AdvertisementEntity;
import org.example.backend.entity.CategoryEntity;
import org.example.backend.entity.CityEntity;
import org.example.backend.entity.UserEntity;
import org.example.backend.enums.AdvertisementStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdvertisementRepository extends JpaRepository<AdvertisementEntity, Long> {
    List<AdvertisementEntity> findByStatus(AdvertisementStatus status);
    List<AdvertisementEntity> findByOwner(UserEntity owner);
    List<AdvertisementEntity> findByStatusOrderByCreatedAtAsc(AdvertisementStatus status);

    long countByCategory(CategoryEntity category);

    long countByCity(CityEntity city);
}
