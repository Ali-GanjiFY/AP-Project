package org.example.backend.entity;

import jakarta.persistence.*;
import org.example.backend.enums.AdvertisementStatusEnum;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "advertisements")
public class AdvertisementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Double price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdvertisementStatusEnum status = AdvertisementStatusEnum.PENDING;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private UserEntity owner;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;

    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    private CityEntity city;

    @OneToMany(mappedBy = "advertisement", cascade = CascadeType.ALL)
    private List<AdvertisementImageEntity> images;

    @OneToMany(mappedBy = "advertisement", cascade = CascadeType.ALL)
    private List<ConversationEntity> conversations;

    @OneToMany(mappedBy = "advertisement", cascade = CascadeType.ALL)
    private List<FavoriteEntity> favorites;

    @OneToMany(mappedBy = "advertisement", cascade = CascadeType.ALL)
    private List<SellerRatingEntity> ratings;

    // Default constructor
    public AdvertisementEntity() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public AdvertisementStatusEnum getStatus() { return status; }
    public void setStatus(AdvertisementStatusEnum status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public UserEntity getOwner() { return owner; }
    public void setOwner(UserEntity owner) { this.owner = owner; }

    public CategoryEntity getCategory() { return category; }
    public void setCategory(CategoryEntity category) { this.category = category; }

    public CityEntity getCity() { return city; }
    public void setCity(CityEntity city) { this.city = city; }

    public List<AdvertisementImageEntity> getImages() { return images; }
    public void setImages(List<AdvertisementImageEntity> images) { this.images = images; }

    public List<ConversationEntity> getConversations() { return conversations; }
    public void setConversations(List<ConversationEntity> conversations) { this.conversations = conversations; }

    public List<FavoriteEntity> getFavorites() { return favorites; }
    public void setFavorites(List<FavoriteEntity> favorites) { this.favorites = favorites; }

    public List<SellerRatingEntity> getRatings() { return ratings; }
    public void setRatings(List<SellerRatingEntity> ratings) { this.ratings = ratings; }
}