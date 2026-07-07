package org.example.backend.repository;

import org.example.backend.entity.ChatMessageEntity;
import org.example.backend.entity.ConversationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {
    List<ChatMessageEntity> findByConversationOrderBySentAtAsc(ConversationEntity conversation);
    ChatMessageEntity findTopByConversationOrderBySentAtDesc(ConversationEntity conversation);
    List<ChatMessageEntity> findByConversationAndSeenFalse(ConversationEntity conversation);
}