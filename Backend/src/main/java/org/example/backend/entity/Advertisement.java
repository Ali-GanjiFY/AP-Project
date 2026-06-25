package org.example.backend.entity;

import jakarta.persistence.*;
import org.example.backend.enums.AdvertisementStatus;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "advertisements")
public class Advertisement {

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
    private User owner;

    // رابطه با دسته‌بندی
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    // رابطه با شهر
    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    // رابطه با تصاویر
    @OneToMany(mappedBy = "advertisement", cascade = CascadeType.ALL)
    private List<AdvertisementImage> images;

    // رابطه با گفتگوها
    @OneToMany(mappedBy = "advertisement", cascade = CascadeType.ALL)
    private List<Conversation> conversations;

    // رابطه با علاقه‌مندی‌ها
    @OneToMany(mappedBy = "advertisement", cascade = CascadeType.ALL)
    private List<Favorite> favorites;

    // رابطه با امتیازها
    @OneToMany(mappedBy = "advertisement", cascade = CascadeType.ALL)
    private List<SellerRating> ratings;

    // Constructors
    public Advertisement() {}

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

    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public City getCity() { return city; }
    public void setCity(City city) { this.city = city; }

    public List<AdvertisementImage> getImages() { return images; }
    public void setImages(List<AdvertisementImage> images) { this.images = images; }

    public List<Conversation> getConversations() { return conversations; }
    public void setConversations(List<Conversation> conversations) { this.conversations = conversations; }

    public List<Favorite> getFavorites() { return favorites; }
    public void setFavorites(List<Favorite> favorites) { this.favorites = favorites; }

    public List<SellerRating> getRatings() { return ratings; }
    public void setRatings(List<SellerRating> ratings) { this.ratings = ratings; }
}


