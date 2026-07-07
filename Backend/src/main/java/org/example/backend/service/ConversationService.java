package org.example.backend.service;

import org.example.backend.dto.response.ConversationResponse;
import org.example.backend.entity.AdvertisementEntity;
import org.example.backend.entity.ConversationEntity;
import org.example.backend.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface ConversationService {

    // Start a new conversation or get existing one between buyer and seller for an ad
    ConversationResponse startOrGetConversation(UserEntity buyer, AdvertisementEntity advertisement);

    // Get all conversations for a user (both as buyer and seller)
    List<ConversationResponse> getUserConversations(UserEntity user);

    // Get conversation by ID with access control (user must be a participant)
    ConversationResponse getConversationById(Long conversationId, UserEntity currentUser);

    // Get conversation entity by ID with access control (internal use by MessageService)
    ConversationEntity getConversationEntityById(Long conversationId, UserEntity currentUser);

    // Update last message timestamp for a conversation
    void touchLastMessageAt(ConversationEntity conversation, LocalDateTime time);
}