package org.example.backend.service.impl;

import org.example.backend.dto.request.AdminDecisionRequest;
import org.example.backend.dto.response.AdminReviewResponse;
import org.example.backend.dto.response.AdvertisementSummaryResponse;
import org.example.backend.entity.AdminReviewEntity;
import org.example.backend.entity.AdvertisementEntity;
import org.example.backend.entity.UserEntity;
import org.example.backend.enums.AdvertisementStatusEnum;
import org.example.backend.enums.ReviewDecisionEnum;
import org.example.backend.exception.InvalidInputException;
import org.example.backend.exception.ResourceNotFoundException;
import org.example.backend.repository.AdminReviewRepository;
import org.example.backend.service.AdminReviewService;
import org.example.backend.service.AdvertisementService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents admin review service impl.
 */
@Service
public class AdminReviewServiceImpl implements AdminReviewService {

    private final AdminReviewRepository adminReviewRepository;
    private final AdvertisementService advertisementService;

    /**
     * Constructs a new AdminReviewServiceImpl.
     * @param adminReviewRepository the admin review repository
     * @param advertisementService the advertisement service
     */
    public AdminReviewServiceImpl(AdminReviewRepository adminReviewRepository,
                                  AdvertisementService advertisementService) {
        this.adminReviewRepository = adminReviewRepository;
        this.advertisementService = advertisementService;
    }

    /**
     * Review an advertisement: only PENDING ads can be reviewed.
     * @param admin the admin
     * @param advertisementId the advertisement id
     * @param request the request
     * @return the result
     */
    @Override
    @Transactional
    public AdminReviewResponse reviewAdvertisement(UserEntity admin, Long advertisementId, AdminDecisionRequest request) {
        AdvertisementEntity advertisement = advertisementService.getAdvertisementEntityById(advertisementId);

        // Only pending advertisements can be reviewed
        if (advertisement.getStatus() != AdvertisementStatusEnum.PENDING) {
            throw new InvalidInputException("فقط آگهی‌های در انتظار بررسی (PENDING) قابل تایید یا رد هستند");
        }

        // Map admin decision to target advertisement status
        ReviewDecisionEnum decision = request.getDecision();
        AdvertisementStatusEnum targetStatus = switch (decision) {
            case APPROVED -> AdvertisementStatusEnum.ACTIVE;
            case REJECTED -> AdvertisementStatusEnum.REJECTED;
            case REMOVED  -> AdvertisementStatusEnum.DELETED;
        };

        // Find existing review or create new one (allows re-review)
        AdminReviewEntity review = adminReviewRepository.findByAdvertisement(advertisement)
                .orElseGet(() -> new AdminReviewEntity(request.getNote(), admin, advertisement));

        // Update review fields
        review.setNote(request.getNote());
        review.setDecision(decision);
        review.setAdmin(admin);
        review.setReviewedAt(LocalDateTime.now());

        // Save review first, then update advertisement status in same transaction
        AdminReviewEntity saved = adminReviewRepository.save(review);
        advertisementService.changeStatus(advertisement, targetStatus);

        return toResponse(saved);
    }

    /**
     * Get review record for a specific advertisement.
     * @param advertisementId the advertisement id
     * @return the result
     */
    @Override
    public AdminReviewResponse getReviewByAdvertisementId(Long advertisementId) {
        AdvertisementEntity advertisement = advertisementService.getAdvertisementEntityById(advertisementId);
        AdminReviewEntity review = adminReviewRepository.findByAdvertisement(advertisement)
                .orElseThrow(() -> new ResourceNotFoundException("هنوز بازبینی‌ای برای این آگهی ثبت نشده است"));
        return toResponse(review);
    }

    /**
     * Delegate to AdvertisementService for pending ads list.
     * @return the result
     */
    @Override
    public List<AdvertisementSummaryResponse> getPendingAdvertisements() {
        return advertisementService.getPendingAdvertisements();
    }

    /**
     * Convert AdminReview entity to AdminReviewResponse DTO.
     * @param review the review
     * @return the result
     */
    private AdminReviewResponse toResponse(AdminReviewEntity review) {
        return new AdminReviewResponse(
                review.getId(), review.getDecision(), review.getNote(), review.getReviewedAt(),
                review.getAdmin().getUsername(), review.getAdvertisement().getId()
        );
    }
}