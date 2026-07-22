package org.example.backend.dto.request;

/**
 * Represents advertisement search request.
 */
public class AdvertisementSearchRequest {

    private String keyword;
    private Long categoryId;
    private Long cityId;
    private Double minPrice;
    private Double maxPrice;
    private String sortBy;        // "date" or "price"
    private String sortDirection; // "asc" or "desc"

    /**
     * Constructs a new AdvertisementSearchRequest.
     */
    public AdvertisementSearchRequest() {}

    /**
     * Constructs a new AdvertisementSearchRequest.
     * @param keyword the keyword
     * @param categoryId the category id
     * @param cityId the city id
     * @param minPrice the min price
     * @param maxPrice the max price
     * @param sortBy the sort by
     * @param sortDirection the sort direction
     */
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


    /**
     * Gets keyword.
     * @return the result
     */
    public String getKeyword() { return keyword; }
    /**
     * Sets keyword.
     * @param keyword the keyword
     */
    public void setKeyword(String keyword) { this.keyword = keyword; }

    /**
     * Gets category id.
     * @return the result
     */
    public Long getCategoryId() { return categoryId; }
    /**
     * Sets category id.
     * @param categoryId the category id
     */
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    /**
     * Gets city id.
     * @return the result
     */
    public Long getCityId() { return cityId; }
    /**
     * Sets city id.
     * @param cityId the city id
     */
    public void setCityId(Long cityId) { this.cityId = cityId; }

    /**
     * Gets min price.
     * @return the result
     */
    public Double getMinPrice() { return minPrice; }
    /**
     * Sets min price.
     * @param minPrice the min price
     */
    public void setMinPrice(Double minPrice) { this.minPrice = minPrice; }

    /**
     * Gets max price.
     * @return the result
     */
    public Double getMaxPrice() { return maxPrice; }
    /**
     * Sets max price.
     * @param maxPrice the max price
     */
    public void setMaxPrice(Double maxPrice) { this.maxPrice = maxPrice; }

    /**
     * Gets sort by.
     * @return the result
     */
    public String getSortBy() { return sortBy; }
    /**
     * Sets sort by.
     * @param sortBy the sort by
     */
    public void setSortBy(String sortBy) { this.sortBy = sortBy; }

    /**
     * Gets sort direction.
     * @return the result
     */
    public String getSortDirection() { return sortDirection; }
    /**
     * Sets sort direction.
     * @param sortDirection the sort direction
     */
    public void setSortDirection(String sortDirection) { this.sortDirection = sortDirection; }
}