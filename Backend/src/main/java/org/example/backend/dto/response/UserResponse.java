package org.example.backend.dto.response;

import org.example.backend.entity.UserEntity;
import org.example.backend.enums.RoleEnum;
import org.example.backend.enums.UserStatusEnum;
import java.time.LocalDateTime;

/**
 * Represents user response.
 */
public class UserResponse {

    private final Long id;
    private final String fullName;
    private final String username;
    private final String phone;
    private final String email;
    private final RoleEnum role;
    private final UserStatusEnum status;
    private final LocalDateTime createdAt;

    /**
     * Constructs a new UserResponse.
     * @param id the id
     * @param fullName the full name
     * @param username the username
     * @param phone the phone
     * @param email the email
     * @param role the role
     * @param status the status
     * @param createdAt the created at
     */
    public UserResponse(Long id, String fullName, String username, String phone,
                        String email, RoleEnum role, UserStatusEnum status, LocalDateTime createdAt) {
        this.id = id;
        this.fullName = fullName;
        this.username = username;
        this.phone = phone;
        this.email = email;
        this.role = role;
        this.status = status;
        this.createdAt = createdAt;
    }

    /**
     * From entity.
     * @param user the user
     * @return the result
     */
    public static UserResponse fromEntity(UserEntity user) {
        return new UserResponse(
                user.getId(), user.getFullName(), user.getUsername(),
                user.getPhone(), user.getEmail(), user.getRole(),
                user.getStatus(), user.getCreatedAt()
        );
    }

    /**
     * Gets id.
     * @return the result
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets full name.
     * @return the result
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Gets username.
     * @return the result
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets phone.
     * @return the result
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Gets email.
     * @return the result
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets role.
     * @return the result
     */
    public RoleEnum getRole() {
        return role;
    }

    /**
     * Gets status.
     * @return the result
     */
    public UserStatusEnum getStatus() {
        return status;
    }

    /**
     * Gets created at.
     * @return the result
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
