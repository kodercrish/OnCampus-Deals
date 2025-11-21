package com.example.chat.dto.fetch;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class FetchMessagesRequest {
    private String chatId;
    // optional: fetch messages after a timestamp (ISO string)
    private String afterTimestamp;
}
