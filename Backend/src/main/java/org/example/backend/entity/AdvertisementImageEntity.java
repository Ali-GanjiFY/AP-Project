package org.example.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "advertisement_images")
public class AdvertisementImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String imagePath;

    @ManyToOne
    @JoinColumn(name = "advertisement_id", nullable = false)
    private AdvertisementEntity advertisement;

    // Constructors
    public AdvertisementImageEntity() {}

    public AdvertisementImageEntity(String imagePath, AdvertisementEntity advertisement) {
        this.imagePath = imagePath;
        this.advertisement = advertisement;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public AdvertisementEntity getAdvertisement() { return advertisement; }
    public void setAdvertisement(AdvertisementEntity advertisement) { this.advertisement = advertisement; }
}