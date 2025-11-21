package com.example.chat.dto.message;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class SendMessageResponse {
    private String message;
    private String messageId;
}
