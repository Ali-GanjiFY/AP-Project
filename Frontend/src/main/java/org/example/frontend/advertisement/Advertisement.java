package org.example.frontend.advertisement;


/**
 * Represents advertisement.
 */
public class Advertisement {

    private Long id;
    private String title;
    private Double price;
    private String cityName;
    private String categoryName;
    private String status;
    private String createdAt;
    private String mainImagePath;

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
    public String getStatus() { return status; }
    /**
     * Gets created at.
     * @return the result
     */
    public String getCreatedAt() { return createdAt; }
    /**
     * Gets main image path.
     * @return the result
     */
    public String getMainImagePath() { return mainImagePath; }
}