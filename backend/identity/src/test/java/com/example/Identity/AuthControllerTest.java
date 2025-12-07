package com.example.identity.controllers;

import com.example.identity.entities.Users;
import com.example.identity.services.AuthService;
import com.example.identity.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    void signup_shouldReturnTokenAndUserId_whenSuccess() throws Exception {
        String email = "u@x.com";
        Users u = new Users();
        u.setId(UUID.randomUUID().toString());
        u.setEmail(email);
        u.setName("Name");
        u.setCreatedAt(LocalDateTime.now());

        when(authService.registerUser(eq(email), eq("Name"), eq("pw"), eq(2025))).thenReturn(u);
        when(jwtUtil.generateToken(email)).thenReturn("tok");

        var req = mapper.writeValueAsString(
                new java.util.HashMap<String, Object>() {{
                    put("email", email);
                    put("fullName", "Name");
                    put("password", "pw");
                    put("graduationYear", 2025);
                }}
        );

        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(req))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully"))
                .andExpect(jsonPath("$.token").value("tok"))
                .andExpect(jsonPath("$.userId").value(u.getId()));

        verify(authService).registerUser(eq(email), eq("Name"), eq("pw"), eq(2025));
        verify(jwtUtil).generateToken(email);
    }

    @Test
    void signup_shouldReturn400_whenEmailAlreadyUsed() throws Exception {
        String email = "exists@x.com";

        when(authService.registerUser(eq(email), anyString(), anyString(), any())).thenThrow(new RuntimeException("Email already in use"));

        var req = mapper.writeValueAsString(
                new java.util.HashMap<String, Object>() {{
                    put("email", email);
                    put("fullName", "Name");
                    put("password", "pw");
                    put("graduationYear", 2025);
                }}
        );

        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(req))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Email already in use"));

        verify(authService).registerUser(eq(email), anyString(), anyString(), any());
        verifyNoInteractions(jwtUtil);
    }

    @Test
    void login_shouldReturnTokenAndUserId_whenSuccess() throws Exception {
        String email = "login@x.com";
        Users u = new Users();
        u.setId(UUID.randomUUID().toString());
        u.setEmail(email);

        when(authService.authenticateUser(eq(email), eq("pw"))).thenReturn(u);
        when(jwtUtil.generateToken(email)).thenReturn("tok-login");

        var req = mapper.writeValueAsString(
                new java.util.HashMap<String, Object>() {{
                    put("email", email);
                    put("password", "pw");
                }}
        );

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(req))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login successful"))
                .andExpect(jsonPath("$.token").value("tok-login"))
                .andExpect(jsonPath("$.userId").value(u.getId()));

        verify(authService).authenticateUser(eq(email), eq("pw"));
        verify(jwtUtil).generateToken(email);
    }

    @Test
    void login_shouldReturn400_whenInvalidCredentials() throws Exception {
        String email = "bad@x.com";

        when(authService.authenticateUser(eq(email), eq("pw"))).thenThrow(new RuntimeException("Invalid Credentials"));

        var req = mapper.writeValueAsString(
                new java.util.HashMap<String, Object>() {{
                    put("email", email);
                    put("password", "pw");
                }}
        );

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(req))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid Credentials"));

        verify(authService).authenticateUser(eq(email), eq("pw"));
        verifyNoInteractions(jwtUtil);
    }
}
