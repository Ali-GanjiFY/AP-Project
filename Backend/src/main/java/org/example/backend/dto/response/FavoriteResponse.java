package org.example.backend.dto.response;

import java.time.LocalDateTime;

/**
 * Represents favorite response.
 */
public class FavoriteResponse {

    private final Long id;
    private final LocalDateTime savedAt;
    private final AdvertisementSummaryResponse advertisement;

    /**
     * Constructs a new FavoriteResponse.
     * @param id the id
     * @param savedAt the saved at
     * @param advertisement the advertisement
     */
    public FavoriteResponse(Long id, LocalDateTime savedAt, AdvertisementSummaryResponse advertisement) {
        this.id = id;
        this.savedAt = savedAt;
        this.advertisement = advertisement;
    }

    /**
     * Gets id.
     * @return the result
     */
    public Long getId() { return id; }
    /**
     * Gets saved at.
     * @return the result
     */
    public LocalDateTime getSavedAt() { return savedAt; }
    /**
     * Gets advertisement.
     * @return the result
     */
    public AdvertisementSummaryResponse getAdvertisement() { return advertisement; }
}
