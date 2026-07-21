package org.example.backend.service;

import org.example.backend.dto.request.CreateRatingRequest;
import org.example.backend.dto.response.SellerRatingResponse;
import org.example.backend.dto.response.SellerRatingSummaryResponse;
import org.example.backend.entity.UserEntity;

import java.util.List;

public interface SellerRatingService {

    // Rate a seller for an ad (prevents duplicate)
    SellerRatingResponse createRating(UserEntity buyer, CreateRatingRequest request);

    // Get all ratings received by a seller
    List<SellerRatingResponse> getSellerRatings(UserEntity seller);

    // Get ratings for a specific ad
    List<SellerRatingResponse> getRatingsByAdvertisement(Long advertisementId);

    // Get average score and count for a seller
    SellerRatingSummaryResponse getSellerRatingSummary(UserEntity seller);
}