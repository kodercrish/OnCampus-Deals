package com.example.chat.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "messages",
       indexes = {
           @Index(name = "idx_chat_id", columnList = "chat_id"),
           @Index(name = "idx_sender_id", columnList = "sender_id"),
           @Index(name = "idx_created_at", columnList = "created_at")
       })
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Message {

    @Id
    @Column(name = "id", nullable = false, updatable = false, length = 36)
    private String id = UUID.randomUUID().toString();

    @Column(name = "chat_id", nullable = false, length = 36)
    private String chatId; // FK to ChatThread.id

    @Column(name = "sender_id", nullable = false, length = 36)
    private String senderId; // User id

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "is_read", nullable = false)
    private Boolean isRead = false;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
