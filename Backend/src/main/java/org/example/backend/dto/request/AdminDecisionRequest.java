package org.example.backend.dto.request;

import jakarta.validation.constraints.NotNull;
import org.example.backend.enums.ReviewDecisionEnum;

/**
 * Represents admin decision request.
 */
public class AdminDecisionRequest {

    @NotNull(message = "Decision is required")
    private ReviewDecisionEnum decision; // "ACTIVE" or "REJECTED"

    private String note;

    /**
     * Constructs a new AdminDecisionRequest.
     */
    public AdminDecisionRequest() {}

    /**
     * Constructs a new AdminDecisionRequest.
     * @param decision the decision
     * @param note the note
     */
    public AdminDecisionRequest(ReviewDecisionEnum decision, String note) {
        this.decision = decision;
        this.note = note;
    }


    /**
     * Gets decision.
     * @return the result
     */
    public ReviewDecisionEnum getDecision() { return decision; }
    /**
     * Sets decision.
     * @param decision the decision
     */
    public void setDecision(ReviewDecisionEnum decision) { this.decision = decision; }

    /**
     * Gets note.
     * @return the result
     */
    public String getNote() { return note; }
    /**
     * Sets note.
     * @param note the note
     */
    public void setNote(String note) { this.note = note; }
}
