package org.example.backend.repository;

import org.example.backend.entity.ChatMessage;
import org.example.backend.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByConversationOrderBySentAtAsc(Conversation conversation);
    ChatMessage findTopByConversationOrderBySentAtDesc(Conversation conversation);
    List<ChatMessage> findByConversationAndSeenFalse(Conversation conversation);
}