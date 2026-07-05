package org.example.backend.service.impl;

import org.example.backend.dto.request.CreateRatingRequest;
import org.example.backend.dto.response.SellerRatingResponse;
import org.example.backend.dto.response.SellerRatingSummaryResponse;
import org.example.backend.entity.Advertisement;
import org.example.backend.entity.SellerRating;
import org.example.backend.entity.User;
import org.example.backend.exception.DuplicateResourceException;
import org.example.backend.exception.InvalidInputException;
import org.example.backend.repository.SellerRatingRepository;
import org.example.backend.service.AdvertisementService;
import org.example.backend.service.SellerRatingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SellerRatingServiceImpl implements SellerRatingService {

    private final SellerRatingRepository sellerRatingRepository;
    private final AdvertisementService advertisementService;

    public SellerRatingServiceImpl(SellerRatingRepository sellerRatingRepository,
                                   AdvertisementService advertisementService) {
        this.sellerRatingRepository = sellerRatingRepository;
        this.advertisementService = advertisementService;
    }

    // Create a rating: prevent self-rating and duplicate ratings
    @Override
    @Transactional
    public SellerRatingResponse createRating(User buyer, CreateRatingRequest request) {
        Advertisement advertisement = advertisementService.getAdvertisementEntityById(request.getAdvertisementId());
        User seller = advertisement.getOwner();

        // Cannot rate your own advertisement
        if (seller.getId().equals(buyer.getId())) {
            throw new InvalidInputException("شما نمی‌توانید به آگهی خودتان امتیاز بدهید");
        }
        // Each buyer can only rate once per advertisement
        if (sellerRatingRepository.existsByBuyerAndAdvertisement(buyer, advertisement)) {
            throw new DuplicateResourceException("شما قبلاً برای این آگهی امتیاز ثبت کرده‌اید");
        }

        SellerRating rating = new SellerRating(request.getScore(), request.getComment(), buyer, seller, advertisement);
        return toResponse(sellerRatingRepository.save(rating));
    }

    // Get all ratings received by a seller
    @Override
    public List<SellerRatingResponse> getSellerRatings(User seller) {
        return sellerRatingRepository.findBySeller(seller).stream().map(this::toResponse).toList();
    }

    // Get all ratings for a specific advertisement
    @Override
    @Transactional(readOnly = true)
    public List<SellerRatingResponse> getRatingsByAdvertisement(Long advertisementId) {
        Advertisement advertisement = advertisementService.getAdvertisementEntityById(advertisementId);
        return sellerRatingRepository.findByAdvertisement(advertisement).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // Get rating summary: average score and total count for a seller
    @Override
    public SellerRatingSummaryResponse getSellerRatingSummary(User seller) {
        List<SellerRating> ratings = sellerRatingRepository.findBySeller(seller);
        // Calculate average, default to 0.0 if no ratings
        double average = ratings.stream().mapToInt(SellerRating::getScore).average().orElse(0.0);
        long count = sellerRatingRepository.countBySeller(seller);
        return new SellerRatingSummaryResponse(average, count);
    }

    // Convert SellerRating entity to SellerRatingResponse DTO
    private SellerRatingResponse toResponse(SellerRating rating) {
        return new SellerRatingResponse(
                rating.getId(), rating.getScore(), rating.getComment(), rating.getRatedAt(),
                rating.getBuyer().getUsername()
        );
    }
}