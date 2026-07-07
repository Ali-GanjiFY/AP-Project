package org.example.backend.service.impl;

import org.example.backend.dto.request.ChangePasswordRequest;
import org.example.backend.dto.request.UpdateProfileRequest;
import org.example.backend.dto.response.UserResponse;
import org.example.backend.entity.UserEntity;
import org.example.backend.enums.Role;
import org.example.backend.enums.UserStatus;
import org.example.backend.exception.DuplicateResourceException;
import org.example.backend.exception.InvalidInputException;
import org.example.backend.exception.ResourceNotFoundException;
import org.example.backend.exception.UnauthorizedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.example.backend.repository.UserRepository;
import org.example.backend.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Prevent operations on soft-deleted users
    private void validateNotDeleted(UserEntity user) {
        if (user.getStatus() == UserStatus.DELETED) {
            throw new InvalidInputException("این کاربر قبلاً حذف شده است");
        }
    }

    // Get user entity by ID (internal use)
    @Override
    public UserEntity getUserEntityById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("کاربر یافت نشد"));
    }

    // Get user entity by username (internal use)
    @Override
    public UserEntity getUserEntityByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("کاربر یافت نشد"));
    }

    // Get user as DTO by ID
    @Override
    public UserResponse getUserById(Long userId) {
        return UserResponse.fromEntity(getUserEntityById(userId));
    }

    // Update user profile: full name, email, phone with uniqueness checks
    @Override
    @Transactional
    public UserResponse updateProfile(Long userId, UpdateProfileRequest request) {
        UserEntity user = getUserEntityById(userId);

        // Update full name if provided
        if (request.getFullName() != null && !request.getFullName().trim().isEmpty()) {
            user.setFullName(request.getFullName().trim());
        }

        // Update email with uniqueness validation (excluding current user)
        if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
            String newEmail = request.getEmail().trim();

            boolean emailTaken = userRepository.findByEmail(newEmail)
                    .filter(existing -> !existing.getId().equals(user.getId()))
                    .isPresent();

            if (emailTaken) {
                throw new DuplicateResourceException("این ایمیل قبلاً ثبت شده است");
            }
            user.setEmail(newEmail);
        }

        // Update phone with uniqueness validation (excluding current user)
        if (request.getPhone() != null && !request.getPhone().trim().isEmpty()) {
            String newPhone = request.getPhone().trim();

            boolean phoneTaken = userRepository.findByPhone(newPhone)
                    .filter(existing -> !existing.getId().equals(user.getId()))
                    .isPresent();

            if (phoneTaken) {
                throw new DuplicateResourceException("این شماره تماس قبلاً ثبت شده است");
            }
            user.setPhone(newPhone);
        }

        UserEntity updatedUser = userRepository.save(user);
        return UserResponse.fromEntity(updatedUser);
    }

    // Change password: verify old password, ensure new password is different
    @Override
    @Transactional
    public void changePassword(Long userId, ChangePasswordRequest request) {
        UserEntity user = getUserEntityById(userId);

        // Check current password
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new InvalidInputException("رمز عبور فعلی اشتباه است");
        }

        // Prevent using the same password
        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new InvalidInputException("رمز عبور جدید باید با رمز عبور فعلی متفاوت باشد");
        }

        // Hash and save new password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    // Get all users (including deleted ones)
    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserResponse::fromEntity)
                .toList();
    }

    // Get users filtered by status
    @Override
    public List<UserResponse> getUsersByStatus(UserStatus status) {
        return userRepository.findByStatus(status).stream()
                .map(UserResponse::fromEntity)
                .toList();
    }

    // Block user: admin only, cannot block other admins
    @Override
    @Transactional
    public UserResponse blockUser(Long userId) {
        UserEntity user = getUserEntityById(userId);

        if (user.getRole() == Role.ADMIN) {
            throw new UnauthorizedException("شما نمی‌توانید یک ادمین را مسدود کنید");
        }

        user.setStatus(UserStatus.BLOCKED);
        return UserResponse.fromEntity(userRepository.save(user));
    }

    // Unblock user: cannot unblock deleted accounts
    @Override
    @Transactional
    public UserResponse unblockUser(Long userId) {
        UserEntity user = getUserEntityById(userId);
        if (user.getStatus() == UserStatus.DELETED) {
            throw new InvalidInputException("حساب کاربری حذف شده قابل فعال‌سازی مجدد نیست");
        }
        user.setStatus(UserStatus.ACTIVE);
        return UserResponse.fromEntity(userRepository.save(user));
    }

    // Soft delete user: user can delete self, admin can delete others (except other admins)
    @Override
    @Transactional
    public void deleteUser(Long currentUserId, Long targetUserId) {
        UserEntity currentUser = getUserEntityById(currentUserId);
        UserEntity targetUser = getUserEntityById(targetUserId);

        validateNotDeleted(currentUser);
        validateNotDeleted(targetUser);

        boolean isAdmin = currentUser.getRole() == Role.ADMIN;
        boolean isSelf = currentUserId.equals(targetUserId);

        // Authorization logic
        if (isAdmin) {
            // Admin cannot delete other admins (only themselves)
            if (!isSelf && targetUser.getRole() == Role.ADMIN) {
                throw new UnauthorizedException("ادمین نمی‌تواند ادمین دیگر را حذف کند");
            }
        } else {
            // Regular users can only delete their own account
            if (!isSelf) {
                throw new UnauthorizedException("شما فقط می‌توانید حساب خودتان را حذف کنید");
            }
        }

        targetUser.setStatus(UserStatus.DELETED);
        userRepository.save(targetUser);
    }
}