package org.example.backend.dto.request;

import jakarta.validation.constraints.Positive;

import java.util.List;

public class UpdateAdvertisementRequest {

    private String title;
    private String description;

    @Positive(message = "Price must be positive")
    private Double price;

    private Long categoryId;
    private Long cityId;

    // لیست کامل نهایی مسیر عکس‌ها (باقی‌مانده‌های قدیمی + آپلودشده‌های جدید)
    // null یعنی عکس‌ها دست‌نخورده بمونن؛ لیست خالی هم توسط سرویس رد می‌شود
    private List<String> imagePaths;

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

    public List<String> getImagePaths() { return imagePaths; }
    public void setImagePaths(List<String> imagePaths) { this.imagePaths = imagePaths; }
}
