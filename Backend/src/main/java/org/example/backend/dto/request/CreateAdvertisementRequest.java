package org.example.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

/**
 * Represents create advertisement request.
 */
public class CreateAdvertisementRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private Double price;

    @NotNull(message = "Category is required")
    private Long categoryId;

    @NotNull(message = "City is required")
    private Long cityId;

    private List<String> imagePaths;

    /**
     * Constructs a new CreateAdvertisementRequest.
     */
    public CreateAdvertisementRequest() {
    }

    /**
     * Constructs a new CreateAdvertisementRequest.
     * @param title the title
     * @param description the description
     * @param price the price
     * @param categoryId the category id
     * @param cityId the city id
     * @param imagePaths the image paths
     */
    public CreateAdvertisementRequest(String title, String description, Double price,
                                      Long categoryId, Long cityId, List<String> imagePaths) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.categoryId = categoryId;
        this.cityId = cityId;
        this.imagePaths = imagePaths;
    }

    /**
     * Gets title.
     * @return the result
     */
    public String getTitle() {
        return title;
    }
    /**
     * Sets title.
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets description.
     * @return the result
     */
    public String getDescription() {
        return description;
    }
    /**
     * Sets description.
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets price.
     * @return the result
     */
    public Double getPrice() {
        return price;
    }
    /**
     * Sets price.
     * @param price the price
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     * Gets category id.
     * @return the result
     */
    public Long getCategoryId() {
        return categoryId;
    }
    /**
     * Sets category id.
     * @param categoryId the category id
     */
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * Gets city id.
     * @return the result
     */
    public Long getCityId() {
        return cityId;
    }
    /**
     * Sets city id.
     * @param cityId the city id
     */
    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    /**
     * Gets image paths.
     * @return the result
     */
    public List<String> getImagePaths() {
        return imagePaths;
    }
    /**
     * Sets image paths.
     * @param imagePaths the image paths
     */
    public void setImagePaths(List<String> imagePaths) {
        this.imagePaths = imagePaths;
    }
}
