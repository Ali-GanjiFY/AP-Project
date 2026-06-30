package org.example.backend.dto.response;

import org.example.backend.enums.AdvertisementStatus;
import java.time.LocalDateTime;
import java.util.List;

public class AdvertisementDetailResponse {

    private final Long id;
    private final String title;
    private final String description;
    private final Double price;
    private final AdvertisementStatus status;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    private final CategoryResponse category;
    private final CityResponse city;
    private final List<AdvertisementImageResponse> images;

    private final Long ownerId;
    private final String ownerFullName;
    private final String ownerUsername;

    private final Double sellerAverageRating;
    private final Long sellerRatingCount;

    private final boolean ownedByCurrentUser;

    public AdvertisementDetailResponse(Long id, String title, String description, Double price,
                                       AdvertisementStatus status, LocalDateTime createdAt, LocalDateTime updatedAt,
                                       CategoryResponse category, CityResponse city,
                                       List<AdvertisementImageResponse> images,
                                       Long ownerId, String ownerFullName, String ownerUsername,
                                       Double sellerAverageRating, Long sellerRatingCount,
                                       boolean ownedByCurrentUser) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.category = category;
        this.city = city;
        this.images = images;
        this.ownerId = ownerId;
        this.ownerFullName = ownerFullName;
        this.ownerUsername = ownerUsername;
        this.sellerAverageRating = sellerAverageRating;
        this.sellerRatingCount = sellerRatingCount;
        this.ownedByCurrentUser = ownedByCurrentUser;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Double getPrice() { return price; }
    public AdvertisementStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public CategoryResponse getCategory() { return category; }
    public CityResponse getCity() { return city; }
    public List<AdvertisementImageResponse> getImages() { return images; }
    public Long getOwnerId() { return ownerId; }
    public String getOwnerFullName() { return ownerFullName; }
    public String getOwnerUsername() { return ownerUsername; }
    public Double getSellerAverageRating() { return sellerAverageRating; }
    public Long getSellerRatingCount() { return sellerRatingCount; }
    public boolean isOwnedByCurrentUser() { return ownedByCurrentUser; }
}

