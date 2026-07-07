package org.example.backend.repository;

import org.example.backend.entity.UserEntity;
import org.example.backend.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByPhone(String phone);
    boolean existsByUsername(String username);
    boolean existsByPhone(String phone);
    boolean existsByEmail(String email);
    List<UserEntity> findByStatus(UserStatus status);
}