package org.example.backend.entity;

import jakarta.persistence.*;

/**
 * Represents advertisement image entity.
 */
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

    /**
     * Constructs a new AdvertisementImageEntity.
     */
    public AdvertisementImageEntity() {}

    /**
     * Constructs a new AdvertisementImageEntity.
     * @param imagePath the image path
     * @param advertisement the advertisement
     */
    public AdvertisementImageEntity(String imagePath, AdvertisementEntity advertisement) {
        this.imagePath = imagePath;
        this.advertisement = advertisement;
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
     * Gets image path.
     * @return the result
     */
    public String getImagePath() { return imagePath; }
    /**
     * Sets image path.
     * @param imagePath the image path
     */
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    /**
     * Gets advertisement.
     * @return the result
     */
    public AdvertisementEntity getAdvertisement() { return advertisement; }
    /**
     * Sets advertisement.
     * @param advertisement the advertisement
     */
    public void setAdvertisement(AdvertisementEntity advertisement) { this.advertisement = advertisement; }
}