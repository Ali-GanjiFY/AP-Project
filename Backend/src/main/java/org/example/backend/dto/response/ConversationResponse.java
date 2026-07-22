package org.example.backend.dto.response;

import java.time.LocalDateTime;

/**
 * Represents conversation response.
 */
public class ConversationResponse {

    private final Long id;
    private final Long advertisementId;
    private final String advertisementTitle;
    private final Long otherUserId;
    private final String otherUserUsername;
    private final LocalDateTime lastMessageAt;
    private final String lastMessagePreview;

    /**
     * Constructs a new ConversationResponse.
     * @param id the id
     * @param advertisementId the advertisement id
     * @param advertisementTitle the advertisement title
     * @param otherUserId the other user id
     * @param otherUserUsername the other user username
     * @param lastMessageAt the last message at
     * @param lastMessagePreview the last message preview
     */
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

    /**
     * Gets id.
     * @return the result
     */
    public Long getId() { return id; }
    /**
     * Gets advertisement id.
     * @return the result
     */
    public Long getAdvertisementId() { return advertisementId; }
    /**
     * Gets advertisement title.
     * @return the result
     */
    public String getAdvertisementTitle() { return advertisementTitle; }
    /**
     * Gets other user id.
     * @return the result
     */
    public Long getOtherUserId() { return otherUserId; }
    /**
     * Gets other user username.
     * @return the result
     */
    public String getOtherUserUsername() { return otherUserUsername; }
    /**
     * Gets last message at.
     * @return the result
     */
    public LocalDateTime getLastMessageAt() { return lastMessageAt; }
    /**
     * Gets last message preview.
     * @return the result
     */
    public String getLastMessagePreview() { return lastMessagePreview; }
}
