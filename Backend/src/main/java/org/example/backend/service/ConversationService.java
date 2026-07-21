package org.example.backend.service;

import org.example.backend.dto.response.ConversationResponse;
import org.example.backend.entity.AdvertisementEntity;
import org.example.backend.entity.ConversationEntity;
import org.example.backend.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface ConversationService {

    // Start new or return existing conversation
    ConversationResponse startOrGetConversation(UserEntity buyer, AdvertisementEntity advertisement);

    // Get all conversations for a user
    List<ConversationResponse> getUserConversations(UserEntity user);

    // Get conversation by ID (user must be participant)
    ConversationResponse getConversationById(Long conversationId, UserEntity currentUser);

    // Internal use - get conversation entity with access check
    ConversationEntity getConversationEntityById(Long conversationId, UserEntity currentUser);

    // Update last message timestamp
    void touchLastMessageAt(ConversationEntity conversation, LocalDateTime time);
}