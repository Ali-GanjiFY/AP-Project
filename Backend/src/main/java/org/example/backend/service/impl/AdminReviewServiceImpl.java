package org.example.backend.service.impl;

import org.example.backend.dto.request.AdminDecisionRequest;
import org.example.backend.dto.response.AdminReviewResponse;
import org.example.backend.dto.response.AdvertisementSummaryResponse;
import org.example.backend.entity.AdminReview;
import org.example.backend.entity.Advertisement;
import org.example.backend.entity.User;
import org.example.backend.enums.AdvertisementStatus;
import org.example.backend.enums.ReviewDecision;
import org.example.backend.exception.InvalidInputException;
import org.example.backend.exception.ResourceNotFoundException;
import org.example.backend.repository.AdminReviewRepository;
import org.example.backend.service.AdminReviewService;
import org.example.backend.service.AdvertisementService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdminReviewServiceImpl implements AdminReviewService {

    private final AdminReviewRepository adminReviewRepository;
    private final AdvertisementService advertisementService;

    public AdminReviewServiceImpl(AdminReviewRepository adminReviewRepository,
                                  AdvertisementService advertisementService) {
        this.adminReviewRepository = adminReviewRepository;
        this.advertisementService = advertisementService;
    }

    // Review an advertisement: only PENDING ads can be reviewed
    @Override
    @Transactional
    public AdminReviewResponse reviewAdvertisement(User admin, Long advertisementId, AdminDecisionRequest request) {
        Advertisement advertisement = advertisementService.getAdvertisementEntityById(advertisementId);

        // Only pending advertisements can be reviewed
        if (advertisement.getStatus() != AdvertisementStatus.PENDING) {
            throw new InvalidInputException("فقط آگهی‌های در انتظار بررسی (PENDING) قابل تایید یا رد هستند");
        }

        // Map admin decision to target advertisement status
        ReviewDecision decision = request.getDecision();
        AdvertisementStatus targetStatus = switch (decision) {
            case APPROVED -> AdvertisementStatus.ACTIVE;
            case REJECTED -> AdvertisementStatus.REJECTED;
            case REMOVED  -> AdvertisementStatus.DELETED;
        };

        // Find existing review or create new one (allows re-review)
        AdminReview review = adminReviewRepository.findByAdvertisement(advertisement)
                .orElseGet(() -> new AdminReview(request.getNote(), admin, advertisement));

        // Update review fields
        review.setNote(request.getNote());
        review.setDecision(decision);
        review.setAdmin(admin);
        review.setReviewedAt(LocalDateTime.now());

        // Save review first, then update advertisement status in same transaction
        AdminReview saved = adminReviewRepository.save(review);
        advertisementService.changeStatus(advertisement, targetStatus);

        return toResponse(saved);
    }

    // Get review record for a specific advertisement
    @Override
    public AdminReviewResponse getReviewByAdvertisementId(Long advertisementId) {
        Advertisement advertisement = advertisementService.getAdvertisementEntityById(advertisementId);
        AdminReview review = adminReviewRepository.findByAdvertisement(advertisement)
                .orElseThrow(() -> new ResourceNotFoundException("هنوز بازبینی‌ای برای این آگهی ثبت نشده است"));
        return toResponse(review);
    }

    // Delegate to AdvertisementService for pending ads list
    @Override
    public List<AdvertisementSummaryResponse> getPendingAdvertisements() {
        return advertisementService.getPendingAdvertisements();
    }

    // Convert AdminReview entity to AdminReviewResponse DTO
    private AdminReviewResponse toResponse(AdminReview review) {
        return new AdminReviewResponse(
                review.getId(), review.getDecision(), review.getNote(), review.getReviewedAt(),
                review.getAdmin().getUsername(), review.getAdvertisement().getId()
        );
    }
}