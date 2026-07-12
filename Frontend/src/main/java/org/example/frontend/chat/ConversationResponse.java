package org.example.frontend.chat;

public class ConversationResponse {

    private Long id;
    private Long advertisementId;
    private String advertisementTitle;
    private Long otherUserId;
    private String otherUserUsername;
    private String lastMessageAt;
    private String lastMessagePreview;

    public ConversationResponse() {
    }

    public Long getId() {
        return id;
    }

    public Long getAdvertisementId() {
        return advertisementId;
    }

    public String getAdvertisementTitle() {
        return advertisementTitle;
    }

    public Long getOtherUserId() {
        return otherUserId;
    }

    public String getOtherUserUsername() {
        return otherUserUsername;
    }

    public String getLastMessageAt() {
        return lastMessageAt;
    }

    public String getLastMessagePreview() {
        return lastMessagePreview;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAdvertisementId(Long advertisementId) {
        this.advertisementId = advertisementId;
    }

    public void setAdvertisementTitle(String advertisementTitle) {
        this.advertisementTitle = advertisementTitle;
    }

    public void setOtherUserId(Long otherUserId) {
        this.otherUserId = otherUserId;
    }

    public void setOtherUserUsername(String otherUserUsername) {
        this.otherUserUsername = otherUserUsername;
    }

    public void setLastMessageAt(String lastMessageAt) {
        this.lastMessageAt = lastMessageAt;
    }

    public void setLastMessagePreview(String lastMessagePreview) {
        this.lastMessagePreview = lastMessagePreview;
    }
}
