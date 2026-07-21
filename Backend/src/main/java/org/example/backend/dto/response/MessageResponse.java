package org.example.backend.dto.response;

/**
 * Represents message response.
 */
public class MessageResponse {

    private final String message;

    /**
     * Constructs a new MessageResponse.
     * @param message the message
     */
    public MessageResponse(String message) {
        this.message = message;
    }

    /**
     * Gets message.
     * @return the result
     */
    public String getMessage() {
        return message;
    }
}
