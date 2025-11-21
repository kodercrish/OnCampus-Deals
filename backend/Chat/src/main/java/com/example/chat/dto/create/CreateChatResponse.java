package com.example.chat.dto.create;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CreateChatResponse {
    private String message;
    private String chatId;
}
