package org.example.backend.dto.response;

import java.time.LocalDateTime;
/**
 * Represents chat message response.
 */
public class ChatMessageResponse {

    private final Long id;
    private final String content;
    private final LocalDateTime sentAt;
    private final boolean seen;
    private final Long senderId;
    private final String senderUsername;

    /**
     * Constructs a new ChatMessageResponse.
     * @param id the id
     * @param content the content
     * @param sentAt the sent at
     * @param seen the seen
     * @param senderId the sender id
     * @param senderUsername the sender username
     */
    public ChatMessageResponse(Long id, String content, LocalDateTime sentAt,
                               boolean seen, Long senderId, String senderUsername) {
        this.id = id;
        this.content = content;
        this.sentAt = sentAt;
        this.seen = seen;
        this.senderId = senderId;
        this.senderUsername = senderUsername;
    }

    /**
     * Gets id.
     * @return the result
     */
    public Long getId() { return id; }
    /**
     * Gets content.
     * @return the result
     */
    public String getContent() { return content; }
    /**
     * Gets sent at.
     * @return the result
     */
    public LocalDateTime getSentAt() { return sentAt; }
    /**
     * Checks whether seen.
     * @return the result
     */
    public boolean isSeen() { return seen; }
    /**
     * Gets sender id.
     * @return the result
     */
    public Long getSenderId() { return senderId; }
    /**
     * Gets sender username.
     * @return the result
     */
    public String getSenderUsername() { return senderUsername; }
}
