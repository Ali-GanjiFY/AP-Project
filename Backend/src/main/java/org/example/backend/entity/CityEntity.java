package org.example.backend.entity;

import jakarta.persistence.*;
import java.util.List;

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

    // Constructors
    public CityEntity() {}

    public CityEntity(String name) {
        this.name = name;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<AdvertisementEntity> getAdvertisements() { return advertisements; }
    public void setAdvertisements(List<AdvertisementEntity> advertisements) { this.advertisements = advertisements; }

    public String getProvince() {
        return province;
    }
    public void setProvince(String province) {
        this.province = province;
    }
}