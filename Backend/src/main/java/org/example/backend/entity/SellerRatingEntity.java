package org.example.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "seller_ratings",
        uniqueConstraints = @UniqueConstraint(columnNames = {"buyer_id", "advertisement_id"}))
public class SellerRatingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer score; // 1 to 5

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column(nullable = false)
    private LocalDateTime ratedAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "buyer_id", nullable = false)
    private UserEntity buyer;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private UserEntity seller;

    @ManyToOne
    @JoinColumn(name = "advertisement_id", nullable = false)
    private AdvertisementEntity advertisement;

    // Constructors
    public SellerRatingEntity() {}

    public SellerRatingEntity(Integer score, String comment, UserEntity buyer, UserEntity seller, AdvertisementEntity advertisement) {
        this.score = score;
        this.comment = comment;
        this.buyer = buyer;
        this.seller = seller;
        this.advertisement = advertisement;
        this.ratedAt = LocalDateTime.now();
    }

    // Getters
    public LocalDateTime getRatedAt() {
        return ratedAt;
    }

    public UserEntity getBuyer() {
        return buyer;
    }

    public AdvertisementEntity getAdvertisement() {
        return advertisement;
    }

    public UserEntity getSeller() {
        return seller;
    }

    public String getComment() {
        return comment;
    }

    public Integer getScore() {
        return score;
    }

    public Long getId() {
        return id;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setRatedAt(LocalDateTime ratedAt) {
        this.ratedAt = ratedAt;
    }

    public void setBuyer(UserEntity buyer) {
        this.buyer = buyer;
    }

    public void setSeller(UserEntity seller) {
        this.seller = seller;
    }

    public void setAdvertisement(AdvertisementEntity advertisement) {
        this.advertisement = advertisement;
    }
}