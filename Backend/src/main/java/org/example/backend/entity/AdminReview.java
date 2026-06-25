package org.example.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "admin_reviews")
public class AdminReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String note;  // توضیح مدیر برای رد کردن آگهی

    @Column(nullable = false)
    private LocalDateTime reviewedAt = LocalDateTime.now();


    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private User admin;

    // آگهی بررسی شده
    @OneToOne
    @JoinColumn(name = "advertisement_id", nullable = false)
    private Advertisement advertisement;

    // Constructors
    public AdminReview() {}

    public AdminReview(String note, User admin, Advertisement advertisement) {
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

    public User getAdmin() { return admin; }
    public void setAdmin(User admin) { this.admin = admin; }

    public Advertisement getAdvertisement() { return advertisement; }
    public void setAdvertisement(Advertisement advertisement) { this.advertisement = advertisement; }
}

