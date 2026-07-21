package org.example.backend.repository;

import org.example.backend.entity.AdvertisementEntity;
import org.example.backend.entity.SellerRatingEntity;
import org.example.backend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Contract for seller rating repository.
 */
@Repository
public interface SellerRatingRepository extends JpaRepository<SellerRatingEntity, Long> {
    /**
     * Finds by seller.
     * @param seller the seller
     * @return the result
     */
    List<SellerRatingEntity> findBySeller(UserEntity seller);
    /**
     * Checks whether by buyer and advertisement.
     * @param buyer the buyer
     * @param advertisement the advertisement
     * @return the result
     */
    boolean existsByBuyerAndAdvertisement(UserEntity buyer, AdvertisementEntity advertisement);
    /**
     * Finds by advertisement.
     * @param advertisement the advertisement
     * @return the result
     */
    List<SellerRatingEntity> findByAdvertisement(AdvertisementEntity advertisement);
    /**
     * Counts by seller.
     * @param seller the seller
     * @return the result
     */
    long countBySeller(UserEntity seller);
}