package org.example.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "seller_ratings",
        uniqueConstraints = @UniqueConstraint(columnNames = {"rater_id", "advertisement_id"}))
public class SellerRating {

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
    @JoinColumn(name = "rater_id", nullable = false)
    private User rater;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    @ManyToOne
    @JoinColumn(name = "advertisement_id", nullable = false)
    private Advertisement advertisement;

    // Constructors
    public SellerRating() {}

    public SellerRating(Integer score, String comment, User rater, User seller, Advertisement advertisement) {
        this.score = score;
        this.comment = comment;
        this.rater = rater;
        this.seller = seller;
        this.advertisement = advertisement;
        this.ratedAt = LocalDateTime.now();
    }

    // Getters
    public LocalDateTime getRatedAt() {
        return ratedAt;
    }

    public User getRater() {
        return rater;
    }

    public Advertisement getAdvertisement() {
        return advertisement;
    }

    public User getSeller() {
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

    public void setRater(User rater) {
        this.rater = rater;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    public void setAdvertisement(Advertisement advertisement) {
        this.advertisement = advertisement;
    }
}
