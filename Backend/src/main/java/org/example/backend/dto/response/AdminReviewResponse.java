package org.example.backend.dto.response;

import org.example.backend.enums.ReviewDecisionEnum;

import java.time.LocalDateTime;

/**
 * Represents admin review response.
 */
public class AdminReviewResponse {

    private final Long id;
    private final ReviewDecisionEnum decision;
    private final String note;
    private final LocalDateTime reviewedAt;
    private final String adminUsername;
    private final Long advertisementId;

    /**
     * Constructs a new AdminReviewResponse.
     * @param id the id
     * @param decision the decision
     * @param note the note
     * @param reviewedAt the reviewed at
     * @param adminUsername the admin username
     * @param advertisementId the advertisement id
     */
    public AdminReviewResponse(Long id, ReviewDecisionEnum decision, String note,
                               LocalDateTime reviewedAt, String adminUsername, Long advertisementId) {
        this.id = id;
        this.decision = decision;
        this.note = note;
        this.reviewedAt = reviewedAt;
        this.adminUsername = adminUsername;
        this.advertisementId = advertisementId;
    }

    /**
     * Gets id.
     * @return the result
     */
    public Long getId() { return id; }
    /**
     * Gets decision.
     * @return the result
     */
    public ReviewDecisionEnum getDecision() { return decision; }
    /**
     * Gets note.
     * @return the result
     */
    public String getNote() { return note; }
    /**
     * Gets reviewed at.
     * @return the result
     */
    public LocalDateTime getReviewedAt() { return reviewedAt; }
    /**
     * Gets admin username.
     * @return the result
     */
    public String getAdminUsername() { return adminUsername; }
    /**
     * Gets advertisement id.
     * @return the result
     */
    public Long getAdvertisementId() { return advertisementId; }
}

