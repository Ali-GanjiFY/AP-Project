package org.example.frontend.shared;

public class UserSession {
    private static UserSession instance;

    private String token;
    private Long userId;
    private String username;
    private String role;

    private UserSession() {}

    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void cleanUserSession() {
        this.token = null;
        this.userId = null;
        this.username = null;
        this.role = null;
    }

    // Getters and Setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
