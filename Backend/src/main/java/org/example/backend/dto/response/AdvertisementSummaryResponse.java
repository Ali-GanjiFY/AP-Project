package org.example.backend.dto.response;

import org.example.backend.enums.AdvertisementStatusEnum;
import java.time.LocalDateTime;

/**
 * Represents advertisement summary response.
 */
public class AdvertisementSummaryResponse  {

    private final Long id;
    private final String title;
    private final Double price;
    private final String cityName;
    private final String categoryName;
    private final AdvertisementStatusEnum status;
    private final LocalDateTime createdAt;
    private final String mainImagePath;
    private final String rejectionReason;

    /**
     * Constructs a new AdvertisementSummaryResponse.
     * @param id the id
     * @param title the title
     * @param price the price
     * @param cityName the city name
     * @param categoryName the category name
     * @param status the status
     * @param createdAt the created at
     * @param mainImagePath the main image path
     */
    public AdvertisementSummaryResponse(Long id, String title, Double price, String cityName,
                                        String categoryName, AdvertisementStatusEnum status,
                                        LocalDateTime createdAt, String mainImagePath) {
        this(id, title, price, cityName, categoryName, status, createdAt, mainImagePath, null);
    }

    /**
     * Constructs a new AdvertisementSummaryResponse.
     * @param id the id
     * @param title the title
     * @param price the price
     * @param cityName the city name
     * @param categoryName the category name
     * @param status the status
     * @param createdAt the created at
     * @param mainImagePath the main image path
     * @param rejectionReason the admin's note when the ad was rejected (null otherwise)
     */
    public AdvertisementSummaryResponse(Long id, String title, Double price, String cityName,
                                        String categoryName, AdvertisementStatusEnum status,
                                        LocalDateTime createdAt, String mainImagePath, String rejectionReason) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.cityName = cityName;
        this.categoryName = categoryName;
        this.status = status;
        this.createdAt = createdAt;
        this.mainImagePath = mainImagePath;
        this.rejectionReason = rejectionReason;
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
     * Gets price.
     * @return the result
     */
    public Double getPrice() { return price; }
    /**
     * Gets city name.
     * @return the result
     */
    public String getCityName() { return cityName; }
    /**
     * Gets category name.
     * @return the result
     */
    public String getCategoryName() { return categoryName; }
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
     * Gets main image path.
     * @return the result
     */
    public String getMainImagePath() { return mainImagePath; }
    /**
     * Gets rejection reason (admin note). Null unless status is REJECTED.
     * @return the result
     */
    public String getRejectionReason() { return rejectionReason; }
}