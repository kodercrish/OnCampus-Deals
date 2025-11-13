package com.example.identity.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class LoginResponse {
    private String message;
    private String token;
    private String userId;
}
