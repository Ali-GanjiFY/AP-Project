package org.example.frontend.advertisement;

import java.util.List;


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

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Double getPrice() { return price; }
    public String getStatus() { return status; }
    public String getCreatedAt() { return createdAt; }
    public String getUpdatedAt() { return updatedAt; }
    public CategoryInfo getCategory() { return category; }
    public CityInfo getCity() { return city; }
    public List<ImageInfo> getImages() { return images; }
    public Long getOwnerId() { return ownerId; }
    public String getOwnerFullName() { return ownerFullName; }
    public String getOwnerUsername() { return ownerUsername; }
    public Double getSellerAverageRating() { return sellerAverageRating; }
    public Long getSellerRatingCount() { return sellerRatingCount; }
    public boolean isOwnedByCurrentUser() { return ownedByCurrentUser; }

    public String getCategoryName() {
        return category != null ? category.getName() : null;
    }

    public String getCityName() {
        return city != null ? city.getName() : null;
    }

    public boolean hasImages() {
        return images != null && !images.isEmpty();
    }

    public static class CategoryInfo {
        private Long id;
        private String name;
        public Long getId() { return id; }
        public String getName() { return name; }
    }

    public static class CityInfo {
        private Long id;
        private String name;
        private String province;
        public Long getId() { return id; }
        public String getName() { return name; }
        public String getProvince() { return province; }
    }

    public static class ImageInfo {
        private Long id;
        private String imagePath;
        public Long getId() { return id; }
        public String getImagePath() { return imagePath; }
    }
}