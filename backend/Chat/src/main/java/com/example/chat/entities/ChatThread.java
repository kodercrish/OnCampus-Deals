package com.example.chat.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "chat_threads",
       indexes = {
           @Index(name = "idx_participant1", columnList = "participant1_id"),
           @Index(name = "idx_participant2", columnList = "participant2_id")
       })
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ChatThread {

    @Id
    @Column(name = "id", nullable = false, updatable = false, length = 36)
    private String id = UUID.randomUUID().toString();

    @Column(name = "participant1_id", nullable = false, length = 36)
    private String participant1Id;

    @Column(name = "participant2_id", nullable = false, length = 36)
    private String participant2Id;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
