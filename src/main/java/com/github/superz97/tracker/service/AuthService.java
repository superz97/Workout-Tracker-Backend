package com.github.superz97.tracker.service;

import com.github.superz97.tracker.dto.request.LoginRequest;
import com.github.superz97.tracker.dto.request.RefreshTokenRequest;
import com.github.superz97.tracker.dto.request.SignupRequest;
import com.github.superz97.tracker.dto.response.JwtResponse;
import com.github.superz97.tracker.dto.response.UserResponse;

public interface AuthService {

    UserResponse signup(SignupRequest signupRequest);
    JwtResponse login(LoginRequest loginRequest);
    JwtResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

}
