package org.example.backend.dto.request;

import jakarta.validation.constraints.NotBlank;

public class SendMessageRequest {

    @NotBlank(message = "Message content cannot be empty")
    private String content;

    // Constructor
    public SendMessageRequest() {}

    public SendMessageRequest(String content) {
        this.content = content;
    }

    // Getters and Setters
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
