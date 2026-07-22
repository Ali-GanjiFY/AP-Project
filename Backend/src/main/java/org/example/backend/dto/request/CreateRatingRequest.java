package org.example.backend.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Represents create rating request.
 */
public class CreateRatingRequest {

    @NotNull(message = "Advertisement id is required")
    private Long advertisementId;

    @NotNull(message = "Score is required")
    @Min(value = 1, message = "Score must be between 1 and 5")
    @Max(value = 5, message = "Score must be between 1 and 5")
    private Integer score;

    private String comment;

    /**
     * Constructs a new CreateRatingRequest.
     */
    public CreateRatingRequest() {}

    /**
     * Constructs a new CreateRatingRequest.
     * @param advertisementId the advertisement id
     * @param score the score
     * @param comment the comment
     */
    public CreateRatingRequest(Long advertisementId, Integer score, String comment) {
        this.advertisementId = advertisementId;
        this.score = score;
        this.comment = comment;
    }


    /**
     * Gets advertisement id.
     * @return the result
     */
    public Long getAdvertisementId() { return advertisementId; }
    /**
     * Sets advertisement id.
     * @param advertisementId the advertisement id
     */
    public void setAdvertisementId(Long advertisementId) { this.advertisementId = advertisementId; }

    /**
     * Gets score.
     * @return the result
     */
    public Integer getScore() { return score; }
    /**
     * Sets score.
     * @param score the score
     */
    public void setScore(Integer score) { this.score = score; }

    /**
     * Gets comment.
     * @return the result
     */
    public String getComment() { return comment; }
    /**
     * Sets comment.
     * @param comment the comment
     */
    public void setComment(String comment) { this.comment = comment; }
}
