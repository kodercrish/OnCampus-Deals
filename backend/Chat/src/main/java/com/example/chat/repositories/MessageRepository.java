package com.example.chat.repositories;

import com.example.chat.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, String> {

    List<Message> findByChatIdOrderByCreatedAtAsc(String chatId);

    List<Message> findByChatIdAndCreatedAtAfterOrderByCreatedAtAsc(String chatId, java.time.LocalDateTime after);

    long countByChatIdAndIsReadFalseAndSenderIdNot(String chatId, String senderId);
}
