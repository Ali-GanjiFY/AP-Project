package org.example.backend.dto.response;

import org.example.backend.enums.RoleEnum;

/**
 * Represents auth response.
 */
public class AuthResponse {

    private final String token;
    private final Long userId;
    private final String username;
    private final RoleEnum role;

    /**
     * Constructs a new AuthResponse.
     * @param token the token
     * @param userId the user id
     * @param username the username
     * @param role the role
     */
    public AuthResponse(String token, Long userId, String username, RoleEnum role) {
        this.token = token;
        this.userId = userId;
        this.username = username;
        this.role = role;
    }

    /**
     * Gets token.
     * @return the result
     */
    public String getToken() {
        return token;
    }

    /**
     * Gets user id.
     * @return the result
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Gets username.
     * @return the result
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets role.
     * @return the result
     */
    public RoleEnum getRole() {
        return role;
    }
}

