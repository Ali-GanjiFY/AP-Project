package org.example.backend.entity;

import jakarta.persistence.*;
import org.example.backend.enums.ReviewDecisionEnum;

import java.time.LocalDateTime;

/**
 * Represents admin review entity.
 */
@Entity
@Table(name = "admin_reviews")
public class AdminReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String note;

    @Column(nullable = false)
    private LocalDateTime reviewedAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReviewDecisionEnum decision;

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private UserEntity admin;

    @OneToOne
    @JoinColumn(name = "advertisement_id", nullable = false)
    private AdvertisementEntity advertisement;


    /**
     * Constructs a new AdminReviewEntity.
     */
    public AdminReviewEntity() {}

    /**
     * Constructs a new AdminReviewEntity.
     * @param note the note
     * @param admin the admin
     * @param advertisement the advertisement
     */
    public AdminReviewEntity(String note, UserEntity admin, AdvertisementEntity advertisement) {
        this.note = note;
        this.admin = admin;
        this.advertisement = advertisement;
        this.reviewedAt = LocalDateTime.now();
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
     * Gets note.
     * @return the result
     */
    public String getNote() { return note; }
    /**
     * Sets note.
     * @param note the note
     */
    public void setNote(String note) { this.note = note; }

    /**
     * Gets reviewed at.
     * @return the result
     */
    public LocalDateTime getReviewedAt() { return reviewedAt; }
    /**
     * Sets reviewed at.
     * @param reviewedAt the reviewed at
     */
    public void setReviewedAt(LocalDateTime reviewedAt) { this.reviewedAt = reviewedAt; }

    /**
     * Gets admin.
     * @return the result
     */
    public UserEntity getAdmin() { return admin; }
    /**
     * Sets admin.
     * @param admin the admin
     */
    public void setAdmin(UserEntity admin) { this.admin = admin; }

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

    /**
     * Gets decision.
     * @return the result
     */
    public ReviewDecisionEnum getDecision() {
        return decision;
    }
    /**
     * Sets decision.
     * @param decision the decision
     */
    public void setDecision(ReviewDecisionEnum decision) {
        this.decision = decision;
    }
}