package org.example.backend.service;

import org.example.backend.dto.request.LoginRequest;
import org.example.backend.dto.request.RegisterRequest;
import org.example.backend.dto.response.AuthResponse;
import org.example.backend.entity.User;
import org.example.backend.enums.UserStatus;
import org.example.backend.exception.AuthenticationException;
import org.example.backend.exception.DuplicateResourceException;
import org.example.backend.exception.UnauthorizedException;
import org.example.backend.repository.UserRepository;
import org.example.backend.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("این نام کاربری قبلاً ثبت شده است");
        }
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new DuplicateResourceException("این شماره تلفن قبلاً ثبت شده است");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = new User(
                request.getFullName(),
                request.getUsername(),
                encodedPassword,
                request.getPhone(),
                request.getEmail()
        );

        User savedUser = userRepository.save(user);

        String token = jwtUtil.generateToken(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getRole().name()
        );

        return new AuthResponse(token, savedUser.getId(), savedUser.getUsername(), savedUser.getRole());
    }

    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AuthenticationException("نام کاربری یا رمز عبور اشتباه است"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AuthenticationException("نام کاربری یا رمز عبور اشتباه است");
        }

        if (user.getStatus() == UserStatus.BLOCKED) {
            throw new UnauthorizedException("حساب کاربری شما مسدود شده است");
        }

        String token = jwtUtil.generateToken(
                user.getId(),
                user.getUsername(),
                user.getRole().name()
        );

        return new AuthResponse(token, user.getId(), user.getUsername(), user.getRole());
    }
}