package org.example.backend.dto.response;

import org.example.backend.enums.AdvertisementStatus;
import java.time.LocalDateTime;

public class AdminReviewResponse {

    private final Long id;
    private final AdvertisementStatus decision;
    private final String note;
    private final LocalDateTime reviewedAt;
    private final String adminUsername;
    private final Long advertisementId;

    public AdminReviewResponse(Long id, AdvertisementStatus decision, String note,
                               LocalDateTime reviewedAt, String adminUsername, Long advertisementId) {
        this.id = id;
        this.decision = decision;
        this.note = note;
        this.reviewedAt = reviewedAt;
        this.adminUsername = adminUsername;
        this.advertisementId = advertisementId;
    }

    public Long getId() { return id; }
    public AdvertisementStatus getDecision() { return decision; }
    public String getNote() { return note; }
    public LocalDateTime getReviewedAt() { return reviewedAt; }
    public String getAdminUsername() { return adminUsername; }
    public Long getAdvertisementId() { return advertisementId; }
}

