package org.example.backend.dto.response;

import org.example.backend.entity.UserEntity;
import org.example.backend.enums.Role;
import org.example.backend.enums.UserStatus;
import java.time.LocalDateTime;

public class UserResponse {

    private final Long id;
    private final String fullName;
    private final String username;
    private final String phone;
    private final String email;
    private final Role role;
    private final UserStatus status;
    private final LocalDateTime createdAt;

    public UserResponse(Long id, String fullName, String username, String phone,
                        String email, Role role, UserStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.fullName = fullName;
        this.username = username;
        this.phone = phone;
        this.email = email;
        this.role = role;
        this.status = status;
        this.createdAt = createdAt;
    }

    public static UserResponse fromEntity(UserEntity user) {
        return new UserResponse(
                user.getId(), user.getFullName(), user.getUsername(),
                user.getPhone(), user.getEmail(), user.getRole(),
                user.getStatus(), user.getCreatedAt()
        );
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUsername() {
        return username;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    public UserStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
