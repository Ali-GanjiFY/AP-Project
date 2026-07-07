package org.example.backend.dto.response;

import org.example.backend.enums.AdvertisementStatusEnum;
import java.time.LocalDateTime;

public class AdvertisementSummaryResponse  {

    private final Long id;
    private final String title;
    private final Double price;
    private final String cityName;
    private final String categoryName;
    private final AdvertisementStatusEnum status;
    private final LocalDateTime createdAt;
    private final String mainImagePath;

    public AdvertisementSummaryResponse(Long id, String title, Double price, String cityName,
                                        String categoryName, AdvertisementStatusEnum status,
                                        LocalDateTime createdAt, String mainImagePath) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.cityName = cityName;
        this.categoryName = categoryName;
        this.status = status;
        this.createdAt = createdAt;
        this.mainImagePath = mainImagePath;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public Double getPrice() { return price; }
    public String getCityName() { return cityName; }
    public String getCategoryName() { return categoryName; }
    public AdvertisementStatusEnum getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getMainImagePath() { return mainImagePath; }
}

