package com.example.chat.dto.fetch;

import lombok.*;
import com.example.chat.entities.Message;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class FetchMessagesResponse {
    private String message;
    private List<Message> messages;
}
