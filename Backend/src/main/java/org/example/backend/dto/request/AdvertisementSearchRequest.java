package org.example.backend.dto.request;

public class AdvertisementSearchRequest {

    private String keyword;
    private Long categoryId;
    private Long cityId;
    private Double minPrice;
    private Double maxPrice;
    private String sortBy;        // "date" or "price"
    private String sortDirection; // "asc" or "desc"

    // Constructor
    public AdvertisementSearchRequest() {}

    public AdvertisementSearchRequest(String keyword, Long categoryId, Long cityId, Double minPrice,
                                      Double maxPrice, String sortBy, String sortDirection) {
        this.keyword = keyword;
        this.categoryId = categoryId;
        this.cityId = cityId;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.sortBy = sortBy;
        this.sortDirection = sortDirection;
    }


    // Getters and Setters
    public String getKeyword() { return keyword; }
    public void setKeyword(String keyword) { this.keyword = keyword; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public Long getCityId() { return cityId; }
    public void setCityId(Long cityId) { this.cityId = cityId; }

    public Double getMinPrice() { return minPrice; }
    public void setMinPrice(Double minPrice) { this.minPrice = minPrice; }

    public Double getMaxPrice() { return maxPrice; }
    public void setMaxPrice(Double maxPrice) { this.maxPrice = maxPrice; }

    public String getSortBy() { return sortBy; }
    public void setSortBy(String sortBy) { this.sortBy = sortBy; }

    public String getSortDirection() { return sortDirection; }
    public void setSortDirection(String sortDirection) { this.sortDirection = sortDirection; }
}