package org.example.backend.controller;

import jakarta.validation.Valid;
import org.example.backend.dto.request.AdminDecisionRequest;
import org.example.backend.dto.response.AdminReviewResponse;
import org.example.backend.dto.response.AdvertisementSummaryResponse;
import org.example.backend.entity.UserEntity;
import org.example.backend.service.AdminReviewService;
import org.example.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminReviewController {

    private final AdminReviewService adminReviewService;
    private final UserService userService;

    public AdminReviewController(AdminReviewService adminReviewService, UserService userService) {
        this.adminReviewService = adminReviewService;
        this.userService = userService;
    }

    // Resolves the authenticated admin User entity from the JWT-backed Authentication.
    private UserEntity currentUser(Authentication authentication) {
        return userService.getUserEntityByUsername(authentication.getName());
    }

    // GET /api/admin/advertisements/pending -> admin only, list ads awaiting review.
    @GetMapping("/advertisements/pending")
    public ResponseEntity<List<AdvertisementSummaryResponse>> getPendingAdvertisements() {
        return ResponseEntity.ok(adminReviewService.getPendingAdvertisements());
    }

    // PUT /api/admin/advertisements/{id}/review -> admin only, approve/reject/remove
    @PutMapping("/advertisements/{id}/review")
    public ResponseEntity<AdminReviewResponse> reviewAdvertisement(
            @PathVariable Long id, Authentication authentication,
            @Valid @RequestBody AdminDecisionRequest request) {
        return ResponseEntity.ok(
                adminReviewService.reviewAdvertisement(currentUser(authentication), id, request));
    }

    // GET /api/admin/advertisements/{id}/review -> admin only, view the review
    @GetMapping("/advertisements/{id}/review")
    public ResponseEntity<AdminReviewResponse> getReviewByAdvertisementId(@PathVariable Long id) {
        return ResponseEntity.ok(adminReviewService.getReviewByAdvertisementId(id));
    }
}