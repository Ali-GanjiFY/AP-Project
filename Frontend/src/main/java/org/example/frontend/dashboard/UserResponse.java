package org.example.frontend.dashboard;

/**
 * Represents user response.
 */
public class UserResponse {
    private Long id;
    private String fullName;
    private String username;
    private String phone;
    private String email;
    private String role;
    private String status; // ACTIVE, BLOCKED, DELETED

    /**
     * Gets id.
     * @return the result
     */
    public Long getId() { return id; }
    /**
     * Sets id.
     * @param id the id
     */
    public void setId(Long id) { this.id = id; }

    /**
     * Gets full name.
     * @return the result
     */
    public String getFullName() { return fullName; }
    /**
     * Sets full name.
     * @param fullName the full name
     */
    public void setFullName(String fullName) { this.fullName = fullName; }

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
     * Gets phone.
     * @return the result
     */
    public String getPhone() { return phone; }
    /**
     * Sets phone.
     * @param phone the phone
     */
    public void setPhone(String phone) { this.phone = phone; }

    /**
     * Gets email.
     * @return the result
     */
    public String getEmail() { return email; }
    /**
     * Sets email.
     * @param email the email
     */
    public void setEmail(String email) { this.email = email; }

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
     * Gets status.
     * @return the result
     */
    public String getStatus() { return status; }
    /**
     * Sets status.
     * @param status the status
     */
    public void setStatus(String status) { this.status = status; }
}

