package org.example.backend.service;

import org.example.backend.dto.request.CreateRatingRequest;
import org.example.backend.dto.response.SellerRatingResponse;
import org.example.backend.dto.response.SellerRatingSummaryResponse;
import org.example.backend.entity.UserEntity;

import java.util.List;

/**
 * Contract for seller rating service.
 */
public interface SellerRatingService {

    /**
     * Rate a seller for an ad (prevents duplicate).
     * @param buyer the buyer
     * @param request the request
     * @return the result
     */
    SellerRatingResponse createRating(UserEntity buyer, CreateRatingRequest request);

    /**
     * Get all ratings received by a seller.
     * @param seller the seller
     * @return the result
     */
    List<SellerRatingResponse> getSellerRatings(UserEntity seller);

    /**
     * Get ratings for a specific ad.
     * @param advertisementId the advertisement id
     * @return the result
     */
    List<SellerRatingResponse> getRatingsByAdvertisement(Long advertisementId);

    /**
     * Get average score and count for a seller.
     * @param seller the seller
     * @return the result
     */
    SellerRatingSummaryResponse getSellerRatingSummary(UserEntity seller);
}