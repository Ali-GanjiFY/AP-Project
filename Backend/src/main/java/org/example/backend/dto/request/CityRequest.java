package org.example.backend.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * Represents city request.
 */
public class CityRequest {

    @NotBlank(message = "City name is required")
    private String name;

    private String province;

    /**
     * Constructs a new CityRequest.
     */
    public CityRequest() {
    }

    /**
     * Constructs a new CityRequest.
     * @param name the name
     * @param province the province
     */
    public CityRequest(String name, String province) {
        this.name = name;
        this.province = province;
    }

    /**
     * Gets name.
     * @return the result
     */
    public String getName() {
        return name;
    }
    /**
     * Sets name.
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets province.
     * @return the result
     */
    public String getProvince() {
        return province;
    }
    /**
     * Sets province.
     * @param province the province
     */
    public void setProvince(String province) {
        this.province = province;
    }
}
