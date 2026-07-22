package org.example.backend.service.impl;

import org.example.backend.dto.response.AdvertisementSummaryResponse;
import org.example.backend.dto.response.FavoriteResponse;
import org.example.backend.entity.AdvertisementEntity;
import org.example.backend.entity.FavoriteEntity;
import org.example.backend.entity.UserEntity;
import org.example.backend.exception.DuplicateResourceException;
import org.example.backend.exception.ResourceNotFoundException;
import org.example.backend.exception.UnauthorizedException;
import org.example.backend.repository.FavoriteRepository;
import org.example.backend.service.FavoriteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Represents favorite service impl.
 */
@Service
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;

    /**
     * Constructs a new FavoriteServiceImpl.
     * @param favoriteRepository the favorite repository
     */
    public FavoriteServiceImpl(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    /**
     * Add advertisement to user's favorites.
     * @param user the user
     * @param advertisement the advertisement
     * @return the result
     */
    @Override
    @Transactional
    public FavoriteResponse addFavorite(UserEntity user, AdvertisementEntity advertisement) {
        if (user.getStatus() != org.example.backend.enums.UserStatusEnum.ACTIVE) {
            throw new UnauthorizedException("حساب کاربری شما مسدود است");
        }
        if (advertisement.getOwner() != null && advertisement.getOwner().getId().equals(user.getId())) {
            throw new UnauthorizedException("شما نمی‌توانید آگهی خودتان را به علاقه‌مندی‌ها اضافه کنید");
        }
        // Check if already favorite
        if (favoriteRepository.existsByUserAndAdvertisement(user, advertisement)) {
            throw new DuplicateResourceException("این آگهی قبلاً به علاقه‌مندی‌ها اضافه شده است");
        }
        return toResponse(favoriteRepository.save(new FavoriteEntity(user, advertisement)));
    }

    /**
     * Remove advertisement from user's favorites.
     * @param user the user
     * @param advertisement the advertisement
     */
    @Override
    @Transactional
    public void removeFavorite(UserEntity user, AdvertisementEntity advertisement) {
        // Verify that it exists
        if (!favoriteRepository.existsByUserAndAdvertisement(user, advertisement)) {
            throw new ResourceNotFoundException("این آگهی در لیست علاقه‌مندی‌های شما نیست");
        }
        favoriteRepository.deleteByUserAndAdvertisement(user, advertisement);
    }

    /**
     * Check if an advertisement is in user's favorites.
     * @param user the user
     * @param advertisement the advertisement
     * @return the result
     */
    @Override
    public boolean isFavorite(UserEntity user, AdvertisementEntity advertisement) {
        return favoriteRepository.existsByUserAndAdvertisement(user, advertisement);
    }

    /**
     * Get all favorites for a user.
     * @param user the user
     * @return the result
     */
    @Override
    public List<FavoriteResponse> getUserFavorites(UserEntity user) {
        return favoriteRepository.findByUserOrderBySavedAtDesc(user).stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Convert Favorite entity to FavoriteResponse.
     * @param favorite the favorite
     * @return the result
     */
    private FavoriteResponse toResponse(FavoriteEntity favorite) {
        AdvertisementEntity ad = favorite.getAdvertisement();

        // Extract first image as main image
        String mainImage = (ad.getImages() != null && !ad.getImages().isEmpty())
                ? ad.getImages().get(0).getImagePath() : null;

        // Build advertisement summary
        AdvertisementSummaryResponse summary = new AdvertisementSummaryResponse(
                ad.getId(), ad.getTitle(), ad.getPrice(), ad.getCity().getName(),
                ad.getCategory().getName(), ad.getStatus(), ad.getCreatedAt(), mainImage
        );

        return new FavoriteResponse(favorite.getId(), favorite.getSavedAt(), summary);
    }
}