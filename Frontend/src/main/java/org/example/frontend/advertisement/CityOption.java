package org.example.frontend.advertisement;

public class CityOption {
    private final Long id;
    private final String name;
    private final String province;

    public CityOption(Long id, String name) {
        this(id, name, null);
    }

    public CityOption(Long id, String name, String province) {
        this.id = id;
        this.name = name;
        this.province = province;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getProvince() { return province; }

    @Override
    public String toString() {
        return name;
    }
}