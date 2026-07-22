package org.example.backend.service.impl;

import org.example.backend.dto.request.CreateRatingRequest;
import org.example.backend.dto.response.SellerRatingResponse;
import org.example.backend.dto.response.SellerRatingSummaryResponse;
import org.example.backend.entity.AdvertisementEntity;
import org.example.backend.entity.SellerRatingEntity;
import org.example.backend.entity.UserEntity;
import org.example.backend.exception.DuplicateResourceException;
import org.example.backend.exception.InvalidInputException;
import org.example.backend.repository.SellerRatingRepository;
import org.example.backend.service.AdvertisementService;
import org.example.backend.service.SellerRatingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents seller rating service impl.
 */
@Service
public class SellerRatingServiceImpl implements SellerRatingService {

    private final SellerRatingRepository sellerRatingRepository;
    private final AdvertisementService advertisementService;

    /**
     * Constructs a new SellerRatingServiceImpl.
     * @param sellerRatingRepository the seller rating repository
     * @param advertisementService the advertisement service
     */
    public SellerRatingServiceImpl(SellerRatingRepository sellerRatingRepository,
                                   AdvertisementService advertisementService) {
        this.sellerRatingRepository = sellerRatingRepository;
        this.advertisementService = advertisementService;
    }

    /**
     * Create a rating: prevent self-rating and duplicate ratings.
     * @param buyer the buyer
     * @param request the request
     * @return the result
     */
    @Override
    @Transactional
    public SellerRatingResponse createRating(UserEntity buyer, CreateRatingRequest request) {
        AdvertisementEntity advertisement = advertisementService.getAdvertisementEntityById(request.getAdvertisementId());
        UserEntity seller = advertisement.getOwner();

        // Cannot rate your own advertisement
        if (seller.getId().equals(buyer.getId())) {
            throw new InvalidInputException("شما نمی‌توانید به آگهی خودتان امتیاز بدهید");
        }
        // Each buyer can only rate once per advertisement
        if (sellerRatingRepository.existsByBuyerAndAdvertisement(buyer, advertisement)) {
            throw new DuplicateResourceException("شما قبلاً برای این آگهی امتیاز ثبت کرده‌اید");
        }

        SellerRatingEntity rating = new SellerRatingEntity(request.getScore(), request.getComment(), buyer, seller, advertisement);
        return toResponse(sellerRatingRepository.save(rating));
    }

    /**
     * Get all ratings received by a seller.
     * @param seller the seller
     * @return the result
     */
    @Override
    public List<SellerRatingResponse> getSellerRatings(UserEntity seller) {
        return sellerRatingRepository.findBySeller(seller).stream().map(this::toResponse).toList();
    }

    /**
     * Get all ratings for a specific advertisement.
     * @param advertisementId the advertisement id
     * @return the result
     */
    @Override
    @Transactional(readOnly = true)
    public List<SellerRatingResponse> getRatingsByAdvertisement(Long advertisementId) {
        AdvertisementEntity advertisement = advertisementService.getAdvertisementEntityById(advertisementId);
        return sellerRatingRepository.findByAdvertisement(advertisement).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get rating summary: average score and total count for a seller.
     * @param seller the seller
     * @return the result
     */
    @Override
    public SellerRatingSummaryResponse getSellerRatingSummary(UserEntity seller) {
        List<SellerRatingEntity> ratings = sellerRatingRepository.findBySeller(seller);
        // Calculate average, default to 0.0 if no ratings
        double average = ratings.stream().mapToInt(SellerRatingEntity::getScore).average().orElse(0.0);
        long count = sellerRatingRepository.countBySeller(seller);
        return new SellerRatingSummaryResponse(average, count);
    }

    /**
     * Convert SellerRating entity to SellerRatingResponse DTO.
     * @param rating the rating
     * @return the result
     */
    private SellerRatingResponse toResponse(SellerRatingEntity rating) {
        return new SellerRatingResponse(
                rating.getId(), rating.getScore(), rating.getComment(), rating.getRatedAt(),
                rating.getBuyer().getUsername()
        );
    }
}