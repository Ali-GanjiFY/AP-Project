package org.example.backend.service;

import org.example.backend.dto.request.AdminDecisionRequest;
import org.example.backend.dto.response.AdminReviewResponse;
import org.example.backend.dto.response.AdvertisementSummaryResponse;
import org.example.backend.entity.UserEntity;

import java.util.List;

/**
 * Contract for admin review service.
 */
public interface AdminReviewService {

    /**
     * Review an advertisement.
     * @param admin the admin
     * @param advertisementId the advertisement id
     * @param request the request
     * @return the result
     */
    AdminReviewResponse reviewAdvertisement(UserEntity admin, Long advertisementId, AdminDecisionRequest request);

    /**
     * Get the review record for a specific advertisement.
     * @param advertisementId the advertisement id
     * @return the result
     */
    AdminReviewResponse getReviewByAdvertisementId(Long advertisementId);

    /**
     * Get all pending advertisements for admin review.
     * @return the result
     */
    List<AdvertisementSummaryResponse> getPendingAdvertisements();
}