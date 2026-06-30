package org.example.backend.dto.response;

import java.time.LocalDateTime;
public class ChatMessageResponse {

    private final Long id;
    private final String content;
    private final LocalDateTime sentAt;
    private final boolean seen;
    private final Long senderId;
    private final String senderUsername;

    public ChatMessageResponse(Long id, String content, LocalDateTime sentAt,
                               boolean seen, Long senderId, String senderUsername) {
        this.id = id;
        this.content = content;
        this.sentAt = sentAt;
        this.seen = seen;
        this.senderId = senderId;
        this.senderUsername = senderUsername;
    }

    public Long getId() { return id; }
    public String getContent() { return content; }
    public LocalDateTime getSentAt() { return sentAt; }
    public boolean isSeen() { return seen; }
    public Long getSenderId() { return senderId; }
    public String getSenderUsername() { return senderUsername; }
}
