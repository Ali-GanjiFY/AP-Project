package org.example.backend.exception;

/**
 * Represents invalid input exception.
 */
public class InvalidInputException extends RuntimeException{
    /**
     * Constructs a new InvalidInputException.
     * @param message the message
     */
    public InvalidInputException(String message) {
        super(message);
    }
}
