package org.example.backend.service.impl;

import jakarta.transaction.Transactional;
import org.example.backend.dto.request.LoginRequest;
import org.example.backend.dto.request.RegisterRequest;
import org.example.backend.dto.response.AuthResponse;
import org.example.backend.entity.UserEntity;
import org.example.backend.enums.RoleEnum;
import org.example.backend.enums.UserStatusEnum;
import org.example.backend.exception.AuthenticationException;
import org.example.backend.exception.DuplicateResourceException;
import org.example.backend.repository.UserRepository;
import org.example.backend.service.AuthService;
import org.example.backend.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // Register a new user with unique validation and JWT generation
    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Check for duplicate username
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Username already exists");
        }
        // Check for duplicate phone
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new DuplicateResourceException("Phone already exists");
        }
        // Check for duplicate email (if provided)
        if (request.getEmail() != null && !request.getEmail().isBlank()
                && userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }

        // Create user
        UserEntity user = new UserEntity();
        user.setFullName(request.getFullName());
        user.setUsername(request.getUsername());
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // Hash password
        user.setRole(RoleEnum.NORMAL_USER); // Default role
        user.setStatus(UserStatusEnum.ACTIVE); // Default status

        userRepository.save(user);

        // Generate JWT token for immediate login
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole().name());
        return new AuthResponse(token, user.getId(), user.getUsername(), user.getRole());
    }

    // Authenticate user and return JWT token
    @Override
    public AuthResponse login(LoginRequest request) {
        // Find by username
        UserEntity user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AuthenticationException("Invalid username or password"));

        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AuthenticationException("Invalid username or password");
        }
        // Check user is not blocked
        if (user.getStatus() != UserStatusEnum.ACTIVE) {
            throw new AuthenticationException("User is blocked");
        }

        // Generate JWT token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole().name());
        return new AuthResponse(token, user.getId(), user.getUsername(), user.getRole());
    }
}