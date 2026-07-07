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

@RestController
@RequestMapping("/api/advertisements")
public class AdvertisementController {

    private final AdvertisementService advertisementService;
    private final UserService userService;

    public AdvertisementController(AdvertisementService advertisementService, UserService userService) {
        this.advertisementService = advertisementService;
        this.userService = userService;
    }

    // Resolves the authenticated User entity; used where login is required.
    private UserEntity currentUser(Authentication authentication) {
        return userService.getUserEntityByUsername(authentication.getName());
    }

    // Resolves the authenticated User entity, or null for anonymous/guest requests.
    // Used for public endpoints (e.g. ad detail) where "ownedByCurrentUser" still
    // needs to be computed correctly if the caller happens to be logged in.
    private UserEntity currentUserOrNull(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            return null;
        }
        return currentUser(authentication);
    }

    // GET /api/advertisements -> public, all active ads
    @GetMapping
    public ResponseEntity<List<AdvertisementSummaryResponse>> getAllActiveAds() {
        return ResponseEntity.ok(advertisementService.getAllActiveAds());
    }

    // GET /api/advertisements/search -> public, filter/search active ads
    @GetMapping("/search")
    public ResponseEntity<List<AdvertisementSummaryResponse>> searchAdvertisements(
            @ModelAttribute AdvertisementSearchRequest request) {
        return ResponseEntity.ok(advertisementService.searchAdvertisements(request));
    }

    // GET /api/advertisements/mine -> self, ads owned by the current user.
    // IMPORTANT: SecurityConfig's GET "/api/advertisements/**" permitAll matcher
    // also matches this path, so it would otherwise be reachable anonymously
    // (Authentication.getName() would be "anonymousUser", causing a lookup failure
    // instead of a clean 401/403). @PreAuthorize is mandatory here, not just
    // defense-in-depth -- please also tighten the SecurityConfig matcher itself.
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/mine")
    public ResponseEntity<List<AdvertisementSummaryResponse>> getMyAdvertisements(Authentication authentication) {
        return ResponseEntity.ok(advertisementService.getMyAdvertisements(currentUser(authentication)));
    }

    // GET /api/advertisements/{id} -> public, full detail. Ownership flag is
    // computed correctly whether the caller is logged in or a guest.
    @GetMapping("/{id}")
    public ResponseEntity<AdvertisementDetailResponse> getAdvertisementDetail(
            @PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(advertisementService.getAdvertisementDetail(id, currentUserOrNull(authentication)));
    }

    // POST /api/advertisements -> auth required, create new ad (starts PENDING)
    @PostMapping
    public ResponseEntity<AdvertisementDetailResponse> createAdvertisement(
            Authentication authentication, @Valid @RequestBody CreateAdvertisementRequest request) {
        AdvertisementDetailResponse response =
                advertisementService.createAdvertisement(currentUser(authentication), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // PUT /api/advertisements/{id} -> owner only (enforced in service), returns to PENDING
    @PutMapping("/{id}")
    public ResponseEntity<AdvertisementDetailResponse> updateAdvertisement(
            @PathVariable Long id, Authentication authentication,
            @Valid @RequestBody UpdateAdvertisementRequest request) {
        return ResponseEntity.ok(
                advertisementService.updateAdvertisement(id, currentUser(authentication), request));
    }

    // DELETE /api/advertisements/{id} -> owner or admin (enforced in service)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdvertisement(@PathVariable Long id, Authentication authentication) {
        advertisementService.deleteAdvertisement(id, currentUser(authentication));
        return ResponseEntity.noContent().build();
    }

    // PUT /api/advertisements/{id}/sold -> owner only (enforced in service)
    @PutMapping("/{id}/sold")
    public ResponseEntity<AdvertisementDetailResponse> markAsSold(
            @PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(advertisementService.markAsSold(id, currentUser(authentication)));
    }

}