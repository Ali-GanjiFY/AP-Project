package org.example.backend.service;

import org.example.backend.dto.request.AdvertisementSearchRequest;
import org.example.backend.dto.request.CreateAdvertisementRequest;
import org.example.backend.dto.request.UpdateAdvertisementRequest;
import org.example.backend.dto.response.AdvertisementDetailResponse;
import org.example.backend.dto.response.AdvertisementSummaryResponse;
import org.example.backend.entity.Advertisement;
import org.example.backend.entity.User;
import org.example.backend.enums.AdvertisementStatus;

import java.util.List;

public interface AdvertisementService {

    // Create a new advertisement with PENDING status and at least one image
    AdvertisementDetailResponse createAdvertisement(User owner, CreateAdvertisementRequest request);

    // Update existing advertisement, returns to PENDING status for admin review
    AdvertisementDetailResponse updateAdvertisement(Long adId, User currentUser, UpdateAdvertisementRequest request);

    // Soft delete advertisement (owner or admin only)
    void deleteAdvertisement(Long adId, User currentUser);

    // Mark active advertisement as SOLD (owner only)
    AdvertisementDetailResponse markAsSold(Long adId, User currentUser);

    // Internal method: change advertisement status (used by AdminReviewService)
    void changeStatus(Advertisement ad, AdvertisementStatus newStatus);

    // Get full advertisement details with seller ratings
    AdvertisementDetailResponse getAdvertisementDetail(Long adId, User currentUser);

    // Get advertisement entity by ID (internal use by other services)
    Advertisement getAdvertisementEntityById(Long adId);

    // Get all active advertisements for public browsing
    List<AdvertisementSummaryResponse> getAllActiveAds();

    // Get all advertisements owned by a specific user
    List<AdvertisementSummaryResponse> getMyAdvertisements(User owner);

    // Get pending advertisements for admin review (oldest first)
    List<AdvertisementSummaryResponse> getPendingAdvertisements();

    // Search and filter active advertisements by keyword, category, city, price range and sorting
    List<AdvertisementSummaryResponse> searchAdvertisements(AdvertisementSearchRequest request);
}