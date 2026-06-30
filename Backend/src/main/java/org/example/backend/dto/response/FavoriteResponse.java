package org.example.backend.dto.response;

import java.time.LocalDateTime;

public class FavoriteResponse {

    private final Long id;
    private final LocalDateTime savedAt;
    private final AdvertisementSummaryResponse advertisement;

    public FavoriteResponse(Long id, LocalDateTime savedAt, AdvertisementSummaryResponse advertisement) {
        this.id = id;
        this.savedAt = savedAt;
        this.advertisement = advertisement;
    }

    public Long getId() { return id; }
    public LocalDateTime getSavedAt() { return savedAt; }
    public AdvertisementSummaryResponse getAdvertisement() { return advertisement; }
}
