package org.example.backend.repository;

import org.example.backend.entity.Advertisement;
import org.example.backend.entity.SellerRating;
import org.example.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SellerRatingRepository extends JpaRepository<SellerRating, Long> {
    List<SellerRating> findBySeller(User seller);
    boolean existsByBuyerAndAdvertisement(User buyer, Advertisement advertisement);
    List<SellerRating> findByAdvertisement(Advertisement advertisement);
    long countBySeller(User seller);
}