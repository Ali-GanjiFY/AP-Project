package org.example.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

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

    // Constructors
    public ChatMessageEntity() {}

    public ChatMessageEntity(String content, UserEntity sender, ConversationEntity conversation) {
        this.content = content;
        this.sender = sender;
        this.conversation = conversation;
        this.sentAt = LocalDateTime.now();
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public UserEntity getSender() {
        return sender;
    }

    public ConversationEntity getConversation() {
        return conversation;
    }

    public boolean isSeen() {
        return seen;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public void setSender(UserEntity sender) {
        this.sender = sender;
    }

    public void setConversation(ConversationEntity conversation) {
        this.conversation = conversation;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }
}