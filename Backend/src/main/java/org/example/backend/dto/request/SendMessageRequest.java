package org.example.backend.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * Represents send message request.
 */
public class SendMessageRequest {

    @NotBlank(message = "Message content cannot be empty")
    private String content;

    /**
     * Constructs a new SendMessageRequest.
     */
    public SendMessageRequest() {}

    /**
     * Constructs a new SendMessageRequest.
     * @param content the content
     */
    public SendMessageRequest(String content) {
        this.content = content;
    }

    /**
     * Gets content.
     * @return the result
     */
    public String getContent() { return content; }
    /**
     * Sets content.
     * @param content the content
     */
    public void setContent(String content) { this.content = content; }
}
