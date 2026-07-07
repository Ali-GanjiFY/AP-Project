package org.example.backend.repository;

import org.example.backend.entity.AdminReviewEntity;
import org.example.backend.entity.AdvertisementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminReviewRepository extends JpaRepository<AdminReviewEntity, Long> {
    Optional<AdminReviewEntity> findByAdvertisement(AdvertisementEntity advertisement);
}