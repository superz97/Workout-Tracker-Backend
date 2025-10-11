package com.github.superz97.tracker.controller;

import com.github.superz97.tracker.dto.request.LoginRequest;
import com.github.superz97.tracker.dto.request.RefreshTokenRequest;
import com.github.superz97.tracker.dto.request.SignupRequest;
import com.github.superz97.tracker.dto.response.ApiResponse;
import com.github.superz97.tracker.dto.response.JwtResponse;
import com.github.superz97.tracker.dto.response.UserResponse;
import com.github.superz97.tracker.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    @Operation(summary = "Register a new user", description = "Create a new user account")
    public ResponseEntity<ApiResponse<UserResponse>> signup(@Valid @RequestBody SignupRequest signupRequest) {
        UserResponse userResponse = authService.signup(signupRequest);
        return new ResponseEntity<>(
                ApiResponse.success("User registered successfully",  userResponse),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user", description = "Login with username/email and password")
    public ResponseEntity<ApiResponse<JwtResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        JwtResponse jwtResponse = authService.login(loginRequest);
        return ResponseEntity.ok(
                ApiResponse.success("Login successful", jwtResponse)
        );
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh access token", description = "Get a new access token using refresh token")
    public ResponseEntity<ApiResponse<JwtResponse>> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        JwtResponse jwtResponse = authService.refreshToken(refreshTokenRequest);
        return ResponseEntity.ok(
                ApiResponse.success("Token refreshed successfully", jwtResponse)
        );
    }

}
