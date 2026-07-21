package org.example.backend.exception;

/**
 * Represents unauthorized exception.
 */
public class UnauthorizedException extends RuntimeException{
    /**
     * Constructs a new UnauthorizedException.
     * @param message the message
     */
    public UnauthorizedException(String message) {
        super(message);
    }
}
