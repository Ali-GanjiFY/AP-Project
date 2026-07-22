package org.example.backend.dto.response;

/**
 * Represents advertisement image response.
 */
public class AdvertisementImageResponse {

    private final Long id;
    private final String imagePath;

    /**
     * Constructs a new AdvertisementImageResponse.
     * @param id the id
     * @param imagePath the image path
     */
    public AdvertisementImageResponse(Long id, String imagePath) {
        this.id = id;
        this.imagePath = imagePath;
    }

    /**
     * Gets id.
     * @return the result
     */
    public Long getId() { return id; }
    /**
     * Gets image path.
     * @return the result
     */
    public String getImagePath() { return imagePath; }
}

