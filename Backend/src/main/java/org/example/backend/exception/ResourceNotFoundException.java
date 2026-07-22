package org.example.backend.exception;

/**
 * Represents resource not found exception.
 */
public class ResourceNotFoundException extends RuntimeException {
    /**
     * Constructs a new ResourceNotFoundException.
     * @param message the message
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

