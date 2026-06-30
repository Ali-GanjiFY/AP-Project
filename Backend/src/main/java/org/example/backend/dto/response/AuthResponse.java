package org.example.backend.dto.response;

import org.example.backend.enums.Role;

public class AuthResponse {

    private final String token;
    private final Long userId;
    private final String username;
    private final Role role;

    public AuthResponse(String token, Long userId, String username, Role role) {
        this.token = token;
        this.userId = userId;
        this.username = username;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public Role getRole() {
        return role;
    }
}

