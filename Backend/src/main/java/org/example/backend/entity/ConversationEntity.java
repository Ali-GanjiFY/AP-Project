package org.example.backend.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "conversations")
public class ConversationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column
    private LocalDateTime lastMessageAt;

    @ManyToOne
    @JoinColumn(name = "buyer_id", nullable = false)
    private UserEntity buyer;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private UserEntity seller;

    @ManyToOne
    @JoinColumn(name = "advertisement_id", nullable = false)
    private AdvertisementEntity advertisement;

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL)
    @OrderBy("sentAt ASC")
    private List<ChatMessageEntity> messages;

    // Constructors
    public ConversationEntity() {}

    public ConversationEntity(UserEntity buyer, UserEntity seller, AdvertisementEntity advertisement) {
        this.buyer = buyer;
        this.seller = seller;
        this.advertisement = advertisement;
        this.createdAt = LocalDateTime.now();
    }

    // Getters
    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public UserEntity getBuyer() {
        return buyer;
    }

    public UserEntity getSeller() {
        return seller;
    }

    public AdvertisementEntity getAdvertisement() {
        return advertisement;
    }

    public List<ChatMessageEntity> getMessages() {
        return messages;
    }

    public LocalDateTime getLastMessageAt() {
        return lastMessageAt;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
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

    public void setMessages(List<ChatMessageEntity> messages) {
        this.messages = messages;
    }

    public void setLastMessageAt(LocalDateTime lastMessageAt) {
        this.lastMessageAt = lastMessageAt;
    }
}