package org.example.backend.dto.response;

public class AdvertisementImageResponse {

    private final Long id;
    private final String imagePath;

    public AdvertisementImageResponse(Long id, String imagePath) {
        this.id = id;
        this.imagePath = imagePath;
    }

    public Long getId() { return id; }
    public String getImagePath() { return imagePath; }
}

