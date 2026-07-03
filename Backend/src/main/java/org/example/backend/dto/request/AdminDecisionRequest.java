package org.example.backend.dto.request;

import jakarta.validation.constraints.NotNull;
import org.example.backend.enums.AdvertisementStatus;
import org.example.backend.enums.ReviewDecision;

public class AdminDecisionRequest {

    @NotNull(message = "Decision is required")
    private ReviewDecision decision; // "ACTIVE" or "REJECTED"

    private String note;

    // Constructor
    public AdminDecisionRequest() {}

    public AdminDecisionRequest(ReviewDecision decision, String note) {
        this.decision = decision;
        this.note = note;
    }


    // Getters and Setters
    public ReviewDecision getDecision() { return decision; }
    public void setDecision(ReviewDecision decision) { this.decision = decision; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
