package org.example.frontend.chat;

public class ChatMessageResponse {

    private Long id;
    private String content;
    private String sentAt;
    private boolean seen;
    private Long senderId;
    private String senderUsername;

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
}

