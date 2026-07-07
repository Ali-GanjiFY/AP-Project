package org.example.backend.service;

import org.example.backend.dto.request.ChangePasswordRequest;
import org.example.backend.dto.request.UpdateProfileRequest;
import org.example.backend.dto.response.UserResponse;
import org.example.backend.entity.UserEntity;
import org.example.backend.enums.UserStatusEnum;

import java.util.List;

public interface UserService {

    // Get user entity by ID (internal use by other services)
    UserEntity getUserEntityById(Long userId);

    // Get user entity by username (internal use by other services)
    UserEntity getUserEntityByUsername(String username);

    // Get user profile as DTO by ID
    UserResponse getUserById(Long userId);

    // Update user profile (full name, email, phone) with uniqueness validation
    UserResponse updateProfile(Long userId, UpdateProfileRequest request);

    // Change user password with old password verification
    void changePassword(Long userId, ChangePasswordRequest request);

    // Get list of all users
    List<UserResponse> getAllUsers();

    // Get list of users filtered by status (ACTIVE, BLOCKED, DELETED)
    List<UserResponse> getUsersByStatus(UserStatusEnum status);

    // Block a user (admin only, cannot block other admins)
    UserResponse blockUser(Long userId);

    // Unblock a user (admin only, cannot unblock deleted users)
    UserResponse unblockUser(Long userId);

    // Soft delete a user: user can delete self, admin can delete others
    void deleteUser(Long currentUserId, Long targetUserId);
}
