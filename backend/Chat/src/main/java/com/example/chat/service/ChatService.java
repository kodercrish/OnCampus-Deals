package com.example.chat.services;

import com.example.chat.entities.ChatThread;
import com.example.chat.entities.Message;
import com.example.chat.repositories.ChatThreadRepository;
import com.example.chat.repositories.MessageRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatThreadRepository chatThreadRepository;
    private final MessageRepository messageRepository;

    /**
     * Create or return existing chat thread between two users.
     */
    @Transactional
    public String createOrGetChat(String user1Id, String user2Id) {

        // Try direct order
        return chatThreadRepository.findByParticipant1IdAndParticipant2Id(user1Id, user2Id)
                .or(() -> chatThreadRepository.findByParticipant2IdAndParticipant1Id(user1Id, user2Id))
                .map(ChatThread::getId)
                .orElseGet(() -> {
                    ChatThread thread = new ChatThread();
                    thread.setParticipant1Id(user1Id);
                    thread.setParticipant2Id(user2Id);
                    thread.setCreatedAt(LocalDateTime.now());
                    chatThreadRepository.save(thread);
                    return thread.getId();
                });
    }

    /**
     * Send message (store it). Returns message id.
     */
    public String sendMessage(String chatId, String senderId, String content) {
        // ensure chat exists
        ChatThread thread = chatThreadRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat thread not found"));

        Message msg = new Message();
        msg.setChatId(chatId);
        msg.setSenderId(senderId);
        msg.setContent(content);
        msg.setIsRead(false);
        msg.setCreatedAt(LocalDateTime.now());

        messageRepository.save(msg);
        return msg.getId();
    }

    /**
     * Fetch all messages for a chat (optionally after a timestamp)
     */
    public List<Message> fetchMessages(String chatId, LocalDateTime after) {
        if (after == null) {
            return messageRepository.findByChatIdOrderByCreatedAtAsc(chatId);
        } else {
            return messageRepository.findByChatIdAndCreatedAtAfterOrderByCreatedAtAsc(chatId, after);
        }
    }

    /**
     * List all chat threads a user participates in.
     */
    public List<ChatThread> fetchChatsForUser(String userId) {
        return chatThreadRepository.findByParticipant1IdOrParticipant2Id(userId, userId);
    }

    /**
     * Mark messages in chat as read (from other user) - optional utility
     */
    @Transactional
    public void markAllRead(String chatId, String byUserId) {
        List<Message> messages = messageRepository.findByChatIdOrderByCreatedAtAsc(chatId);
        for (Message m : messages) {
            if (!m.getIsRead() && !m.getSenderId().equals(byUserId)) {
                m.setIsRead(true);
                messageRepository.save(m);
            }
        }
    }
}
