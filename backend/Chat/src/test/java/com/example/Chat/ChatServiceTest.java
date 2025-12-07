package com.example.chat.services;

import com.example.chat.entities.ChatThread;
import com.example.chat.entities.Message;
import com.example.chat.repositories.ChatThreadRepository;
import com.example.chat.repositories.MessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChatServiceTest {

    private ChatThreadRepository chatThreadRepository;
    private MessageRepository messageRepository;
    private ChatService service;

    @BeforeEach
    void setup() {
        chatThreadRepository = mock(ChatThreadRepository.class);
        messageRepository = mock(MessageRepository.class);
        service = new ChatService(chatThreadRepository, messageRepository);
    }

    @Test
    void createOrGetChat_shouldReturnExisting_whenDirectOrderFound() {
        ChatThread thread = new ChatThread();
        thread.setId("T1");
        thread.setParticipant1Id("U1");
        thread.setParticipant2Id("U2");

        when(chatThreadRepository.findByParticipant1IdAndParticipant2Id("U1", "U2"))
                .thenReturn(Optional.of(thread));

        String id = service.createOrGetChat("U1", "U2");

        assertEquals("T1", id);
        verify(chatThreadRepository).findByParticipant1IdAndParticipant2Id("U1", "U2");
        verifyNoMoreInteractions(chatThreadRepository);
    }

    @Test
    void createOrGetChat_shouldReturnExisting_whenReverseOrderFound() {
        when(chatThreadRepository.findByParticipant1IdAndParticipant2Id("U1", "U2"))
                .thenReturn(Optional.empty());

        ChatThread thread = new ChatThread();
        thread.setId("T2");
        thread.setParticipant1Id("U2");
        thread.setParticipant2Id("U1");

        when(chatThreadRepository.findByParticipant2IdAndParticipant1Id("U1", "U2"))
                .thenReturn(Optional.of(thread));

        String id = service.createOrGetChat("U1", "U2");

        assertEquals("T2", id);
        verify(chatThreadRepository).findByParticipant1IdAndParticipant2Id("U1", "U2");
        verify(chatThreadRepository).findByParticipant2IdAndParticipant1Id("U1", "U2");
    }

    @Test
    void createOrGetChat_shouldCreateNew_whenNotFound() {
        when(chatThreadRepository.findByParticipant1IdAndParticipant2Id("A", "B"))
                .thenReturn(Optional.empty());
        when(chatThreadRepository.findByParticipant2IdAndParticipant1Id("A", "B"))
                .thenReturn(Optional.empty());

        // stub save to return the same thread with id set
        when(chatThreadRepository.save(any(ChatThread.class))).thenAnswer(inv -> {
            ChatThread t = inv.getArgument(0);
            t.setId("NEW");
            return t;
        });

        String id = service.createOrGetChat("A", "B");

        assertEquals("NEW", id);
        verify(chatThreadRepository).save(any(ChatThread.class));
    }

    @Test
    void sendMessage_shouldSaveAndReturnMessageId_whenValid() {
        String chatId = "C1";
        ChatThread thread = new ChatThread();
        thread.setId(chatId);
        thread.setParticipant1Id("U1");
        thread.setParticipant2Id("U2");

        when(chatThreadRepository.findById(chatId)).thenReturn(Optional.of(thread));

        // capture save and set id
        when(messageRepository.save(any(Message.class))).thenAnswer(inv -> {
            Message m = inv.getArgument(0);
            m.setId("M1");
            return m;
        });

        String msgId = service.sendMessage(chatId, "U1", "hello");

        assertEquals("M1", msgId);
        verify(chatThreadRepository).findById(chatId);
        verify(messageRepository).save(any(Message.class));
    }

    @Test
    void sendMessage_shouldThrow_whenChatNotFound() {
        when(chatThreadRepository.findById("NO")).thenReturn(Optional.empty());

        var ex = assertThrows(RuntimeException.class, () -> service.sendMessage("NO", "U1", "hi"));
        assertTrue(ex.getMessage().contains("Chat thread not found"));
        verify(chatThreadRepository).findById("NO");
        verifyNoInteractions(messageRepository);
    }

    @Test
    void sendMessage_shouldThrow_whenSenderNotParticipant() {
        String chatId = "C2";
        ChatThread thread = new ChatThread();
        thread.setId(chatId);
        thread.setParticipant1Id("A");
        thread.setParticipant2Id("B");

        when(chatThreadRepository.findById(chatId)).thenReturn(Optional.of(thread));

        var ex = assertThrows(RuntimeException.class, () -> service.sendMessage(chatId, "X", "msg"));
        assertTrue(ex.getMessage().contains("Sender not participant of chat"));
        verify(chatThreadRepository).findById(chatId);
        verifyNoInteractions(messageRepository);
    }

    @Test
    void fetchMessages_shouldReturnAll_whenNoAfterProvided() {
        String chatId = "C3";
        Message m1 = new Message();
        m1.setId("m1");
        m1.setChatId(chatId);
        m1.setContent("a");
        m1.setCreatedAt(LocalDateTime.now().minusMinutes(10));

        when(messageRepository.findByChatIdOrderByCreatedAtAsc(chatId)).thenReturn(List.of(m1));

        var res = service.fetchMessages(chatId, null);

        assertEquals(1, res.size());
        assertEquals("m1", res.get(0).getId());
        verify(messageRepository).findByChatIdOrderByCreatedAtAsc(chatId);
    }

    @Test
    void fetchMessages_shouldReturnAfterProvided() {
        String chatId = "C4";
        LocalDateTime after = LocalDateTime.now().minusHours(1);

        Message m2 = new Message();
        m2.setId("m2");
        m2.setChatId(chatId);
        m2.setCreatedAt(LocalDateTime.now());

        when(messageRepository.findByChatIdAndCreatedAtAfterOrderByCreatedAtAsc(chatId, after))
                .thenReturn(List.of(m2));

        var res = service.fetchMessages(chatId, after);

        assertEquals(1, res.size());
        assertEquals("m2", res.get(0).getId());
        verify(messageRepository).findByChatIdAndCreatedAtAfterOrderByCreatedAtAsc(chatId, after);
    }

    @Test
    void fetchChatsForUser_shouldReturnList() {
        ChatThread t1 = new ChatThread();
        t1.setId("T1");
        t1.setParticipant1Id("U1");
        t1.setParticipant2Id("U2");

        when(chatThreadRepository.findByParticipant1IdOrParticipant2Id("U1", "U1"))
                .thenReturn(List.of(t1));

        var res = service.fetchChatsForUser("U1");

        assertEquals(1, res.size());
        assertEquals("T1", res.get(0).getId());
        verify(chatThreadRepository).findByParticipant1IdOrParticipant2Id("U1", "U1");
    }

    @Test
    void markAllRead_shouldMarkUnreadFromOtherUser_andSave() {
        String chatId = "C5";
        Message m1 = new Message();
        m1.setId("a");
        m1.setChatId(chatId);
        m1.setSenderId("S1");
        m1.setIsRead(false);

        Message m2 = new Message();
        m2.setId("b");
        m2.setChatId(chatId);
        m2.setSenderId("S2");
        m2.setIsRead(false);

        // byUserId = S1, so only messages not sent by S1 should be marked read (m2)
        when(messageRepository.findByChatIdOrderByCreatedAtAsc(chatId)).thenReturn(List.of(m1, m2));

        service.markAllRead(chatId, "S1");

        // m1 should remain unread, m2 should be saved with isRead true
        assertFalse(m1.getIsRead());
        assertTrue(m2.getIsRead());
        verify(messageRepository).findByChatIdOrderByCreatedAtAsc(chatId);
        verify(messageRepository).save(m2);
        verify(messageRepository, never()).save(m1);
    }
}
