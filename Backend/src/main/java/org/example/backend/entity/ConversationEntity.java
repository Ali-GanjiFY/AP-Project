package org.example.backend.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents conversation entity.
 */
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

    /**
     * Constructs a new ConversationEntity.
     */
    public ConversationEntity() {}

    /**
     * Constructs a new ConversationEntity.
     * @param buyer the buyer
     * @param seller the seller
     * @param advertisement the advertisement
     */
    public ConversationEntity(UserEntity buyer, UserEntity seller, AdvertisementEntity advertisement) {
        this.buyer = buyer;
        this.seller = seller;
        this.advertisement = advertisement;
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Gets id.
     * @return the result
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets created at.
     * @return the result
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Gets buyer.
     * @return the result
     */
    public UserEntity getBuyer() {
        return buyer;
    }

    /**
     * Gets seller.
     * @return the result
     */
    public UserEntity getSeller() {
        return seller;
    }

    /**
     * Gets advertisement.
     * @return the result
     */
    public AdvertisementEntity getAdvertisement() {
        return advertisement;
    }

    /**
     * Gets messages.
     * @return the result
     */
    public List<ChatMessageEntity> getMessages() {
        return messages;
    }

    /**
     * Gets last message at.
     * @return the result
     */
    public LocalDateTime getLastMessageAt() {
        return lastMessageAt;
    }

    /**
     * Sets id.
     * @param id the id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Sets created at.
     * @param createdAt the created at
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
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

    /**
     * Sets messages.
     * @param messages the messages
     */
    public void setMessages(List<ChatMessageEntity> messages) {
        this.messages = messages;
    }

    /**
     * Sets last message at.
     * @param lastMessageAt the last message at
     */
    public void setLastMessageAt(LocalDateTime lastMessageAt) {
        this.lastMessageAt = lastMessageAt;
    }
}