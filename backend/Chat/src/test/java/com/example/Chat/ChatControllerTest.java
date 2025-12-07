package com.example.chat.controllers;

import com.example.chat.constants.ApiEndpoints;
import com.example.chat.dto.create.CreateChatRequest;
import com.example.chat.dto.message.SendMessageRequest;
import com.example.chat.repositories.ChatThreadRepository;
import com.example.chat.repositories.MessageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
public class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    // We inject Repositories to clean/setup DB state before tests
    @Autowired
    private ChatThreadRepository chatThreadRepository;
    @Autowired
    private MessageRepository messageRepository;

    @BeforeEach
    void setup() {
        messageRepository.deleteAll();
        chatThreadRepository.deleteAll();
    }

    @Test
    void testCreateChat() throws Exception {
        CreateChatRequest req = new CreateChatRequest("user1", "user2");

        mockMvc.perform(post(ApiEndpoints.CHAT_BASE + ApiEndpoints.CREATE_CHAT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Chat ready"))
                .andExpect(jsonPath("$.chatId").isNotEmpty());
    }

    @Test
    void testSendMessage() throws Exception {
        // 1. Create a chat first (using real service logic implicitly via DB)
        // We can manually save to DB to set up the state
        com.example.chat.entities.ChatThread thread = new com.example.chat.entities.ChatThread();
        thread.setParticipant1Id("userA");
        thread.setParticipant2Id("userB");
        chatThreadRepository.save(thread);

        // 2. Send message to that chat
        SendMessageRequest req = new SendMessageRequest(thread.getId(), "userA", "Hello World");

        mockMvc.perform(post(ApiEndpoints.CHAT_BASE + ApiEndpoints.SEND_MESSAGE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Message sent"));
    }
}