package org.example.backend.repository;

import org.example.backend.entity.AdminReview;
import org.example.backend.entity.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminReviewRepository extends JpaRepository<AdminReview, Long> {
    Optional<AdminReview> findByAdvertisement(Advertisement advertisement);
}