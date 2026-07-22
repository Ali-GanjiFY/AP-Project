package org.example.backend.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * Represents login request.
 */
public class LoginRequest {

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    /**
     * Constructs a new LoginRequest.
     */
    public LoginRequest() {
    }

    /**
     * Constructs a new LoginRequest.
     * @param username the username
     * @param password the password
     */
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Gets username.
     * @return the result
     */
    public String getUsername() {
        return username;
    }
    /**
     * Sets username.
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets password.
     * @return the result
     */
    public String getPassword() {
        return password;
    }
    /**
     * Sets password.
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
