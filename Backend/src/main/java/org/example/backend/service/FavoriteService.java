package org.example.backend.service;

import org.example.backend.dto.response.FavoriteResponse;
import org.example.backend.entity.Advertisement;
import org.example.backend.entity.User;

import java.util.List;

public interface FavoriteService {

    // Add an advertisement to user's favorites (prevents duplicates)
    FavoriteResponse addFavorite(User user, Advertisement advertisement);

    // Remove an advertisement from user's favorites
    void removeFavorite(User user, Advertisement advertisement);

    // Check if an advertisement is in user's favorites
    boolean isFavorite(User user, Advertisement advertisement);

    // Get all favorites for a user (sorted by saved date descending)
    List<FavoriteResponse> getUserFavorites(User user);
}