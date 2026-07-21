package org.example.backend.service;

import org.example.backend.dto.request.AdvertisementSearchRequest;
import org.example.backend.dto.request.CreateAdvertisementRequest;
import org.example.backend.dto.request.UpdateAdvertisementRequest;
import org.example.backend.dto.response.AdvertisementDetailResponse;
import org.example.backend.dto.response.AdvertisementSummaryResponse;
import org.example.backend.entity.AdvertisementEntity;
import org.example.backend.entity.UserEntity;
import org.example.backend.enums.AdvertisementStatusEnum;

import java.util.List;

public interface AdvertisementService {

    // Creates ad with PENDING status
    AdvertisementDetailResponse createAdvertisement(UserEntity owner, CreateAdvertisementRequest request);

    // Edits ad, reverts status to PENDING for re-review
    AdvertisementDetailResponse updateAdvertisement(Long adId, UserEntity currentUser, UpdateAdvertisementRequest request);

    // Soft delete (owner or admin)
    void deleteAdvertisement(Long adId, UserEntity currentUser);

    // Marks as SOLD (owner only)
    AdvertisementDetailResponse markAsSold(Long adId, UserEntity currentUser);

    // Used by AdminReviewService
    void changeStatus(AdvertisementEntity ad, AdvertisementStatusEnum newStatus);

    // Full details with seller ratings
    AdvertisementDetailResponse getAdvertisementDetail(Long adId, UserEntity currentUser);

    // Internal use only
    AdvertisementEntity getAdvertisementEntityById(Long adId);

    // All active ads for public view
    List<AdvertisementSummaryResponse> getAllActiveAds();

    // Ads owned by specific user
    List<AdvertisementSummaryResponse> getMyAdvertisements(UserEntity owner);

    // Pending ads for admin (oldest first)
    List<AdvertisementSummaryResponse> getPendingAdvertisements();

    // Search: keyword, category, city, price, sorting
    List<AdvertisementSummaryResponse> searchAdvertisements(AdvertisementSearchRequest request);
}