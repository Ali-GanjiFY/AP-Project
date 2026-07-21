package org.example.backend.service;

import org.example.backend.dto.response.FavoriteResponse;
import org.example.backend.entity.AdvertisementEntity;
import org.example.backend.entity.UserEntity;

import java.util.List;

public interface FavoriteService {

    // Add ad to user's favorites (prevents duplicate)
    FavoriteResponse addFavorite(UserEntity user, AdvertisementEntity advertisement);

    // Remove ad from user's favorites
    void removeFavorite(UserEntity user, AdvertisementEntity advertisement);

    // Check if ad is in user's favorites
    boolean isFavorite(UserEntity user, AdvertisementEntity advertisement);

    // Get all favorites for a user (sorted newest first)
    List<FavoriteResponse> getUserFavorites(UserEntity user);
}