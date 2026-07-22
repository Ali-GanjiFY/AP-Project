package org.example.backend.repository;

import org.example.backend.entity.AdminReviewEntity;
import org.example.backend.entity.AdvertisementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Contract for admin review repository.
 */
@Repository
public interface AdminReviewRepository extends JpaRepository<AdminReviewEntity, Long> {
    /**
     * Finds by advertisement.
     * @param advertisement the advertisement
     * @return the result
     */
    Optional<AdminReviewEntity> findByAdvertisement(AdvertisementEntity advertisement);
}