package org.example.frontend.chat;

/**
 * Represents chat message response.
 */
public class ChatMessageResponse {

    private Long id;
    private String content;
    private String sentAt;
    private boolean seen;
    private Long senderId;
    private String senderUsername;

    /**
     * Constructs a new ChatMessageResponse.
     */
    public ChatMessageResponse() {
    }

    /**
     * Gets id.
     * @return the result
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets content.
     * @return the result
     */
    public String getContent() {
        return content;
    }

    /**
     * Gets sent at.
     * @return the result
     */
    public String getSentAt() {
        return sentAt;
    }

    /**
     * Checks whether seen.
     * @return the result
     */
    public boolean isSeen() {
        return seen;
    }

    /**
     * Gets sender id.
     * @return the result
     */
    public Long getSenderId() {
        return senderId;
    }

    /**
     * Gets sender username.
     * @return the result
     */
    public String getSenderUsername() {
        return senderUsername;
    }

    /**
     * Sets id.
     * @param id the id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Sets content.
     * @param content the content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Sets sent at.
     * @param sentAt the sent at
     */
    public void setSentAt(String sentAt) {
        this.sentAt = sentAt;
    }

    /**
     * Sets seen.
     * @param seen the seen
     */
    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    /**
     * Sets sender id.
     * @param senderId the sender id
     */
    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    /**
     * Sets sender username.
     * @param senderUsername the sender username
     */
    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }
}
