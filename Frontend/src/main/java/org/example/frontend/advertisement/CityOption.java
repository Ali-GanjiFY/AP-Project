package org.example.frontend.advertisement;

/**
 * Represents city option.
 */
public class CityOption {
    private final Long id;
    private final String name;
    private final String province;

    /**
     * Constructs a new CityOption.
     * @param id the id
     * @param name the name
     */
    public CityOption(Long id, String name) {
        this(id, name, null);
    }

    /**
     * Constructs a new CityOption.
     * @param id the id
     * @param name the name
     * @param province the province
     */
    public CityOption(Long id, String name, String province) {
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

    /**
     * Converts to string.
     * @return the result
     */
    @Override
    public String toString() {
        return name;
    }
}