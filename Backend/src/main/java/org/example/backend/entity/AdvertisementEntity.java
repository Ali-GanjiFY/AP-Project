package org.example.backend.entity;

import jakarta.persistence.*;
import org.example.backend.enums.AdvertisementStatus;
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
    private AdvertisementStatus status = AdvertisementStatus.PENDING;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column
    private LocalDateTime updatedAt;

    // رابطه با صاحب آگهی
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private UserEntity owner;

    // رابطه با دسته‌بندی
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;

    // رابطه با شهر
    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    private CityEntity city;

    // رابطه با تصاویر
    @OneToMany(mappedBy = "advertisement", cascade = CascadeType.ALL)
    private List<AdvertisementImageEntity> images;

    // رابطه با گفتگوها
    @OneToMany(mappedBy = "advertisement", cascade = CascadeType.ALL)
    private List<ConversationEntity> conversations;

    // رابطه با علاقه‌مندی‌ها
    @OneToMany(mappedBy = "advertisement", cascade = CascadeType.ALL)
    private List<FavoriteEntity> favorites;

    // رابطه با امتیازها
    @OneToMany(mappedBy = "advertisement", cascade = CascadeType.ALL)
    private List<SellerRatingEntity> ratings;

    // Constructors
    public AdvertisementEntity() {}

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public AdvertisementStatus getStatus() { return status; }
    public void setStatus(AdvertisementStatus status) { this.status = status; }

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