package com.example.chat.controllers;

import com.example.chat.constants.ApiEndpoints;
import com.example.chat.dto.create.*;
import com.example.chat.dto.message.*;
import com.example.chat.dto.fetch.*;
import com.example.chat.services.ChatService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping(ApiEndpoints.CHAT_BASE)
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    /** Create or get chat thread */
    @PostMapping(ApiEndpoints.CREATE_CHAT)
    public ResponseEntity<CreateChatResponse> createChat(@RequestBody CreateChatRequest request) {
        try {
            String chatId = chatService.createOrGetChat(request.getUser1Id(), request.getUser2Id());
            return ResponseEntity.ok(new CreateChatResponse("Chat ready", chatId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(new CreateChatResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new CreateChatResponse("Internal server error", null));
        }
    }

    /** Send message */
    @PostMapping(ApiEndpoints.SEND_MESSAGE)
    public ResponseEntity<SendMessageResponse> sendMessage(@RequestBody SendMessageRequest request) {
        try {
            String msgId = chatService.sendMessage(request.getChatId(), request.getSenderId(), request.getContent());
            return ResponseEntity.ok(new SendMessageResponse("Message sent", msgId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(new SendMessageResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new SendMessageResponse("Internal server error", null));
        }
    }

    /** Fetch messages for chat (optional afterTimestamp in ISO format) */
    @PostMapping(ApiEndpoints.FETCH_MESSAGES)
    public ResponseEntity<FetchMessagesResponse> fetchMessages(@RequestBody FetchMessagesRequest request) {
        try {
            LocalDateTime after = null;
            if (request.getAfterTimestamp() != null && !request.getAfterTimestamp().isBlank()) {
                try {
                    after = LocalDateTime.parse(request.getAfterTimestamp());
                } catch (DateTimeParseException ex) {
                    return ResponseEntity.status(400).body(new FetchMessagesResponse("Invalid afterTimestamp format", null));
                }
            }
            List<com.example.chat.entities.Message> messages = chatService.fetchMessages(request.getChatId(), after);
            return ResponseEntity.ok(new FetchMessagesResponse("Messages fetched", messages));
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(new FetchMessagesResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new FetchMessagesResponse("Internal server error", null));
        }
    }

    /** Fetch all chats for a user */
    @PostMapping(ApiEndpoints.FETCH_USER_CHATS)
    public ResponseEntity<FetchChatsResponse> fetchChats(@RequestBody FetchChatsRequest request) {
        try {
            var chats = chatService.fetchChatsForUser(request.getUserId());
            return ResponseEntity.ok(new FetchChatsResponse(chats));
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(new FetchChatsResponse(null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new FetchChatsResponse(null));
        }
    }
}
