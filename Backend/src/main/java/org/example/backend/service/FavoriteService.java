package org.example.backend.service;

import org.example.backend.dto.response.FavoriteResponse;
import org.example.backend.entity.AdvertisementEntity;
import org.example.backend.entity.UserEntity;

import java.util.List;

public interface FavoriteService {

    // Add an advertisement to user's favorites (prevents duplicates)
    FavoriteResponse addFavorite(UserEntity user, AdvertisementEntity advertisement);

    // Remove an advertisement from user's favorites
    void removeFavorite(UserEntity user, AdvertisementEntity advertisement);

    // Check if an advertisement is in user's favorites
    boolean isFavorite(UserEntity user, AdvertisementEntity advertisement);

    // Get all favorites for a user (sorted by saved date descending)
    List<FavoriteResponse> getUserFavorites(UserEntity user);
}