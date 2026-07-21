package org.example.frontend.advertisement;

import java.util.List;


/**
 * Represents advertisement detail.
 */
public class AdvertisementDetail {

    private Long id;
    private String title;
    private String description;
    private Double price;
    private String status;
    private String createdAt;
    private String updatedAt;

    private CategoryInfo category;
    private CityInfo city;
    private List<ImageInfo> images;

    private Long ownerId;
    private String ownerFullName;
    private String ownerUsername;

    private Double sellerAverageRating;
    private Long sellerRatingCount;

    private boolean ownedByCurrentUser;

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
    public String getStatus() { return status; }
    /**
     * Gets created at.
     * @return the result
     */
    public String getCreatedAt() { return createdAt; }
    /**
     * Gets updated at.
     * @return the result
     */
    public String getUpdatedAt() { return updatedAt; }
    /**
     * Gets category.
     * @return the result
     */
    public CategoryInfo getCategory() { return category; }
    /**
     * Gets city.
     * @return the result
     */
    public CityInfo getCity() { return city; }
    /**
     * Gets images.
     * @return the result
     */
    public List<ImageInfo> getImages() { return images; }
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

    /**
     * Gets category name.
     * @return the result
     */
    public String getCategoryName() {
        return category != null ? category.getName() : null;
    }

    /**
     * Gets city name.
     * @return the result
     */
    public String getCityName() {
        return city != null ? city.getName() : null;
    }

    /**
     * Checks whether images.
     * @return the result
     */
    public boolean hasImages() {
        return images != null && !images.isEmpty();
    }

    /**
     * Represents category info.
     */
    public static class CategoryInfo {
        private Long id;
        private String name;
        /**
         * Gets id.
         * @return the result
         */
        public Long getId() { return id; }
        /**
         * Gets name.
         * @return the result
         */
        public String getName() { return name; }
    }

    /**
     * Represents city info.
     */
    public static class CityInfo {
        private Long id;
        private String name;
        private String province;
        /**
         * Gets id.
         * @return the result
         */
        public Long getId() { return id; }
        /**
         * Gets name.
         * @return the result
         */
        public String getName() { return name; }
        /**
         * Gets province.
         * @return the result
         */
        public String getProvince() { return province; }
    }

    /**
     * Represents image info.
     */
    public static class ImageInfo {
        private Long id;
        private String imagePath;
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
}