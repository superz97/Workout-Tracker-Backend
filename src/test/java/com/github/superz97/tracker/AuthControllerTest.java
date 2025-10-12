package com.github.superz97.tracker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.superz97.tracker.dto.request.LoginRequest;
import com.github.superz97.tracker.dto.request.SignupRequest;
import com.github.superz97.tracker.dto.response.JwtResponse;
import com.github.superz97.tracker.dto.response.UserResponse;
import com.github.superz97.tracker.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private AuthService authService;

    private SignupRequest signupRequest;
    private LoginRequest loginRequest;
    private UserResponse userResponse;
    private JwtResponse jwtResponse;

    @BeforeEach
    void setUp() {
        signupRequest = SignupRequest.builder()
                .username("testuser")
                .email("test@example.com")
                .password("TestPass123!")
                .firstName("Test")
                .lastName("User")
                .build();
        loginRequest = LoginRequest.builder()
                .usernameOrEmail("testuser")
                .password("TestPass123!")
                .build();
        userResponse = UserResponse.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .firstName("Test")
                .lastName("User")
                .isActive(true)
                .build();
        jwtResponse = JwtResponse.builder()
                .accessToken("test.access.token")
                .refreshToken("test.refresh.token")
                .tokenType("Bearer")
                .expiresIn(3600000L)
                .build();
    }

    @Test
    void signup_ShouldReturnCreated_WhenValidRequest() throws Exception {
        when(authService.signup(any(SignupRequest.class))).thenReturn(userResponse);
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User registered successfully"))
                .andExpect(jsonPath("$.data.username").value("testuser"))
                .andExpect(jsonPath("$.data.email").value("test@example.com"));
    }

    @Test
    void signup_ShouldReturnBadRequest_WhenInvalidEmail() throws Exception {
        signupRequest.setEmail("invalid-email");
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void login_ShouldReturnToken_WhenValidCredentials() throws Exception {
        when(authService.login(any(LoginRequest.class))).thenReturn(jwtResponse);
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Login successful"))
                .andExpect(jsonPath("$.data.accessToken").exists())
                .andExpect(jsonPath("$.data.refreshToken").exists())
                .andExpect(jsonPath("$.data.tokenType").value("Bearer"));
    }

    @Test
    void login_ShouldReturnBadRequest_WhenMissingPassword() throws Exception {
        loginRequest.setPassword(null);
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

}
