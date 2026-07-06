package org.example.backend.service;

import org.example.backend.dto.response.ConversationResponse;
import org.example.backend.entity.Advertisement;
import org.example.backend.entity.Conversation;
import org.example.backend.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public interface ConversationService {

    // Start a new conversation or get existing one between buyer and seller for an ad
    ConversationResponse startOrGetConversation(User buyer, Advertisement advertisement);

    // Get all conversations for a user (both as buyer and seller)
    List<ConversationResponse> getUserConversations(User user);

    // Get conversation by ID with access control (user must be a participant)
    ConversationResponse getConversationById(Long conversationId, User currentUser);

    // Get conversation entity by ID with access control (internal use by MessageService)
    Conversation getConversationEntityById(Long conversationId, User currentUser);

    // Update last message timestamp for a conversation
    void touchLastMessageAt(Conversation conversation, LocalDateTime time);
}