package org.example.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "seller_ratings",
        uniqueConstraints = @UniqueConstraint(columnNames = {"buyer_id", "advertisement_id"}))
/**
 * Represents seller rating entity.
 */
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

    /**
     * Constructs a new SellerRatingEntity.
     */
    public SellerRatingEntity() {}

    /**
     * Constructs a new SellerRatingEntity.
     * @param score the score
     * @param comment the comment
     * @param buyer the buyer
     * @param seller the seller
     * @param advertisement the advertisement
     */
    public SellerRatingEntity(Integer score, String comment, UserEntity buyer, UserEntity seller, AdvertisementEntity advertisement) {
        this.score = score;
        this.comment = comment;
        this.buyer = buyer;
        this.seller = seller;
        this.advertisement = advertisement;
        this.ratedAt = LocalDateTime.now();
    }

    /**
     * Gets rated at.
     * @return the result
     */
    public LocalDateTime getRatedAt() {
        return ratedAt;
    }

    /**
     * Gets buyer.
     * @return the result
     */
    public UserEntity getBuyer() {
        return buyer;
    }

    /**
     * Gets advertisement.
     * @return the result
     */
    public AdvertisementEntity getAdvertisement() {
        return advertisement;
    }

    /**
     * Gets seller.
     * @return the result
     */
    public UserEntity getSeller() {
        return seller;
    }

    /**
     * Gets comment.
     * @return the result
     */
    public String getComment() {
        return comment;
    }

    /**
     * Gets score.
     * @return the result
     */
    public Integer getScore() {
        return score;
    }

    /**
     * Gets id.
     * @return the result
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets id.
     * @param id the id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Sets score.
     * @param score the score
     */
    public void setScore(Integer score) {
        this.score = score;
    }

    /**
     * Sets comment.
     * @param comment the comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Sets rated at.
     * @param ratedAt the rated at
     */
    public void setRatedAt(LocalDateTime ratedAt) {
        this.ratedAt = ratedAt;
    }

    /**
     * Sets buyer.
     * @param buyer the buyer
     */
    public void setBuyer(UserEntity buyer) {
        this.buyer = buyer;
    }

    /**
     * Sets seller.
     * @param seller the seller
     */
    public void setSeller(UserEntity seller) {
        this.seller = seller;
    }

    /**
     * Sets advertisement.
     * @param advertisement the advertisement
     */
    public void setAdvertisement(AdvertisementEntity advertisement) {
        this.advertisement = advertisement;
    }
}