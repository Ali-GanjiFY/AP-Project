package org.example.frontend.chat;

public class ConversationResponse {
    private Long id;
    private Long advertisementId;
    private Long buyerId;
    private Long sellerId;

    // Getters (Setters هم اگر لازم باشد)
    public Long getId() {
        return id;
    }

    public Long getAdvertisementId() {
        return advertisementId;
    }

    public Long getBuyerId() {
        return buyerId;
    }

    public Long getSellerId() {
        return sellerId;
    }
}
