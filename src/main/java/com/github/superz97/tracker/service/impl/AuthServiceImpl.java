package com.github.superz97.tracker.service.impl;

import com.github.superz97.tracker.dto.request.LoginRequest;
import com.github.superz97.tracker.dto.request.RefreshTokenRequest;
import com.github.superz97.tracker.dto.request.SignupRequest;
import com.github.superz97.tracker.dto.response.JwtResponse;
import com.github.superz97.tracker.dto.response.UserResponse;
import com.github.superz97.tracker.repository.UserRepository;
import com.github.superz97.tracker.security.JwtTokenProvider;
import com.github.superz97.tracker.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    // private final UserMapper userMapper;

    @Override
    public UserResponse signup(SignupRequest signupRequest) {
        return null;
    }

    @Override
    public JwtResponse login(LoginRequest loginRequest) {
        return null;
    }

    @Override
    public JwtResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        return null;
    }
}
