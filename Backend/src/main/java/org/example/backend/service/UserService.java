package org.example.backend.service;

import org.example.backend.dto.request.ChangePasswordRequest;
import org.example.backend.dto.request.UpdateProfileRequest;
import org.example.backend.dto.response.UserResponse;
import org.example.backend.entity.UserEntity;
import org.example.backend.enums.UserStatusEnum;

import java.util.List;

public interface UserService {

    // Internal: get user entity by ID
    UserEntity getUserEntityById(Long userId);

    // Internal: get user entity by username
    UserEntity getUserEntityByUsername(String username);

    // Get user profile as DTO
    UserResponse getUserById(Long userId);

    // Update profile (full name, email, phone)
    UserResponse updateProfile(Long userId, UpdateProfileRequest request);

    // Change password (requires old password check)
    void changePassword(Long userId, ChangePasswordRequest request);

    // List all users
    List<UserResponse> getAllUsers();

    // Filter users by status
    List<UserResponse> getUsersByStatus(UserStatusEnum status);

    // Block user (admin only)
    UserResponse blockUser(Long userId);

    // Unblock user (admin only)
    UserResponse unblockUser(Long userId);

    // Soft delete (self or admin for others)
    void deleteUser(Long currentUserId, Long targetUserId);
}