package com.example.identity.controllers;

import com.example.identity.dto.*;
import com.example.identity.constants.ApiEndpoints;
import com.example.identity.entities.Users;
import com.example.identity.services.AuthService;
import com.example.identity.util.JwtUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiEndpoints.AUTH_BASE)
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @PostMapping(ApiEndpoints.SIGNUP)
    public ResponseEntity<SignupResponse> signup(@RequestBody SignupRequest request) {
        try {
            Users newUser = authService.registerUser(
                    request.getEmail(),
                    request.getFullName(),
                    request.getPassword(),
                    request.getGraduationYear()
            );

            String token = jwtUtil.generateToken(newUser.getEmail());

            SignupResponse response = new SignupResponse(
                    "User registered successfully",
                    token,
                    newUser.getId()
            );

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.status(400)
                    .body(new SignupResponse(e.getMessage(), null, null));

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new SignupResponse("Internal Server Error", null, null));
        }
    }

    @PostMapping(ApiEndpoints.LOGIN)
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        try {
            Users user = authService.authenticateUser(request.getEmail(), request.getPassword());

            String token = jwtUtil.generateToken(user.getEmail());

            LoginResponse response = new LoginResponse(
                    "Login successful",
                    token,
                    user.getId()
            );

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.status(400)
                    .body(new LoginResponse(e.getMessage(), null, null));

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new LoginResponse("Internal Server Error", null, null));
        }
    }
}
