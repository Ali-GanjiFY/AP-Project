package org.example.backend.service;

import org.example.backend.dto.request.LoginRequest;
import org.example.backend.dto.request.RegisterRequest;
import org.example.backend.dto.response.AuthResponse;

public interface AuthService {

    // Register new user
    AuthResponse register(RegisterRequest request);

    // Authenticate and return JWT
    AuthResponse login(LoginRequest request);
}