package org.example.backend.controller;

import jakarta.validation.Valid;
import org.example.backend.dto.request.AdvertisementSearchRequest;
import org.example.backend.dto.request.CreateAdvertisementRequest;
import org.example.backend.dto.request.UpdateAdvertisementRequest;
import org.example.backend.dto.response.AdvertisementDetailResponse;
import org.example.backend.dto.response.AdvertisementSummaryResponse;
import org.example.backend.entity.UserEntity;
import org.example.backend.service.AdvertisementService;
import org.example.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Represents advertisement controller.
 */
@RestController
@RequestMapping("/api/advertisements")
public class AdvertisementController {

    private final AdvertisementService advertisementService;
    private final UserService userService;

    /**
     * Constructs a new AdvertisementController.
     * @param advertisementService the advertisement service
     * @param userService the user service
     */
    public AdvertisementController(AdvertisementService advertisementService, UserService userService) {
        this.advertisementService = advertisementService;
        this.userService = userService;
    }

    /**
     * Resolves the authenticated User entity; used where login is required.
     * @param authentication the authentication
     * @return the result
     */
    private UserEntity currentUser(Authentication authentication) {
        return userService.getUserEntityByUsername(authentication.getName());
    }

    /**
     * Resolves the authenticated User entity, or null for anonymous/guest requests. Used for public endpoints (e.g. ad detail) where "ownedByCurrentUser" still needs to be computed correctly if the caller happens to be logged in.
     * @param authentication the authentication
     * @return the result
     */
    private UserEntity currentUserOrNull(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            return null;
        }
        return currentUser(authentication);
    }

    /**
     * GET /api/advertisements -> public, all active ads.
     * @return the result
     */
    @GetMapping
    public ResponseEntity<List<AdvertisementSummaryResponse>> getAllActiveAds() {
        return ResponseEntity.ok(advertisementService.getAllActiveAds());
    }

    /**
     * GET /api/advertisements/search -> public, filter/search active ads.
     * @param request the request
     * @return the result
     */
    @GetMapping("/search")
    public ResponseEntity<List<AdvertisementSummaryResponse>> searchAdvertisements(
            @ModelAttribute AdvertisementSearchRequest request) {
        return ResponseEntity.ok(advertisementService.searchAdvertisements(request));
    }

    /**
     * GET /api/advertisements/mine -> self, ads owned by the current user.
     * @param authentication the authentication
     * @return the result
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/mine")
    public ResponseEntity<List<AdvertisementSummaryResponse>> getMyAdvertisements(Authentication authentication) {
        return ResponseEntity.ok(advertisementService.getMyAdvertisements(currentUser(authentication)));
    }

    /**
     * GET /api/advertisements/{id} -> public, full detail. Ownership flag is computed correctly whether the caller is logged in or a guest.
     * @param id the id
     * @param authentication the authentication
     * @return the result
     */
    @GetMapping("/{id}")
    public ResponseEntity<AdvertisementDetailResponse> getAdvertisementDetail(
            @PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(advertisementService.getAdvertisementDetail(id, currentUserOrNull(authentication)));
    }

    /**
     * POST /api/advertisements -> auth required, create new ad (starts PENDING).
     * @param authentication the authentication
     * @param request the request
     * @return the result
     */
    @PostMapping
    public ResponseEntity<AdvertisementDetailResponse> createAdvertisement(
            Authentication authentication, @Valid @RequestBody CreateAdvertisementRequest request) {
        AdvertisementDetailResponse response =
                advertisementService.createAdvertisement(currentUser(authentication), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * PUT /api/advertisements/{id} -> owner only (enforced in service), returns to PENDING.
     * @param id the id
     * @param authentication the authentication
     * @param request the request
     * @return the result
     */
    @PutMapping("/{id}")
    public ResponseEntity<AdvertisementDetailResponse> updateAdvertisement(
            @PathVariable Long id, Authentication authentication,
            @Valid @RequestBody UpdateAdvertisementRequest request) {
        return ResponseEntity.ok(
                advertisementService.updateAdvertisement(id, currentUser(authentication), request));
    }

    /**
     * DELETE /api/advertisements/{id} -> owner or admin (enforced in service).
     * @param id the id
     * @param authentication the authentication
     * @return the result
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdvertisement(@PathVariable Long id, Authentication authentication) {
        advertisementService.deleteAdvertisement(id, currentUser(authentication));
        return ResponseEntity.noContent().build();
    }

    /**
     * PUT /api/advertisements/{id}/sold -> owner only (enforced in service).
     * @param id the id
     * @param authentication the authentication
     * @return the result
     */
    @PutMapping("/{id}/sold")
    public ResponseEntity<AdvertisementDetailResponse> markAsSold(
            @PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(advertisementService.markAsSold(id, currentUser(authentication)));
    }

}