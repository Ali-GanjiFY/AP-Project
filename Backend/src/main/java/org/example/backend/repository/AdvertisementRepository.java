package org.example.backend.repository;

import org.example.backend.entity.AdvertisementEntity;
import org.example.backend.entity.CategoryEntity;
import org.example.backend.entity.CityEntity;
import org.example.backend.entity.UserEntity;
import org.example.backend.enums.AdvertisementStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Contract for advertisement repository.
 */
@Repository
public interface AdvertisementRepository extends JpaRepository<AdvertisementEntity, Long> {
    /**
     * Finds by status.
     * @param status the status
     * @return the result
     */
    List<AdvertisementEntity> findByStatus(AdvertisementStatusEnum status);
    /**
     * Finds by owner.
     * @param owner the owner
     * @return the result
     */
    List<AdvertisementEntity> findByOwner(UserEntity owner);
    /**
     * Finds by status order by created at asc.
     * @param status the status
     * @return the result
     */
    List<AdvertisementEntity> findByStatusOrderByCreatedAtAsc(AdvertisementStatusEnum status);

    /**
     * Counts by category.
     * @param category the category
     * @return the result
     */
    long countByCategory(CategoryEntity category);

    /**
     * Counts by city.
     * @param city the city
     * @return the result
     */
    long countByCity(CityEntity city);
}
