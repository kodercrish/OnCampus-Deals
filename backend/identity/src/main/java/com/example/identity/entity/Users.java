package com.example.identity.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Users {

    @Id
    @Column(name = "id", updatable = false, nullable = false, length = 36)
    private String id = UUID.randomUUID().toString();

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 255, unique = true, nullable = false)
    private String email;

    @Column(name = "password_hash", length = 255, nullable = false)
    private String passwordHash;

    @Column(name = "graduation_year")
    private Integer graduationYear;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private String role = "STUDENT";
}
