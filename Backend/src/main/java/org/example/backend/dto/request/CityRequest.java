package org.example.backend.dto.request;

import jakarta.validation.constraints.NotBlank;

public class CityRequest {

    @NotBlank(message = "City name is required")
    private String name;

    private String province;

    // Constructor
    public CityRequest() {
    }

    public CityRequest(String name, String province) {
        this.name = name;
        this.province = province;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getProvince() {
        return province;
    }
    public void setProvince(String province) {
        this.province = province;
    }
}
