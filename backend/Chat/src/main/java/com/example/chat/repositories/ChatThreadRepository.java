package com.example.chat.repositories;

import com.example.chat.entities.ChatThread;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface ChatThreadRepository extends JpaRepository<ChatThread, String> {

    // find existing thread between two users (either order)
    Optional<ChatThread> findByParticipant1IdAndParticipant2Id(String p1, String p2);
    Optional<ChatThread> findByParticipant2IdAndParticipant1Id(String p1, String p2);

    // find all threads where user participates
    List<ChatThread> findByParticipant1IdOrParticipant2Id(String participant1Id, String participant2Id);
}
