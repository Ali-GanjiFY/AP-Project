package org.example.backend.service.impl;

import org.example.backend.entity.UserEntity;
import org.example.backend.enums.RoleEnum;
import org.example.backend.enums.UserStatusEnum;
import org.example.backend.exception.InvalidInputException;
import org.example.backend.exception.ResourceNotFoundException;
import org.example.backend.exception.UnauthorizedException;
import org.example.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Provides unit tests for {@link UserServiceImpl}.
 *
 * <p>These tests verify user retrieval, account blocking, account unblocking,
 * authorization rules, and soft-deletion behavior without loading the Spring
 * application context.</p>
 */
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    /**
     * Mocked repository used to isolate the service from the database.
     */
    @Mock
    private UserRepository userRepository;

    /**
     * Mocked password encoder required by the user service.
     */
    @Mock
    private PasswordEncoder passwordEncoder;

    /**
     * Service instance under test with mocked dependencies injected by Mockito.
     */
    @InjectMocks
    private UserServiceImpl userService;

    /**
     * Verifies that an existing user is returned when a valid identifier is
     * provided.
     */
    @Test
    void getUserEntityById_WhenUserExists_ShouldReturnUser() {
        UserEntity user = createUser(
                1L,
                RoleEnum.NORMAL_USER,
                UserStatusEnum.ACTIVE
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserEntity result = userService.getUserEntityById(1L);

        assertSame(user, result);
        verify(userRepository).findById(1L);
    }

    /**
     * Verifies that a resource-not-found exception is thrown when the requested
     * user does not exist.
     */
    @Test
    void getUserEntityById_WhenUserDoesNotExist_ShouldThrowException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> userService.getUserEntityById(99L)
        );

        verify(userRepository).findById(99L);
    }

    /**
     * Verifies that an administrator account cannot be blocked.
     */
    @Test
    void blockUser_WhenTargetUserIsAdmin_ShouldThrowUnauthorizedException() {
        UserEntity admin = createUser(
                1L,
                RoleEnum.ADMIN,
                UserStatusEnum.ACTIVE
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(admin));

        assertThrows(
                UnauthorizedException.class,
                () -> userService.blockUser(1L)
        );

        verify(userRepository).findById(1L);
        verify(userRepository, never()).save(admin);
    }

    /**
     * Verifies that a soft-deleted account cannot be activated again.
     */
    @Test
    void unblockUser_WhenUserIsDeleted_ShouldThrowInvalidInputException() {
        UserEntity deletedUser = createUser(
                1L,
                RoleEnum.NORMAL_USER,
                UserStatusEnum.DELETED
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(deletedUser));

        assertThrows(
                InvalidInputException.class,
                () -> userService.unblockUser(1L)
        );

        verify(userRepository).findById(1L);
        verify(userRepository, never()).save(deletedUser);
    }

    /**
     * Verifies that a normal user cannot delete another user's account.
     */
    @Test
    void deleteUser_WhenNormalUserDeletesAnotherUser_ShouldThrowUnauthorizedException() {
        UserEntity currentUser = createUser(
                1L,
                RoleEnum.NORMAL_USER,
                UserStatusEnum.ACTIVE
        );

        UserEntity targetUser = createUser(
                2L,
                RoleEnum.NORMAL_USER,
                UserStatusEnum.ACTIVE
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(currentUser));
        when(userRepository.findById(2L)).thenReturn(Optional.of(targetUser));

        assertThrows(
                UnauthorizedException.class,
                () -> userService.deleteUser(1L, 2L)
        );

        verify(userRepository).findById(1L);
        verify(userRepository).findById(2L);
        verify(userRepository, never()).save(targetUser);
    }

    /**
     * Verifies that a normal user can soft-delete their own account.
     */
    @Test
    void deleteUser_WhenUserDeletesOwnAccount_ShouldMarkUserAsDeleted() {
        UserEntity user = createUser(
                1L,
                RoleEnum.NORMAL_USER,
                UserStatusEnum.ACTIVE
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        userService.deleteUser(1L, 1L);

        assertEquals(UserStatusEnum.DELETED, user.getStatus());
        verify(userRepository, times(2)).findById(1L);
        verify(userRepository).save(user);
    }

    /**
     * Creates a user entity with the specified identifier, role, and status.
     *
     * @param id the identifier assigned to the user
     * @param role the role assigned to the user
     * @param status the current account status
     * @return a configured user entity for unit testing
     */
    private UserEntity createUser(
            Long id,
            RoleEnum role,
            UserStatusEnum status
    ) {
        UserEntity user = new UserEntity(
                "Test User",
                "test-user-" + id,
                "encoded-password",
                "0912000000" + id,
                "user" + id + "@example.com"
        );

        user.setId(id);
        user.setRole(role);
        user.setStatus(status);
        return user;
    }
}
