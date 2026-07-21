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

/**
 * Contract for advertisement service.
 */
public interface AdvertisementService {

    /**
     * Creates ad with PENDING status.
     * @param owner the owner
     * @param request the request
     * @return the result
     */
    AdvertisementDetailResponse createAdvertisement(UserEntity owner, CreateAdvertisementRequest request);

    /**
     * Edits ad, reverts status to PENDING for re-review.
     * @param adId the ad id
     * @param currentUser the current user
     * @param request the request
     * @return the result
     */
    AdvertisementDetailResponse updateAdvertisement(Long adId, UserEntity currentUser, UpdateAdvertisementRequest request);

    /**
     * Soft delete (owner or admin).
     * @param adId the ad id
     * @param currentUser the current user
     */
    void deleteAdvertisement(Long adId, UserEntity currentUser);

    /**
     * Marks as SOLD (owner only).
     * @param adId the ad id
     * @param currentUser the current user
     * @return the result
     */
    AdvertisementDetailResponse markAsSold(Long adId, UserEntity currentUser);

    /**
     * Used by AdminReviewService.
     * @param ad the ad
     * @param newStatus the new status
     */
    void changeStatus(AdvertisementEntity ad, AdvertisementStatusEnum newStatus);

    /**
     * Full details with seller ratings.
     * @param adId the ad id
     * @param currentUser the current user
     * @return the result
     */
    AdvertisementDetailResponse getAdvertisementDetail(Long adId, UserEntity currentUser);

    /**
     * Internal use only.
     * @param adId the ad id
     * @return the result
     */
    AdvertisementEntity getAdvertisementEntityById(Long adId);

    /**
     * All active ads for public view.
     * @return the result
     */
    List<AdvertisementSummaryResponse> getAllActiveAds();

    /**
     * Ads owned by specific user.
     * @param owner the owner
     * @return the result
     */
    List<AdvertisementSummaryResponse> getMyAdvertisements(UserEntity owner);

    /**
     * Pending ads for admin (oldest first).
     * @return the result
     */
    List<AdvertisementSummaryResponse> getPendingAdvertisements();

    /**
     * Search: keyword, category, city, price, sorting.
     * @param request the request
     * @return the result
     */
    List<AdvertisementSummaryResponse> searchAdvertisements(AdvertisementSearchRequest request);
}