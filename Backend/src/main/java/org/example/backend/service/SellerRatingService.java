package org.example.backend.service;

import org.example.backend.dto.request.CreateRatingRequest;
import org.example.backend.dto.response.SellerRatingResponse;
import org.example.backend.dto.response.SellerRatingSummaryResponse;
import org.example.backend.entity.User;

import java.util.List;

public interface SellerRatingService {

    // Create a rating for a seller (buyer rates seller for an advertisement)
    SellerRatingResponse createRating(User buyer, CreateRatingRequest request);

    // Get all ratings received by a seller
    List<SellerRatingResponse> getSellerRatings(User seller);

    // Get all ratings for a specific advertisement
    List<SellerRatingResponse> getRatingsByAdvertisement(Long advertisementId);

    // Get rating summary (average score and total count) for a seller
    SellerRatingSummaryResponse getSellerRatingSummary(User seller);
}