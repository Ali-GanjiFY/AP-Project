package org.example.backend.repository;

import org.example.backend.entity.UserEntity;
import org.example.backend.enums.UserStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Contract for user repository.
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    /**
     * Finds by username.
     * @param username the username
     * @return the result
     */
    Optional<UserEntity> findByUsername(String username);
    /**
     * Finds by email.
     * @param email the email
     * @return the result
     */
    Optional<UserEntity> findByEmail(String email);
    /**
     * Finds by phone.
     * @param phone the phone
     * @return the result
     */
    Optional<UserEntity> findByPhone(String phone);
    /**
     * Checks whether by username.
     * @param username the username
     * @return the result
     */
    boolean existsByUsername(String username);
    /**
     * Checks whether by phone.
     * @param phone the phone
     * @return the result
     */
    boolean existsByPhone(String phone);
    /**
     * Checks whether by email.
     * @param email the email
     * @return the result
     */
    boolean existsByEmail(String email);
    /**
     * Finds by status.
     * @param status the status
     * @return the result
     */
    List<UserEntity> findByStatus(UserStatusEnum status);
}