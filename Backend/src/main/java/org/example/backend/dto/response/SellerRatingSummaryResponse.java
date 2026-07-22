package org.example.backend.dto.response;

/**
 * Represents seller rating summary response.
 */
public class  SellerRatingSummaryResponse {

    private final double averageScore;
    private final long totalCount;

    /**
     * Constructs a new SellerRatingSummaryResponse.
     * @param averageScore the average score
     * @param totalCount the total count
     */
    public SellerRatingSummaryResponse(double averageScore, long totalCount) {
        this.averageScore = averageScore;
        this.totalCount = totalCount;
    }

    /**
     * Gets average score.
     * @return the result
     */
    public double getAverageScore() { return averageScore; }
    /**
     * Gets total count.
     * @return the result
     */
    public long getTotalCount() { return totalCount; }
}

