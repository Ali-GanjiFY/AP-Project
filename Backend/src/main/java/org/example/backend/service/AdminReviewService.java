package org.example.backend.service;

import org.example.backend.dto.request.AdminDecisionRequest;
import org.example.backend.dto.response.AdminReviewResponse;
import org.example.backend.dto.response.AdvertisementSummaryResponse;
import org.example.backend.entity.User;

import java.util.List;

public interface AdminReviewService {

    // Review an advertisement: approve, reject, or remove (admin only)
    AdminReviewResponse reviewAdvertisement(User admin, Long advertisementId, AdminDecisionRequest request);

    // Get the review record for a specific advertisement
    AdminReviewResponse getReviewByAdvertisementId(Long advertisementId);

    // Get all pending advertisements for admin review
    List<AdvertisementSummaryResponse> getPendingAdvertisements();
}