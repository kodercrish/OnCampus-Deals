package com.example.chat.dto.fetch;

import lombok.*;
import com.example.chat.entities.ChatThread;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class FetchChatsResponse {
    private List<ChatThread> chats;
}
