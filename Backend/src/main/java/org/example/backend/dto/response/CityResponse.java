
package org.example.backend.dto.response;

/**
 * Represents city response.
 */
public class CityResponse {

    private final Long id;
    private final String name;
    private final String province;

    /**
     * Constructs a new CityResponse.
     * @param id the id
     * @param name the name
     * @param province the province
     */
    public CityResponse(Long id, String name, String province) {
        this.id = id;
        this.name = name;
        this.province = province;
    }

    /**
     * Gets id.
     * @return the result
     */
    public Long getId() { return id; }
    /**
     * Gets name.
     * @return the result
     */
    public String getName() { return name; }
    /**
     * Gets province.
     * @return the result
     */
    public String getProvince() { return province; }
}
