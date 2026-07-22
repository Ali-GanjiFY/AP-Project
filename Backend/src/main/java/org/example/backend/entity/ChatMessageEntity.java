package org.example.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Represents chat message entity.
 */
@Entity
@Table(name = "chat_messages")
public class ChatMessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private LocalDateTime sentAt = LocalDateTime.now();

    @Column(nullable = false)
    private boolean seen = false;


    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private UserEntity sender;

    @ManyToOne
    @JoinColumn(name = "conversation_id", nullable = false)
    private ConversationEntity conversation;

    /**
     * Constructs a new ChatMessageEntity.
     */
    public ChatMessageEntity() {}

    /**
     * Constructs a new ChatMessageEntity.
     * @param content the content
     * @param sender the sender
     * @param conversation the conversation
     */
    public ChatMessageEntity(String content, UserEntity sender, ConversationEntity conversation) {
        this.content = content;
        this.sender = sender;
        this.conversation = conversation;
        this.sentAt = LocalDateTime.now();
    }

    /**
     * Gets id.
     * @return the result
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets content.
     * @return the result
     */
    public String getContent() {
        return content;
    }

    /**
     * Gets sent at.
     * @return the result
     */
    public LocalDateTime getSentAt() {
        return sentAt;
    }

    /**
     * Gets sender.
     * @return the result
     */
    public UserEntity getSender() {
        return sender;
    }

    /**
     * Gets conversation.
     * @return the result
     */
    public ConversationEntity getConversation() {
        return conversation;
    }

    /**
     * Checks whether seen.
     * @return the result
     */
    public boolean isSeen() {
        return seen;
    }

    /**
     * Sets id.
     * @param id the id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Sets content.
     * @param content the content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Sets sent at.
     * @param sentAt the sent at
     */
    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    /**
     * Sets sender.
     * @param sender the sender
     */
    public void setSender(UserEntity sender) {
        this.sender = sender;
    }

    /**
     * Sets conversation.
     * @param conversation the conversation
     */
    public void setConversation(ConversationEntity conversation) {
        this.conversation = conversation;
    }

    /**
     * Sets seen.
     * @param seen the seen
     */
    public void setSeen(boolean seen) {
        this.seen = seen;
    }
}