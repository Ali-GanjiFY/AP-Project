package org.example.frontend.chat;

public class ConversationResponse {

    private Long id;
    private Long advertisementId;
    private String advertisementTitle;
    private Long otherUserId;
    private String otherUserUsername;
    private String lastMessageAt;
    private String lastMessagePreview;

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
}
