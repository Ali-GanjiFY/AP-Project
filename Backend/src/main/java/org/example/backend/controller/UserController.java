package org.example.backend.controller;

import jakarta.validation.Valid;
import org.example.backend.dto.request.ChangePasswordRequest;
import org.example.backend.dto.request.UpdateProfileRequest;
import org.example.backend.dto.response.UserResponse;
import org.example.backend.enums.UserStatusEnum;
import org.example.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Extracts current user ID from JWT-backed Authentication
    private Long currentUserId(Authentication authentication) {
        return userService.getUserEntityByUsername(authentication.getName()).getId();
    }

    // GET /api/users/me -> self, view own profile
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMyProfile(Authentication authentication) {
        return ResponseEntity.ok(userService.getUserById(currentUserId(authentication)));
    }

    // PUT /api/users/me -> self, update own profile
    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateMyProfile(Authentication authentication,
                                                        @Valid @RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(userService.updateProfile(currentUserId(authentication), request));
    }

    // PUT /api/users/me/password -> self, change own password
    @PutMapping("/me/password")
    public ResponseEntity<Void> changeMyPassword(Authentication authentication,
                                                 @Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(currentUserId(authentication), request);
        return ResponseEntity.noContent().build();
    }

    // DELETE /api/users/me -> self, soft-delete own account
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMyAccount(Authentication authentication) {
        Long userId = currentUserId(authentication);
        userService.deleteUser(userId, userId);
        return ResponseEntity.noContent().build();
    }

    // GET /api/users -> admin only, list all users (optionally filter by status)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserResponse>> getUsers(@RequestParam(required = false) UserStatusEnum status) {
        if (status != null) {
            return ResponseEntity.ok(userService.getUsersByStatus(status));
        }
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // GET /api/users/{id} -> admin only, view any user's profile
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    // PUT /api/users/{id}/block -> admin only
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/block")
    public ResponseEntity<UserResponse> blockUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.blockUser(id));
    }

    // PUT /api/users/{id}/unblock -> admin only
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/unblock")
    public ResponseEntity<UserResponse> unblockUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.unblockUser(id));
    }

    // DELETE /api/users/{id} -> admin only, delete another user's account.
    // Admin cannot delete another admin
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(Authentication authentication, @PathVariable Long id) {
        userService.deleteUser(currentUserId(authentication), id);
        return ResponseEntity.noContent().build();
    }
}