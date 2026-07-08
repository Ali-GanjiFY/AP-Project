package org.example.frontend.advertisement;


public class Advertisement {

    private Long id;
    private String title;
    private Double price;
    private String cityName;
    private String categoryName;
    private String status;
    private String createdAt;
    private String mainImagePath;

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public Double getPrice() { return price; }
    public String getCityName() { return cityName; }
    public String getCategoryName() { return categoryName; }
    public String getStatus() { return status; }
    public String getCreatedAt() { return createdAt; }
    public String getMainImagePath() { return mainImagePath; }
}