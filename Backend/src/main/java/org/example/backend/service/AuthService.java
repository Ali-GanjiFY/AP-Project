package org.example.backend.service;

import org.example.backend.dto.request.LoginRequest;
import org.example.backend.dto.request.RegisterRequest;
import org.example.backend.dto.response.AuthResponse;

public interface AuthService {

    // Register a new user with unique username, phone, email validation
    AuthResponse register(RegisterRequest request);

    // Authenticate user with username and password, return JWT token
    AuthResponse login(LoginRequest request);
}