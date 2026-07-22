package org.example.backend.entity;

import jakarta.persistence.*;
import java.util.List;

/**
 * Represents city entity.
 */
@Entity
@Table(name = "cities")
public class CityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column
    private String province;

    @OneToMany(mappedBy = "city")
    private List<AdvertisementEntity> advertisements;

    /**
     * Constructs a new CityEntity.
     */
    public CityEntity() {}

    /**
     * Constructs a new CityEntity.
     * @param name the name
     */
    public CityEntity(String name) {
        this.name = name;
    }

    /**
     * Getters & Setters.
     * @return the result
     */
    public Long getId() { return id; }
    /**
     * Sets id.
     * @param id the id
     */
    public void setId(Long id) { this.id = id; }

    /**
     * Gets name.
     * @return the result
     */
    public String getName() { return name; }
    /**
     * Sets name.
     * @param name the name
     */
    public void setName(String name) { this.name = name; }

    /**
     * Gets advertisements.
     * @return the result
     */
    public List<AdvertisementEntity> getAdvertisements() { return advertisements; }
    /**
     * Sets advertisements.
     * @param advertisements the advertisements
     */
    public void setAdvertisements(List<AdvertisementEntity> advertisements) { this.advertisements = advertisements; }

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