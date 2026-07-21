package org.example.backend.service;

import org.example.backend.dto.response.ConversationResponse;
import org.example.backend.entity.AdvertisementEntity;
import org.example.backend.entity.ConversationEntity;
import org.example.backend.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Contract for conversation service.
 */
public interface ConversationService {

    /**
     * Start new or return existing conversation.
     * @param buyer the buyer
     * @param advertisement the advertisement
     * @return the result
     */
    ConversationResponse startOrGetConversation(UserEntity buyer, AdvertisementEntity advertisement);

    /**
     * Get all conversations for a user.
     * @param user the user
     * @return the result
     */
    List<ConversationResponse> getUserConversations(UserEntity user);

    /**
     * Get conversation by ID (user must be participant).
     * @param conversationId the conversation id
     * @param currentUser the current user
     * @return the result
     */
    ConversationResponse getConversationById(Long conversationId, UserEntity currentUser);

    /**
     * Internal use - get conversation entity with access check.
     * @param conversationId the conversation id
     * @param currentUser the current user
     * @return the result
     */
    ConversationEntity getConversationEntityById(Long conversationId, UserEntity currentUser);

    /**
     * Update last message timestamp.
     * @param conversation the conversation
     * @param time the time
     */
    void touchLastMessageAt(ConversationEntity conversation, LocalDateTime time);
}