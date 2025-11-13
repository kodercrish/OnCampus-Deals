package com.example.identity.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class SignupRequest {
    private String fullName;
    private String email;
    private String password;
    private Integer graduationYear;
}
