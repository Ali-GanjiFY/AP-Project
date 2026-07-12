package org.example.frontend.chat;

public class ChatMessageResponse {

    private Long id;
    private String content;
    private String sentAt;
    private boolean seen;
    private Long senderId;
    private String senderUsername;

    public ChatMessageResponse() {
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getSentAt() {
        return sentAt;
    }

    public boolean isSeen() {
        return seen;
    }

    public Long getSenderId() {
        return senderId;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setSentAt(String sentAt) {
        this.sentAt = sentAt;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }
}
