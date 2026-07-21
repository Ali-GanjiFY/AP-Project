package org.example.backend.controller;

import jakarta.validation.Valid;
import org.example.backend.dto.request.CreateRatingRequest;
import org.example.backend.dto.response.SellerRatingResponse;
import org.example.backend.dto.response.SellerRatingSummaryResponse;
import org.example.backend.entity.UserEntity;
import org.example.backend.service.SellerRatingService;
import org.example.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
public class SellerRatingController {

    private final SellerRatingService sellerRatingService;
    private final UserService userService;

    public SellerRatingController(SellerRatingService sellerRatingService, UserService userService) {
        this.sellerRatingService = sellerRatingService;
        this.userService = userService;
    }

    // Resolves the authenticated User entity from the JWT-backed Authentication.
    private UserEntity currentUser(Authentication authentication) {
        return userService.getUserEntityByUsername(authentication.getName());
    }

    // POST /api/ratings -> self, create a new rating for a seller
    @PostMapping
    public ResponseEntity<SellerRatingResponse> createRating(
            Authentication authentication, @Valid @RequestBody CreateRatingRequest request) {
        SellerRatingResponse response =
                sellerRatingService.createRating(currentUser(authentication), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET /api/ratings/advertisements/{advertisementId} -> public, all ratings
    // left for a specific advertisement.
    @GetMapping("/advertisements/{advertisementId}")
    public ResponseEntity<List<SellerRatingResponse>> getRatingsByAdvertisement(
            @PathVariable Long advertisementId) {
        return ResponseEntity.ok(sellerRatingService.getRatingsByAdvertisement(advertisementId));
    }

    // GET /api/ratings/sellers/{sellerId} -> admin only, all ratings received by
    // a specific seller.
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/sellers/{sellerId}")
    public ResponseEntity<List<SellerRatingResponse>> getSellerRatings(@PathVariable Long sellerId) {
        UserEntity seller = userService.getUserEntityById(sellerId);
        return ResponseEntity.ok(sellerRatingService.getSellerRatings(seller));
    }

    // GET /api/ratings/sellers/{sellerId}/summary -> admin only, average score +
    // total count for a specific seller.
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/sellers/{sellerId}/summary")
    public ResponseEntity<SellerRatingSummaryResponse> getSellerRatingSummary(@PathVariable Long sellerId) {
        UserEntity seller = userService.getUserEntityById(sellerId);
        return ResponseEntity.ok(sellerRatingService.getSellerRatingSummary(seller));
    }

    // GET /api/ratings/me -> self, ratings received by the current logged-in user
    @GetMapping("/me")
    public ResponseEntity<List<SellerRatingResponse>> getMyRatings(Authentication authentication) {
        return ResponseEntity.ok(sellerRatingService.getSellerRatings(currentUser(authentication)));
    }

    // GET /api/ratings/me/summary -> self, rating summary for the current user
    @GetMapping("/me/summary")
    public ResponseEntity<SellerRatingSummaryResponse> getMyRatingSummary(Authentication authentication) {
        return ResponseEntity.ok(sellerRatingService.getSellerRatingSummary(currentUser(authentication)));
    }
}