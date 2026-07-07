package org.example.backend.dto.request;

import jakarta.validation.constraints.NotNull;
import org.example.backend.enums.ReviewDecisionEnum;

public class AdminDecisionRequest {

    @NotNull(message = "Decision is required")
    private ReviewDecisionEnum decision; // "ACTIVE" or "REJECTED"

    private String note;

    // Constructor
    public AdminDecisionRequest() {}

    public AdminDecisionRequest(ReviewDecisionEnum decision, String note) {
        this.decision = decision;
        this.note = note;
    }


    // Getters and Setters
    public ReviewDecisionEnum getDecision() { return decision; }
    public void setDecision(ReviewDecisionEnum decision) { this.decision = decision; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
