package org.example.backend.service;

import org.example.backend.dto.response.FavoriteResponse;
import org.example.backend.entity.AdvertisementEntity;
import org.example.backend.entity.UserEntity;

import java.util.List;

/**
 * Contract for favorite service.
 */
public interface FavoriteService {

    /**
     * Add ad to user's favorites (prevents duplicate).
     * @param user the user
     * @param advertisement the advertisement
     * @return the result
     */
    FavoriteResponse addFavorite(UserEntity user, AdvertisementEntity advertisement);

    /**
     * Remove ad from user's favorites.
     * @param user the user
     * @param advertisement the advertisement
     */
    void removeFavorite(UserEntity user, AdvertisementEntity advertisement);

    /**
     * Check if ad is in user's favorites.
     * @param user the user
     * @param advertisement the advertisement
     * @return the result
     */
    boolean isFavorite(UserEntity user, AdvertisementEntity advertisement);

    /**
     * Get all favorites for a user (sorted newest first).
     * @param user the user
     * @return the result
     */
    List<FavoriteResponse> getUserFavorites(UserEntity user);
}