package com.example.chat.dto.message;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class SendMessageRequest {
    private String chatId;
    private String senderId;
    private String content;
}
