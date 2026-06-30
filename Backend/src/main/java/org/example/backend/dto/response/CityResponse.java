
package org.example.backend.dto.response;

public class CityResponse {

    private final Long id;
    private final String name;
    private final String province;

    public CityResponse(Long id, String name, String province) {
        this.id = id;
        this.name = name;
        this.province = province;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getProvince() { return province; }
}
