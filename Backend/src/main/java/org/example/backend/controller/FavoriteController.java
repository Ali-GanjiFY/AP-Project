package org.example.backend.controller;

import org.example.backend.dto.response.FavoriteResponse;
import org.example.backend.entity.Advertisement;
import org.example.backend.entity.User;
import org.example.backend.service.AdvertisementService;
import org.example.backend.service.FavoriteService;
import org.example.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final AdvertisementService advertisementService;
    private final UserService userService;

    public FavoriteController(FavoriteService favoriteService,
                              AdvertisementService advertisementService,
                              UserService userService) {
        this.favoriteService = favoriteService;
        this.advertisementService = advertisementService;
        this.userService = userService;
    }

    // Resolves the authenticated User entity from the JWT-backed Authentication.
    private User currentUser(Authentication authentication) {
        return userService.getUserEntityByUsername(authentication.getName());
    }

    // GET /api/favorites -> self, list of the current user's favorite advertisements
    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> getMyFavorites(Authentication authentication) {
        return ResponseEntity.ok(favoriteService.getUserFavorites(currentUser(authentication)));
    }

    // POST /api/favorites/{advertisementId} -> self, add an advertisement to favorites
    @PostMapping("/{advertisementId}")
    public ResponseEntity<FavoriteResponse> addFavorite(
            @PathVariable Long advertisementId, Authentication authentication) {
        Advertisement advertisement = advertisementService.getAdvertisementEntityById(advertisementId);
        FavoriteResponse response = favoriteService.addFavorite(currentUser(authentication), advertisement);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // DELETE /api/favorites/{advertisementId} -> self, remove an advertisement from favorites
    @DeleteMapping("/{advertisementId}")
    public ResponseEntity<Void> removeFavorite(
            @PathVariable Long advertisementId, Authentication authentication) {
        Advertisement advertisement = advertisementService.getAdvertisementEntityById(advertisementId);
        favoriteService.removeFavorite(currentUser(authentication), advertisement);
        return ResponseEntity.noContent().build();
    }

    // GET /api/favorites/{advertisementId}/status -> self, check whether an
    @GetMapping("/{advertisementId}/status")
    public ResponseEntity<Boolean> isFavorite(
            @PathVariable Long advertisementId, Authentication authentication) {
        Advertisement advertisement = advertisementService.getAdvertisementEntityById(advertisementId);
        return ResponseEntity.ok(favoriteService.isFavorite(currentUser(authentication), advertisement));
    }
}