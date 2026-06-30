package org.example.backend.dto.request;

import jakarta.validation.constraints.NotNull;
import org.example.backend.enums.AdvertisementStatus;

public class AdminDecisionRequest {

    @NotNull(message = "Decision is required")
    private AdvertisementStatus decision; // "ACTIVE" or "REJECTED"

    private String note;

    // Constructor
    public AdminDecisionRequest() {}

    public AdminDecisionRequest(AdvertisementStatus decision, String note) {
        this.decision = decision;
        this.note = note;
    }


    // Getters and Setters
    public AdvertisementStatus getDecision() { return decision; }
    public void setDecision(AdvertisementStatus decision) { this.decision = decision; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
