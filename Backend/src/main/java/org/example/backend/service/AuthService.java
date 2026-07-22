package org.example.backend.service;

import org.example.backend.dto.request.LoginRequest;
import org.example.backend.dto.request.RegisterRequest;
import org.example.backend.dto.response.AuthResponse;

/**
 * Contract for auth service.
 */
public interface AuthService {

    /**
     * Register new user.
     * @param request the request
     * @return the result
     */
    AuthResponse register(RegisterRequest request);

    /**
     * Authenticate and return JWT.
     * @param request the request
     * @return the result
     */
    AuthResponse login(LoginRequest request);
}