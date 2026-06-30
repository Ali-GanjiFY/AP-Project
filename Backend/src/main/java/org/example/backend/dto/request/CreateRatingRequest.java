package org.example.backend.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CreateRatingRequest {

    @NotNull(message = "Advertisement id is required")
    private Long advertisementId;

    @NotNull(message = "Score is required")
    @Min(value = 1, message = "Score must be between 1 and 5")
    @Max(value = 5, message = "Score must be between 1 and 5")
    private Integer score;

    private String comment;

    // Constructor
    public CreateRatingRequest() {}

    public CreateRatingRequest(Long advertisementId, Integer score, String comment) {
        this.advertisementId = advertisementId;
        this.score = score;
        this.comment = comment;
    }


    // Getters and Setters
    public Long getAdvertisementId() { return advertisementId; }
    public void setAdvertisementId(Long advertisementId) { this.advertisementId = advertisementId; }

    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}
