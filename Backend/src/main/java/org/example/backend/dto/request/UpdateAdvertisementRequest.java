package org.example.backend.dto.request;

import jakarta.validation.constraints.Positive;

import java.util.List;

public class UpdateAdvertisementRequest {

    // Optional fields; null means "no change"
    private String title;
    private String description;

    @Positive(message = "Price must be positive")
    private Double price;

    private Long categoryId;
    private Long cityId;

    // Final list of image paths (old remaining + newly uploaded).
    // null = images left unchanged; empty list is rejected by the service.
    private List<String> imagePaths;

    // Default constructor
    public UpdateAdvertisementRequest() {}

    // Convenience constructor for required fields
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