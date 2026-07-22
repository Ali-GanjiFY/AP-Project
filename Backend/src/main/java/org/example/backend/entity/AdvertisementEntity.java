package org.example.backend.entity;

import jakarta.persistence.*;
import org.example.backend.enums.AdvertisementStatusEnum;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents advertisement entity.
 */
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

    /**
     * Default constructor.
     */
    public AdvertisementEntity() {}

    /**
     * Gets id.
     * @return the result
     */
    public Long getId() { return id; }
    /**
     * Sets id.
     * @param id the id
     */
    public void setId(Long id) { this.id = id; }

    /**
     * Gets title.
     * @return the result
     */
    public String getTitle() { return title; }
    /**
     * Sets title.
     * @param title the title
     */
    public void setTitle(String title) { this.title = title; }

    /**
     * Gets description.
     * @return the result
     */
    public String getDescription() { return description; }
    /**
     * Sets description.
     * @param description the description
     */
    public void setDescription(String description) { this.description = description; }

    /**
     * Gets price.
     * @return the result
     */
    public Double getPrice() { return price; }
    /**
     * Sets price.
     * @param price the price
     */
    public void setPrice(Double price) { this.price = price; }

    /**
     * Gets status.
     * @return the result
     */
    public AdvertisementStatusEnum getStatus() { return status; }
    /**
     * Sets status.
     * @param status the status
     */
    public void setStatus(AdvertisementStatusEnum status) { this.status = status; }

    /**
     * Gets created at.
     * @return the result
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
    /**
     * Sets created at.
     * @param createdAt the created at
     */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    /**
     * Gets updated at.
     * @return the result
     */
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    /**
     * Sets updated at.
     * @param updatedAt the updated at
     */
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    /**
     * Gets owner.
     * @return the result
     */
    public UserEntity getOwner() { return owner; }
    /**
     * Sets owner.
     * @param owner the owner
     */
    public void setOwner(UserEntity owner) { this.owner = owner; }

    /**
     * Gets category.
     * @return the result
     */
    public CategoryEntity getCategory() { return category; }
    /**
     * Sets category.
     * @param category the category
     */
    public void setCategory(CategoryEntity category) { this.category = category; }

    /**
     * Gets city.
     * @return the result
     */
    public CityEntity getCity() { return city; }
    /**
     * Sets city.
     * @param city the city
     */
    public void setCity(CityEntity city) { this.city = city; }

    /**
     * Gets images.
     * @return the result
     */
    public List<AdvertisementImageEntity> getImages() { return images; }
    /**
     * Sets images.
     * @param images the images
     */
    public void setImages(List<AdvertisementImageEntity> images) { this.images = images; }

    /**
     * Gets conversations.
     * @return the result
     */
    public List<ConversationEntity> getConversations() { return conversations; }
    /**
     * Sets conversations.
     * @param conversations the conversations
     */
    public void setConversations(List<ConversationEntity> conversations) { this.conversations = conversations; }

    /**
     * Gets favorites.
     * @return the result
     */
    public List<FavoriteEntity> getFavorites() { return favorites; }
    /**
     * Sets favorites.
     * @param favorites the favorites
     */
    public void setFavorites(List<FavoriteEntity> favorites) { this.favorites = favorites; }

    /**
     * Gets ratings.
     * @return the result
     */
    public List<SellerRatingEntity> getRatings() { return ratings; }
    /**
     * Sets ratings.
     * @param ratings the ratings
     */
    public void setRatings(List<SellerRatingEntity> ratings) { this.ratings = ratings; }
}