package com.example.chat.services;

import com.example.chat.entities.ChatThread;
import com.example.chat.repositories.ChatThreadRepository;
import com.example.chat.repositories.MessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChatServiceTest {

    private ChatThreadRepository chatThreadRepository;
    private MessageRepository messageRepository;
    private ChatService chatService;

    @BeforeEach
    void setup() {
        // Manual mocking exactly like your SearchServiceTest
        chatThreadRepository = mock(ChatThreadRepository.class);
        messageRepository = mock(MessageRepository.class);
        chatService = new ChatService(chatThreadRepository, messageRepository);
    }

    @Test
    void testCreateChat_New() {
        String u1 = "user1";
        String u2 = "user2";

        when(chatThreadRepository.findByParticipant1IdAndParticipant2Id(u1, u2)).thenReturn(Optional.empty());
        when(chatThreadRepository.findByParticipant2IdAndParticipant1Id(u1, u2)).thenReturn(Optional.empty());

        String result = chatService.createOrGetChat(u1, u2);

        assertNotNull(result);
        verify(chatThreadRepository, times(1)).save(any(ChatThread.class));
    }
}
