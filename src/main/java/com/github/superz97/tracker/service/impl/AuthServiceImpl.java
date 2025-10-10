package com.github.superz97.tracker.service.impl;

import com.github.superz97.tracker.dto.request.LoginRequest;
import com.github.superz97.tracker.dto.request.RefreshTokenRequest;
import com.github.superz97.tracker.dto.request.SignupRequest;
import com.github.superz97.tracker.dto.response.JwtResponse;
import com.github.superz97.tracker.dto.response.UserResponse;
import com.github.superz97.tracker.entity.User;
import com.github.superz97.tracker.exception.BadRequestException;
import com.github.superz97.tracker.mapper.UserMapper;
import com.github.superz97.tracker.repository.UserRepository;
import com.github.superz97.tracker.security.JwtTokenProvider;
import com.github.superz97.tracker.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserMapper userMapper;

    @Value("${app.jwt.expiration}")
    private Long jwtExpiration;

    @Override
    @Transactional
    public UserResponse signup(SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            throw new BadRequestException("Username is already taken");
        }
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new BadRequestException("Email is already registered");
        }
        User user = User.builder()
                .username(signupRequest.getUsername())
                .email(signupRequest.getEmail())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .firstName(signupRequest.getFirstName())
                .lastName(signupRequest.getLastName())
                .isActive(true)
                .build();
        User savedUser = userRepository.save(user);
        log.info("New user registered successfully: {}", savedUser.getUsername());
        return userMapper.toUserResponse(savedUser);
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
