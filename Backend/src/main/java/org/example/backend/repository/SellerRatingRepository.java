package org.example.backend.repository;

import org.example.backend.entity.AdvertisementEntity;
import org.example.backend.entity.SellerRatingEntity;
import org.example.backend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SellerRatingRepository extends JpaRepository<SellerRatingEntity, Long> {
    List<SellerRatingEntity> findBySeller(UserEntity seller);
    boolean existsByBuyerAndAdvertisement(UserEntity buyer, AdvertisementEntity advertisement);
    List<SellerRatingEntity> findByAdvertisement(AdvertisementEntity advertisement);
    long countBySeller(UserEntity seller);
}