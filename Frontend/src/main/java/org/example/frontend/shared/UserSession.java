package org.example.frontend.shared;

/**
 * Represents user session.
 */
public class UserSession {
    private static UserSession instance;

    private String token;
    private Long userId;
    private String username;
    private String role;

    /**
     * Constructs a new UserSession.
     */
    private UserSession() {}

    /**
     * Gets instance.
     * @return the result
     */
    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    /**
     * Gets token.
     * @return the result
     */
    public String getToken() { return token; }
    /**
     * Sets token.
     * @param token the token
     */
    public void setToken(String token) { this.token = token; }

    /**
     * Gets user id.
     * @return the result
     */
    public Long getUserId() { return userId; }
    /**
     * Sets user id.
     * @param userId the user id
     */
    public void setUserId(Long userId) { this.userId = userId; }

    /**
     * Gets username.
     * @return the result
     */
    public String getUsername() { return username; }
    /**
     * Sets username.
     * @param username the username
     */
    public void setUsername(String username) { this.username = username; }

    /**
     * Gets role.
     * @return the result
     */
    public String getRole() { return role; }
    /**
     * Sets role.
     * @param role the role
     */
    public void setRole(String role) { this.role = role; }

    /**
     * Clean session.
     */
    public void cleanSession() {
        this.token = null;
        this.userId = null;
        this.username = null;
        this.role = null;
    }
}

