package org.example.backend.dto.request;

import jakarta.validation.constraints.Positive;

public class UpdateAdvertisementRequest {

    private String title;
    private String description;

    @Positive(message = "Price must be positive")
    private Double price;

    private Long categoryId;
    private Long cityId;

    // Constructor
    public UpdateAdvertisementRequest() {}

    public UpdateAdvertisementRequest(String title, String description, Double price, Long categoryId, Long cityId) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.categoryId = categoryId;
        this.cityId = cityId;
    }


    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public Long getCityId() { return cityId; }
    public void setCityId(Long cityId) { this.cityId = cityId; }
}
