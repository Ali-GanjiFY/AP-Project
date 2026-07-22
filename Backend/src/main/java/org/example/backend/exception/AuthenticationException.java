package org.example.backend.exception;

/**
 * Represents authentication exception.
 */
public class AuthenticationException extends RuntimeException {
    /**
     * Constructs a new AuthenticationException.
     * @param message the message
     */
    public AuthenticationException(String message) {
        super(message);
    }
}

