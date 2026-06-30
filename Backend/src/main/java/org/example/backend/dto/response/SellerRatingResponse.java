package org.example.backend.dto.response;

import java.time.LocalDateTime;

public class SellerRatingResponse {

    private final Long id;
    private final Integer score;
    private final String comment;
    private final LocalDateTime ratedAt;
    private final String buyerUsername;

    public SellerRatingResponse(Long id, Integer score, String comment,
                                LocalDateTime ratedAt, String buyerUsername) {
        this.id = id;
        this.score = score;
        this.comment = comment;
        this.ratedAt = ratedAt;
        this.buyerUsername = buyerUsername;
    }

    public Long getId() { return id; }
    public Integer getScore() { return score; }
    public String getComment() { return comment; }
    public LocalDateTime getRatedAt() { return ratedAt; }
    public String getBuyerUsername() { return buyerUsername; }
}
