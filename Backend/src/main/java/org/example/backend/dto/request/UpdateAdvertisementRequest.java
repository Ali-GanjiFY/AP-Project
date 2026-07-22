package org.example.backend.dto.request;

import jakarta.validation.constraints.Positive;

import java.util.List;

/**
 * Represents update advertisement request.
 */
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

    /**
     * Default constructor.
     */
    public UpdateAdvertisementRequest() {}

    /**
     * Convenience constructor for required fields.
     * @param title the title
     * @param description the description
     * @param price the price
     * @param categoryId the category id
     * @param cityId the city id
     */
    public UpdateAdvertisementRequest(String title, String description, Double price, Long categoryId, Long cityId) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.categoryId = categoryId;
        this.cityId = cityId;
    }

    /**
     * Gets title.
     * @return the result
     */
    public String getTitle() { return title; }
    /**
     * Sets title.
     * @param title the title
     */
    public void setTitle(String title) { this.title = title; }

    /**
     * Gets description.
     * @return the result
     */
    public String getDescription() { return description; }
    /**
     * Sets description.
     * @param description the description
     */
    public void setDescription(String description) { this.description = description; }

    /**
     * Gets price.
     * @return the result
     */
    public Double getPrice() { return price; }
    /**
     * Sets price.
     * @param price the price
     */
    public void setPrice(Double price) { this.price = price; }

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
     * Gets image paths.
     * @return the result
     */
    public List<String> getImagePaths() { return imagePaths; }
    /**
     * Sets image paths.
     * @param imagePaths the image paths
     */
    public void setImagePaths(List<String> imagePaths) { this.imagePaths = imagePaths; }
}