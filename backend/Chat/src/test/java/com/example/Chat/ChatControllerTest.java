package com.example.chat.controllers;

import com.example.chat.entities.ChatThread;
import com.example.chat.entities.Message;
import com.example.chat.services.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ChatService chatService;

    @Test
    void createChat_shouldReturnChatId_whenSuccess() throws Exception {
        String u1 = "U1", u2 = "U2";
        String chatId = UUID.randomUUID().toString();

        when(chatService.createOrGetChat(u1, u2)).thenReturn(chatId);

        var payload = mapper.writeValueAsString(new java.util.HashMap<String,String>() {{
            put("user1Id", u1);
            put("user2Id", u2);
        }});

        mockMvc.perform(post("/api/chat/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("Chat ready")))
                .andExpect(content().string(Matchers.containsString(chatId)));

        verify(chatService).createOrGetChat(u1, u2);
    }

    @Test
    void createChat_shouldReturn400_whenServiceThrowsRuntime() throws Exception {
        String u1 = "U1", u2 = "U2";

        when(chatService.createOrGetChat(u1, u2)).thenThrow(new RuntimeException("some error"));

        var payload = mapper.writeValueAsString(new java.util.HashMap<String,String>() {{
            put("user1Id", u1);
            put("user2Id", u2);
        }});

        mockMvc.perform(post("/api/chat/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(Matchers.containsString("some error")));

        verify(chatService).createOrGetChat(u1, u2);
    }

    @Test
    void sendMessage_shouldReturnMessageId_whenSuccess() throws Exception {
        String chatId = "C1", senderId = "S1", contentStr = "hello!";
        String msgId = UUID.randomUUID().toString();

        when(chatService.sendMessage(chatId, senderId, contentStr)).thenReturn(msgId);

        var payload = mapper.writeValueAsString(new java.util.HashMap<String,String>() {{
            put("chatId", chatId);
            put("senderId", senderId);
            put("content", contentStr);
        }});

        mockMvc.perform(post("/api/chat/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("Message sent")))
                .andExpect(content().string(Matchers.containsString(msgId)));

        verify(chatService).sendMessage(chatId, senderId, contentStr);
    }

    @Test
    void sendMessage_shouldReturn400_whenServiceThrowsRuntime() throws Exception {
        String chatId = "C1", senderId = "S1", contentStr = "hi";

        when(chatService.sendMessage(chatId, senderId, contentStr)).thenThrow(new RuntimeException("Sender not participant"));

        var payload = mapper.writeValueAsString(new java.util.HashMap<String,String>() {{
            put("chatId", chatId);
            put("senderId", senderId);
            put("content", contentStr);
        }});

        mockMvc.perform(post("/api/chat/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(Matchers.containsString("Sender not participant")));

        verify(chatService).sendMessage(chatId, senderId, contentStr);
    }

    @Test
    void fetchMessages_shouldReturnMessages_whenNoAfterProvided() throws Exception {
        String chatId = "C2";
        Message m = new Message();
        m.setId("M1");
        m.setChatId(chatId);
        m.setSenderId("S1");
        m.setContent("hello world");
        m.setCreatedAt(LocalDateTime.now());

        when(chatService.fetchMessages(eq(chatId), isNull())).thenReturn(List.of(m));

        var payload = mapper.writeValueAsString(new java.util.HashMap<String,String>() {{
            put("chatId", chatId);
            put("afterTimestamp", ""); // empty -> treated as null
        }});

        mockMvc.perform(post("/api/chat/fetch-messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("Messages fetched")))
                .andExpect(content().string(Matchers.containsString("hello world")));

        verify(chatService).fetchMessages(eq(chatId), isNull());
    }

    @Test
    void fetchMessages_shouldReturn400_forInvalidAfterTimestamp() throws Exception {
        String chatId = "C2";

        var payload = mapper.writeValueAsString(new java.util.HashMap<String,String>() {{
            put("chatId", chatId);
            put("afterTimestamp", "not-a-timestamp");
        }});

        mockMvc.perform(post("/api/chat/fetch-messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(Matchers.containsString("Invalid afterTimestamp format")));

        verifyNoInteractions(chatService);
    }

    @Test
    void fetchChats_shouldReturnChatThreads() throws Exception {
        String userId = "U5";
        ChatThread t = new ChatThread();
        t.setId("T100");
        t.setParticipant1Id(userId);
        t.setParticipant2Id("X");

        when(chatService.fetchChatsForUser(userId)).thenReturn(List.of(t));

        var payload = mapper.writeValueAsString(new java.util.HashMap<String,String>() {{
            put("userId", userId);
        }});

        mockMvc.perform(post("/api/chat/my-chats")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("T100")));

        verify(chatService).fetchChatsForUser(userId);
    }
}
