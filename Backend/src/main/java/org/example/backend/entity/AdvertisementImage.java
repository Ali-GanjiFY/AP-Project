```java
package org.example.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "advertisement_images")
public class AdvertisementImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String imagePath;   // مسیر فایل تصویر

    @ManyToOne
    @JoinColumn(name = "advertisement_id", nullable = false)
    private Advertisement advertisement;

    // Constructors
    public AdvertisementImage() {}

    public AdvertisementImage(String imagePath, Advertisement advertisement) {
        this.imagePath = imagePath;
        this.advertisement = advertisement;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public Advertisement getAdvertisement() { return advertisement; }
    public void setAdvertisement(Advertisement advertisement) { this.advertisement = advertisement; }
}
```
