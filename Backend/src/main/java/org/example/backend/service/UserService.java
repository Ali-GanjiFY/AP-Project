package org.example.backend.service;

import org.example.backend.dto.request.ChangePasswordRequest;
import org.example.backend.dto.request.UpdateProfileRequest;
import org.example.backend.dto.response.UserResponse;
import org.example.backend.entity.UserEntity;
import org.example.backend.enums.UserStatusEnum;

import java.util.List;

/**
 * Contract for user service.
 */
public interface UserService {

    /**
     * Internal: get user entity by ID.
     * @param userId the user id
     * @return the result
     */
    UserEntity getUserEntityById(Long userId);

    /**
     * Internal: get user entity by username.
     * @param username the username
     * @return the result
     */
    UserEntity getUserEntityByUsername(String username);

    /**
     * Get user profile as DTO.
     * @param userId the user id
     * @return the result
     */
    UserResponse getUserById(Long userId);

    /**
     * Update profile (full name, email, phone).
     * @param userId the user id
     * @param request the request
     * @return the result
     */
    UserResponse updateProfile(Long userId, UpdateProfileRequest request);

    /**
     * Change password (requires old password check).
     * @param userId the user id
     * @param request the request
     */
    void changePassword(Long userId, ChangePasswordRequest request);

    /**
     * List all users.
     * @return the result
     */
    List<UserResponse> getAllUsers();

    /**
     * Filter users by status.
     * @param status the status
     * @return the result
     */
    List<UserResponse> getUsersByStatus(UserStatusEnum status);

    /**
     * Block user (admin only).
     * @param userId the user id
     * @return the result
     */
    UserResponse blockUser(Long userId);

    /**
     * Unblock user (admin only).
     * @param userId the user id
     * @return the result
     */
    UserResponse unblockUser(Long userId);

    /**
     * Soft delete (self or admin for others).
     * @param currentUserId the current user id
     * @param targetUserId the target user id
     */
    void deleteUser(Long currentUserId, Long targetUserId);
}