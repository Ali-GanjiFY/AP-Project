package org.example.backend.dto.response;

public class  SellerRatingSummaryResponse {

    private final double averageScore;
    private final long totalCount;

    public SellerRatingSummaryResponse(double averageScore, long totalCount) {
        this.averageScore = averageScore;
        this.totalCount = totalCount;
    }

    public double getAverageScore() { return averageScore; }
    public long getTotalCount() { return totalCount; }
}

