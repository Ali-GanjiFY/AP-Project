package org.example.backend.dto.response;

/**
 * Represents error response.
 */
public class ErrorResponse {

    private final String message;
    private final int status;

    /**
     * Constructs a new ErrorResponse.
     * @param message the message
     * @param status the status
     */
    public ErrorResponse(String message, int status) {
        this.message = message;
        this.status = status;
    }

    /**
     * Gets message.
     * @return the result
     */
    public String getMessage() { return message; }
    /**
     * Gets status.
     * @return the result
     */
    public int getStatus() { return status; }
}