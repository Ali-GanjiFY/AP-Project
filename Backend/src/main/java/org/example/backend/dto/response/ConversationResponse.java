package org.example.backend.dto.response;

import java.time.LocalDateTime;

public class ConversationResponse {

    private final Long id;
    private final Long advertisementId;
    private final String advertisementTitle;
    private final Long otherUserId;
    private final String otherUserUsername;
    private final LocalDateTime lastMessageAt;
    private final String lastMessagePreview;

    public ConversationResponse(Long id, Long advertisementId, String advertisementTitle,
                                Long otherUserId, String otherUserUsername,
                                LocalDateTime lastMessageAt, String lastMessagePreview) {
        this.id = id;
        this.advertisementId = advertisementId;
        this.advertisementTitle = advertisementTitle;
        this.otherUserId = otherUserId;
        this.otherUserUsername = otherUserUsername;
        this.lastMessageAt = lastMessageAt;
        this.lastMessagePreview = lastMessagePreview;
    }

    public Long getId() { return id; }
    public Long getAdvertisementId() { return advertisementId; }
    public String getAdvertisementTitle() { return advertisementTitle; }
    public Long getOtherUserId() { return otherUserId; }
    public String getOtherUserUsername() { return otherUserUsername; }
    public LocalDateTime getLastMessageAt() { return lastMessageAt; }
    public String getLastMessagePreview() { return lastMessagePreview; }
}
