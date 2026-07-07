package org.example.backend.dto.response;

import org.example.backend.enums.RoleEnum;

public class AuthResponse {

    private final String token;
    private final Long userId;
    private final String username;
    private final RoleEnum role;

    public AuthResponse(String token, Long userId, String username, RoleEnum role) {
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

    public RoleEnum getRole() {
        return role;
    }
}

