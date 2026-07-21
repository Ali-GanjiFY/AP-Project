package org.example.backend.dto.response;

import org.example.backend.enums.AdvertisementStatusEnum;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents advertisement detail response.
 */
public class AdvertisementDetailResponse {

    private final Long id;
    private final String title;
    private final String description;
    private final Double price;
    private final AdvertisementStatusEnum status;
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

    /**
     * Constructs a new AdvertisementDetailResponse.
     * @param id the id
     * @param title the title
     * @param description the description
     * @param price the price
     * @param status the status
     * @param createdAt the created at
     * @param updatedAt the updated at
     * @param category the category
     * @param city the city
     * @param images the images
     * @param ownerId the owner id
     * @param ownerFullName the owner full name
     * @param ownerUsername the owner username
     * @param sellerAverageRating the seller average rating
     * @param sellerRatingCount the seller rating count
     * @param ownedByCurrentUser the owned by current user
     */
    public AdvertisementDetailResponse(Long id, String title, String description, Double price,
                                       AdvertisementStatusEnum status, LocalDateTime createdAt, LocalDateTime updatedAt,
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

    /**
     * Gets id.
     * @return the result
     */
    public Long getId() { return id; }
    /**
     * Gets title.
     * @return the result
     */
    public String getTitle() { return title; }
    /**
     * Gets description.
     * @return the result
     */
    public String getDescription() { return description; }
    /**
     * Gets price.
     * @return the result
     */
    public Double getPrice() { return price; }
    /**
     * Gets status.
     * @return the result
     */
    public AdvertisementStatusEnum getStatus() { return status; }
    /**
     * Gets created at.
     * @return the result
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
    /**
     * Gets updated at.
     * @return the result
     */
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    /**
     * Gets category.
     * @return the result
     */
    public CategoryResponse getCategory() { return category; }
    /**
     * Gets city.
     * @return the result
     */
    public CityResponse getCity() { return city; }
    /**
     * Gets images.
     * @return the result
     */
    public List<AdvertisementImageResponse> getImages() { return images; }
    /**
     * Gets owner id.
     * @return the result
     */
    public Long getOwnerId() { return ownerId; }
    /**
     * Gets owner full name.
     * @return the result
     */
    public String getOwnerFullName() { return ownerFullName; }
    /**
     * Gets owner username.
     * @return the result
     */
    public String getOwnerUsername() { return ownerUsername; }
    /**
     * Gets seller average rating.
     * @return the result
     */
    public Double getSellerAverageRating() { return sellerAverageRating; }
    /**
     * Gets seller rating count.
     * @return the result
     */
    public Long getSellerRatingCount() { return sellerRatingCount; }
    /**
     * Checks whether owned by current user.
     * @return the result
     */
    public boolean isOwnedByCurrentUser() { return ownedByCurrentUser; }
}

