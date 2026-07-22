package org.example.frontend.chat;

/**
 * Represents conversation response.
 */
public class ConversationResponse {

    private Long id;
    private Long advertisementId;
    private String advertisementTitle;
    private Long otherUserId;
    private String otherUserUsername;
    private String lastMessageAt;
    private String lastMessagePreview;

    /**
     * Constructs a new ConversationResponse.
     */
    public ConversationResponse() {
    }

    /**
     * Gets id.
     * @return the result
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets advertisement id.
     * @return the result
     */
    public Long getAdvertisementId() {
        return advertisementId;
    }

    /**
     * Gets advertisement title.
     * @return the result
     */
    public String getAdvertisementTitle() {
        return advertisementTitle;
    }

    /**
     * Gets other user id.
     * @return the result
     */
    public Long getOtherUserId() {
        return otherUserId;
    }

    /**
     * Gets other user username.
     * @return the result
     */
    public String getOtherUserUsername() {
        return otherUserUsername;
    }

    /**
     * Gets last message at.
     * @return the result
     */
    public String getLastMessageAt() {
        return lastMessageAt;
    }

    /**
     * Gets last message preview.
     * @return the result
     */
    public String getLastMessagePreview() {
        return lastMessagePreview;
    }

    /**
     * Sets id.
     * @param id the id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Sets advertisement id.
     * @param advertisementId the advertisement id
     */
    public void setAdvertisementId(Long advertisementId) {
        this.advertisementId = advertisementId;
    }

    /**
     * Sets advertisement title.
     * @param advertisementTitle the advertisement title
     */
    public void setAdvertisementTitle(String advertisementTitle) {
        this.advertisementTitle = advertisementTitle;
    }

    /**
     * Sets other user id.
     * @param otherUserId the other user id
     */
    public void setOtherUserId(Long otherUserId) {
        this.otherUserId = otherUserId;
    }

    /**
     * Sets other user username.
     * @param otherUserUsername the other user username
     */
    public void setOtherUserUsername(String otherUserUsername) {
        this.otherUserUsername = otherUserUsername;
    }

    /**
     * Sets last message at.
     * @param lastMessageAt the last message at
     */
    public void setLastMessageAt(String lastMessageAt) {
        this.lastMessageAt = lastMessageAt;
    }

    /**
     * Sets last message preview.
     * @param lastMessagePreview the last message preview
     */
    public void setLastMessagePreview(String lastMessagePreview) {
        this.lastMessagePreview = lastMessagePreview;
    }
}
