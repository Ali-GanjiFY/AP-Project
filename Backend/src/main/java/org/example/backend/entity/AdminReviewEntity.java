package org.example.backend.entity;

import jakarta.persistence.*;
import org.example.backend.enums.ReviewDecisionEnum;

import java.time.LocalDateTime;

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


    // Constructors
    public AdminReviewEntity() {}

    public AdminReviewEntity(String note, UserEntity admin, AdvertisementEntity advertisement) {
        this.note = note;
        this.admin = admin;
        this.advertisement = advertisement;
        this.reviewedAt = LocalDateTime.now();
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public LocalDateTime getReviewedAt() { return reviewedAt; }
    public void setReviewedAt(LocalDateTime reviewedAt) { this.reviewedAt = reviewedAt; }

    public UserEntity getAdmin() { return admin; }
    public void setAdmin(UserEntity admin) { this.admin = admin; }

    public AdvertisementEntity getAdvertisement() { return advertisement; }
    public void setAdvertisement(AdvertisementEntity advertisement) { this.advertisement = advertisement; }

    public ReviewDecisionEnum getDecision() {
        return decision;
    }
    public void setDecision(ReviewDecisionEnum decision) {
        this.decision = decision;
    }
}