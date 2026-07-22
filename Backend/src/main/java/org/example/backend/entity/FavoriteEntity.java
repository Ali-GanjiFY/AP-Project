package org.example.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "favorites",
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "advertisement_id"}))
/**
 * Represents favorite entity.
 */
public class FavoriteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime savedAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "advertisement_id", nullable = false)
    private AdvertisementEntity advertisement;

    /**
     * Constructs a new FavoriteEntity.
     */
    public FavoriteEntity() {}

    /**
     * Constructs a new FavoriteEntity.
     * @param user the user
     * @param advertisement the advertisement
     */
    public FavoriteEntity(UserEntity user, AdvertisementEntity advertisement) {
        this.user = user;
        this.advertisement = advertisement;
        this.savedAt = LocalDateTime.now();
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
     * Gets saved at.
     * @return the result
     */
    public LocalDateTime getSavedAt() { return savedAt; }
    /**
     * Sets saved at.
     * @param savedAt the saved at
     */
    public void setSavedAt(LocalDateTime savedAt) { this.savedAt = savedAt; }

    /**
     * Gets user.
     * @return the result
     */
    public UserEntity getUser() { return user; }
    /**
     * Sets user.
     * @param user the user
     */
    public void setUser(UserEntity user) { this.user = user; }

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