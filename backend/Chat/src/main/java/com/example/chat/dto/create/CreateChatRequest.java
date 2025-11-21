package com.example.chat.dto.create;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CreateChatRequest {
    private String user1Id;
    private String user2Id;
}
