package org.example.backend.dto.response;

import java.time.LocalDateTime;

/**
 * Represents seller rating response.
 */
public class SellerRatingResponse {

    private final Long id;
    private final Integer score;
    private final String comment;
    private final LocalDateTime ratedAt;
    private final String buyerUsername;

    /**
     * Constructs a new SellerRatingResponse.
     * @param id the id
     * @param score the score
     * @param comment the comment
     * @param ratedAt the rated at
     * @param buyerUsername the buyer username
     */
    public SellerRatingResponse(Long id, Integer score, String comment,
                                LocalDateTime ratedAt, String buyerUsername) {
        this.id = id;
        this.score = score;
        this.comment = comment;
        this.ratedAt = ratedAt;
        this.buyerUsername = buyerUsername;
    }

    /**
     * Gets id.
     * @return the result
     */
    public Long getId() { return id; }
    /**
     * Gets score.
     * @return the result
     */
    public Integer getScore() { return score; }
    /**
     * Gets comment.
     * @return the result
     */
    public String getComment() { return comment; }
    /**
     * Gets rated at.
     * @return the result
     */
    public LocalDateTime getRatedAt() { return ratedAt; }
    /**
     * Gets buyer username.
     * @return the result
     */
    public String getBuyerUsername() { return buyerUsername; }
}
