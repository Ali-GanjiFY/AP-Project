package org.example.backend.repository;

import org.example.backend.entity.ChatMessageEntity;
import org.example.backend.entity.ConversationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Contract for chat message repository.
 */
@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {
    /**
     * Finds by conversation order by sent at asc.
     * @param conversation the conversation
     * @return the result
     */
    List<ChatMessageEntity> findByConversationOrderBySentAtAsc(ConversationEntity conversation);
    /**
     * Finds top by conversation order by sent at desc.
     * @param conversation the conversation
     * @return the result
     */
    ChatMessageEntity findTopByConversationOrderBySentAtDesc(ConversationEntity conversation);
    /**
     * Finds by conversation and seen false.
     * @param conversation the conversation
     * @return the result
     */
    List<ChatMessageEntity> findByConversationAndSeenFalse(ConversationEntity conversation);
}