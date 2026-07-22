package org.example.backend.exception;

/**
 * Represents duplicate resource exception.
 */
public class DuplicateResourceException extends RuntimeException {
    /**
     * Constructs a new DuplicateResourceException.
     * @param message the message
     */
    public  DuplicateResourceException(String message) {
        super(message);
    }
}
